package com.insanoid.whatsapp.presentation.viewmodel

import android.R.id.message
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.RemoteCallbackList
import android.util.Base64
import android.util.Log
import androidx.compose.runtime.snapshots.Snapshot
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.insanoid.whatsapp.model.Message
import com.insanoid.whatsapp.presentation.bottomnavigation.chat_box.chatlistModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream

class BaseViewModel: ViewModel() {
    fun serachUserByPhoneNumber(phoneNumber: String,callback: (chatlistModel?)-> Unit){
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser==null){
            Log.d("BaseViewModel","User is not authenticated")
            callback(null)
            return
        }
        val databaseReference = FirebaseDatabase.getInstance().getReference("users")
        databaseReference. orderByChild("phoneNumber").equalTo(phoneNumber )
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot){
                     if (snapshot.exists()){
                         val user = snapshot.children.first().getValue(chatlistModel::class.java)
                         callback(user)
                     }
                    else{
                        callback(null)
                     }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(
                        "BaseViewModel",
                        "Error fetching user: ${error.message},Details: ${error.details}"

                    )
                    callback(null)

                }

            }
            )
    }
    fun getChatForUser(userId: String, callback: (List<chatlistModel>) -> Unit) {
        val chatref = FirebaseDatabase.getInstance().getReference("users/$userId/chats")
        chatref.orderByChild("userId").equalTo(userId)
            .addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val chatList= mutableListOf<chatlistModel>()
                    for(childSnapshot in snapshot.children){
                        val chat = childSnapshot.getValue(chatlistModel::class.java)
                        if (chat!=null){
                            chatList.add(chat)
                        }
                    }
                    callback(chatList)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("BaseViewModel","Error fetching chat: ${error.message}")
                    callback(emptyList())
                }

            })
    }
    private val _chatList = MutableStateFlow<List<chatlistModel>>(emptyList())
    val chatList= _chatList.asStateFlow()

    init {
            loadChatData()
    }

    private fun loadChatData(){
        val currentUser = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUser!=null){
            val chatRef =FirebaseDatabase.getInstance().getReference("chats")
            chatRef.orderByChild("userId").equalTo(currentUser)
                .addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val chatList = mutableListOf<chatlistModel>()
                        for (childSnapshot in snapshot.children){
                            val chat = childSnapshot.getValue(chatlistModel::class.java)
                            if (chat!=null){
                                chatList.add(chat)
                            }

                        }
                        _chatList.value = chatList
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("BaseViewModel","Error fetching chat: ${error.message}")
                    }

                })

        }
    }
    fun addChat(newChat: chatlistModel){
        val currentUser = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUser!=null){
            val newChatRef = FirebaseDatabase.getInstance().getReference("chats").push()
            val chatWithUser = newChat.copy(currentUser)
            newChatRef.setValue(chatWithUser).addOnSuccessListener {
                Log.d("BaseViewModel","Chat added successfully")
            }.addOnFailureListener {
                Log.e("BaseViewModel","Error adding chat: ${it.message}")
            }
        }
        else{
            Log.d("BaseViewModel","User is not authenticated")
        }
    }
    private val databaseReference = FirebaseDatabase.getInstance().reference
    fun sendMessage(senderPhoneNumber: String, receiverPhoneNumber: String,messageText: String) {
        val messageId = databaseReference.push().key?: return
        val message = Message(
            senderPhoneNumber = senderPhoneNumber,
            message = messageText,
            timestamp = System.currentTimeMillis()

        )
        databaseReference.child("messages")
            .child(senderPhoneNumber)
            .child(receiverPhoneNumber)
            .child(messageId)
            .setValue(message)

        databaseReference.child("messages")
            .child(receiverPhoneNumber)
            .child(senderPhoneNumber)
            .child(messageId)
            .setValue(message)

    }
    fun getMessage(senderPhoneNumber: String,receiverPhoneNumber: String, onNewMessage: (Message) -> Unit) {
        val messageRef = databaseReference.child("messages")
            .child(senderPhoneNumber)
            .child(receiverPhoneNumber)
        messageRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(Message::class.java)
                if (message != null) {
                    onNewMessage(message)
                }
            }

            override fun onChildChanged(
                snapshot: DataSnapshot,
                previousChildName: String?
            ) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(
                snapshot: DataSnapshot,
                previousChildName: String?
            ) {

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    fun fetchLastMessageForChat(
        senderPhoneNumber: String,receiverPhoneNumber: String,onLastMessageFetched: (String, String) -> Unit
    ){
        val chatRef = FirebaseDatabase.getInstance().reference
            .child("messages")
            .child(senderPhoneNumber)
            .child(receiverPhoneNumber)

        chatRef.orderByChild( "timestamp").limitToLast(1)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot){
                     if (snapshot.exists()){
                         val lastMessage = snapshot.children.firstOrNull()?.child("messages")?.value as? String
                         val timestamp = snapshot.children.firstOrNull()?.child("timestamp")?.value as? String
                          onLastMessageFetched(lastMessage?:"No message",timestamp?:"--:--")
                     }
                    else{
                         onLastMessageFetched("No message","--:--")
                     }
                }

                override fun onCancelled(error: DatabaseError) {
                    onLastMessageFetched("No message","--:--")
                }
            })

    }
    fun loadChatList(
        currentUserPhoneNumber: String,
        onChatListLoaded: (List<chatlistModel>) -> Unit
    ){
        val chatList =mutableListOf<chatlistModel>()
        val chatRef = FirebaseDatabase.getInstance().reference
            .child("chats")
            .child(currentUserPhoneNumber)
        chatRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    snapshot.children.forEach { child->
                        val phoneNumber =child.key?:return@forEach
                        val name = child.child("name").value as? String?:"Unknown"
                        val image = child.child("image").value as? String
                        val profileImageBitmap = image?.let { decodeBase64toBitmap(it) }
                        fetchLastMessageForChat(currentUserPhoneNumber,phoneNumber){lastMessage, time ->
                            chatList.add(
                                chatlistModel(
                                name = name,
                                image = profileImageBitmap as Int?,
                                message = lastMessage,
                                time = time
                            )
                            )
                            if (chatList.size == snapshot.childrenCount.toInt()){
                                onChatListLoaded(chatList)
                            }
                        }
                    }
                }
                else{
                    onChatListLoaded(emptyList())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                onChatListLoaded(emptyList())
            }

        })
    }
    private fun decodeBase64toBitmap(base64Image: String): Bitmap? {
        return try {
            val decodedByte = Base64.decode(base64Image, Base64.DEFAULT)
             BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.size)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }

    }
    fun base64ToBitmap(base64Image: String): Bitmap? {
        return try {
            val decodedByte = Base64.decode(base64Image, Base64.DEFAULT)
            val inputStream: InputStream = ByteArrayInputStream(decodedByte)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            null
        }
    }
}