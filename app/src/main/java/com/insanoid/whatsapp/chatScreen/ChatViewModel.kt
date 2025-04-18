package com.insanoid.whatsapp.presentation.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.insanoid.whatsapp.chatScreen.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val database: FirebaseDatabase
) : ViewModel() {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages = MutableStateFlow<List<Message>>(emptyList())

    private var messagesRef: DatabaseReference? = null
    private var currentUserId: String? = null
    private var receiverId: String? = null
    private var eventListener: ChildEventListener? = null

    fun initializeChat(contactPhone: String) {
        currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        receiverId = contactPhone

        currentUserId?.let { uid ->
            val chatPath = getChatPath(uid, contactPhone)
            messagesRef = database.getReference("chats/$chatPath/messages")
            setupMessageListener()
        }
    }

    private fun setupMessageListener() {
        eventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("ChatDebug", "New message added: ${snapshot.value}")
                viewModelScope.launch {
                    snapshot.getValue(Message::class.java)?.let { message ->
                        // Fixed key assignment
                        val messageWithKey = message.copy(snapshot.key ?: "")
                        _messages.value = _messages.value + messageWithKey
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                viewModelScope.launch {
                    snapshot.getValue(Message::class.java)?.let { updatedMessage ->
                        val messageWithKey = updatedMessage.copy( snapshot.key ?: "")
                        _messages.value = _messages.value.map {
                            if (it.key == messageWithKey.key) messageWithKey else it
                        }
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                viewModelScope.launch {
                    _messages.value = _messages.value.filterNot { it.key == snapshot.key }
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {
                Log.e("ChatViewModel", "Listener cancelled: ${error.message}")
            }
        }
        messagesRef?.addChildEventListener(eventListener!!)
    }

    fun sendMessage(message: Message) {
        val chatId = listOf(message.senderId, message.receiverId).sorted().joinToString("_")
        val messageRef = database.reference.child("chats/$chatId/messages").push()

        val msgWithKey = message.copy(key = messageRef.key ?: "")
        _messages.value = _messages.value + msgWithKey

        // ✅ 2. Send to Firebase
        messageRef.setValue(msgWithKey)
    }
    fun getMessages(senderId: String, receiverId: String) {
        val chatId = listOf(senderId, receiverId).sorted().joinToString("_")
        messagesRef = database.reference.child("chats/$chatId/messages")


        messagesRef?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messages = snapshot.children.mapNotNull {
                    it.getValue(Message::class.java)?.copy(key = it.key ?: "")
                }
                _messages.value = messages.sortedBy { it.timestamp } // CHANGE 2: Add sorting
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Chat", "Messages error: ${error.message}")
            }
        })
    }


    override fun onCleared() {
        eventListener?.let {
            messagesRef?.removeEventListener(it)
        }
        super.onCleared()
    }

    private fun getChatPath(user1: String, user2: String): String {
        return listOf(user1, user2).sorted().joinToString("_")
    }
}
