package com.insanoid.whatsapp.presentation.profile

import com.insanoid.whatsapp.R
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.compose.ui.graphics.Color

import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.google.firebase.Firebase
import androidx.compose.material3.TextFieldDefaults
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.auth
import com.insanoid.whatsapp.presentation.navigation.Routes
import com.insanoid.whatsapp.presentation.viewmodel.PhoneAuthViewModel



@Composable
fun UserProfileSetScreen(phoneAuthViewModel: PhoneAuthViewModel = hiltViewModel(), navHostController: NavHostController){
    var name by remember{ mutableStateOf("") }
    var status by remember { mutableStateOf("") }
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }
    var bitmapImage by remember { mutableStateOf<Bitmap?>(null) }


    val firebaseAuth = Firebase.auth
    val phoneNumber = firebaseAuth.currentUser?.phoneNumber ?: ""
    val userId = firebaseAuth.currentUser?.uid ?: ""

    val context = LocalContext.current
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            profileImageUri = uri
            uri?.let {
                bitmapImage= if(Build.VERSION.SDK_INT<28){
                    @Suppress("DEPRECATION")
                    MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                } else {
                    val source = ImageDecoder.createSource(context.contentResolver, it)
                    ImageDecoder.decodeBitmap(source)

                }
                }

            }
            )
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(128.dp)
                .clip(CircleShape)
                .border(2.dp, color = Color.Gray, shape = CircleShape)
                .clickable {
                    imagePickerLauncher.launch("image/*")
                }){
                         if(bitmapImage!= null){
                             Image(
                                 bitmap = bitmapImage!!.asImageBitmap(),
                                 contentDescription = null,
                                 modifier = Modifier
                                     .fillMaxSize()
                                     .clip(CircleShape),
                                 contentScale = ContentScale.Crop
                             )
                         }
                        else if (profileImageUri!=null){
                             Image(
                                 painter = rememberImagePainter(profileImageUri) ,
                                 contentDescription = null,
                                 modifier = Modifier
                                     .fillMaxSize()
                                     .clip(CircleShape),
                                 contentScale = ContentScale.Crop
                             )

                         }
                        else{
                             Image(
                                 painter = androidx.compose.ui.res.painterResource(id = R.drawable.profile_placeholder),
                                 contentDescription = null,
                                 modifier = Modifier
                                     .fillMaxSize()
                                     .align(Alignment.Center)
                             )
                         }
                    }
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "$phoneNumber")
        Spacer(modifier = Modifier.padding(8.dp))

        TextField(
            value = name,
            onValueChange = { name = it }, // or use { newValue -> name = newValue }
            label = {
                Text(text = "Name")
            },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedIndicatorColor = colorResource(R.color.light_green)
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = status,
            onValueChange = { status= it }, // or use { newValue -> name = newValue }
            label = {
                Text(text = "Status")
            },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedIndicatorColor = colorResource(R.color.light_green)
            )
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = {
            phoneAuthViewModel.saveUserProfile(userId, name, status, bitmapImage)
            navHostController.navigate(Routes.HomeScreen)
        }, colors = ButtonDefaults.buttonColors(colorResource(R.color.light_green))) {
            Text(text = "Save")

        }


    }
}



