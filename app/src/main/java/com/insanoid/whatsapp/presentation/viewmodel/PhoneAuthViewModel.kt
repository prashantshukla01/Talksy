package com.insanoid.whatsapp.presentation.viewmodel

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.insanoid.whatsapp.model.PhoneAuthUser

import com.insanoid.whatsapp.presentation.viewmodel.PhoneAuthViewModel.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.collections.getValue
import kotlin.text.get

@HiltViewModel
class PhoneAuthViewModel @Inject constructor(
    internal val firebaseAuth: FirebaseAuth,
    private val database: FirebaseDatabase
):ViewModel() {
    internal val _authState = MutableStateFlow<AuthState>(AuthState.Ideal)
    val authState = _authState.asStateFlow()

    private val userRef =
        FirebaseDatabase.getInstance().reference.child("users") // ✅ Fixed Firebase reference

    fun sendVerificationCode(phoneNumber: String, activity: Activity) {
        _authState.value = AuthState.Loading
        val option = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(id, token)
                Log.d("PhoneAuth", "onCodeSent triggered. verification ID: $id")
                _authState.value = AuthState.CodeSent(verificationId = id)
            }

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signWithCredential(credential, context = activity) // ✅ Fixed function parameter
            }

            override fun onVerificationFailed(exception: FirebaseException) {
                Log.d("PhoneAuth", "onVerificationFailed: ${exception.message}")
                _authState.value = AuthState.Error(exception.message ?: "Verification failed")
            }
        }

        //otp management
        val PhoneAuthOptions = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(option)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(PhoneAuthOptions)

    }


    sealed class AuthState {
        object Ideal : AuthState()
        object Loading : AuthState()
        data class CodeSent(val verificationId: String) : AuthState()
        data class Success(val user: PhoneAuthUser) : AuthState()
        data class Error(val message: String) : AuthState()
    }

    private fun PhoneAuthViewModel.signWithCredential(
        credential: PhoneAuthCredential,
        context: android.content.Context
    ) { // ✅ Fixed function scope and import
        _authState.value = AuthState.Loading
        firebaseAuth.signInWithCredential(credential) // ✅ Fixed method name
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    val phoneAuthUser = PhoneAuthUser(
                        userId = user?.uid ?: "",
                        phoneNumber = user?.phoneNumber ?: ""
                    )
                    markUserAsSignedIn(context)
                    _authState.value = AuthState.Success(phoneAuthUser)

                    fetchUserProfile(user?.uid ?: "")
                } else {
                    _authState.value = AuthState.Error(
                        task.exception?.message ?: "Something went wrong"
                    ) // ✅ Fixed misplaced parenthesis
                }
            }
    }

    private fun fetchUserProfile(userId: String) {
        val userRef = userRef.child(userId)
        userRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {

                val userProfile = snapshot.getValue(PhoneAuthUser::class.java)
                if (userProfile != null) {
                    _authState.value = AuthState.Success(userProfile)
                }
            }

        }.addOnFailureListener {
            _authState.value = AuthState.Error(it.message ?: "Something went wrong")
        }
    }

    fun verifyCode(code: String, context: Context) {
        val currentAuthState = _authState.value
        if (currentAuthState !is AuthState.CodeSent || currentAuthState.verificationId.isEmpty()) {
            Log.e("PhoneAuth", "Invalid verification ID")
            _authState.value = AuthState.Error("Invalid verification ID")
            return
        }
        val credential = PhoneAuthProvider.getCredential(currentAuthState.verificationId, code)
        signWithCredential(credential, context)
    }

    fun saveUserProfile(userId: String, name: String, status: String, profileImage: Bitmap?) {
        val database = FirebaseDatabase.getInstance().reference
        val encodedImage = profileImage?.let { convertBitmapToBase64(it) }
        val userProfile = PhoneAuthUser(
            userId = userId,
            name = name,
            status = status,
            phoneNumber = FirebaseAuth.getInstance().currentUser?.phoneNumber ?: ""
        )
        database.child("users").child(userId).setValue(userProfile)

    }

    fun convertBitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG ,100,byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)


    }
    fun resetAuthState(){
        _authState.value = AuthState.Ideal
    }

    //logout user from firebase
    fun signOut(activity: Activity){
        firebaseAuth.signOut()
        val sharedPreferences = activity.getSharedPreferences("app_prefs", Activity.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("isSigned",false).apply()
    }

    private fun markUserAsSignedIn(context: Context) {
        //for storing data in key value pair form
        val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("is_signed_in", true).apply()

    }

}

