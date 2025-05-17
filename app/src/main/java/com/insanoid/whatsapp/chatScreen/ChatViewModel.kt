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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val database: FirebaseDatabase
) : ViewModel() {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private var messagesRef: DatabaseReference? = null
    private var currentUserId: String? = null
    private var receiverId: String? = null
    private var eventListener: ChildEventListener? = null

    private val existingKeys = mutableSetOf<String>()


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
                val key = snapshot.key ?: return
                viewModelScope.launch {
                    // 1. Check for existing key first
                    if (existingKeys.contains(key)) {
                        Log.w("ChatDebug", "Duplicate key detected: $key")
                        return@launch
                    }

                    // 2. Get message with proper key assignment
                    snapshot.getValue(Message::class.java)?.let { message ->
                        val messageWithKey = message.copy(key = key)

                        // 3. Update state atomically
                        _messages.update { currentMessages ->
                            if (currentMessages.any { it.key == key }) currentMessages
                            else currentMessages + messageWithKey
                        }
                        existingKeys.add(key)
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
                    val key = snapshot.key ?: return@launch
                    _messages.update { it.filterNot { msg -> msg.key == key } }
                    existingKeys.remove(key)
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
//        val chatId = listOf(message.senderId, message.receiverId).sorted().joinToString("_")
//        val messageRef = database.reference.child("chats/$chatId/messages").push()
        val chatId = getChatPath(message.senderId, message.receiverId)
        val messagesRef = database.reference.child("chats/$chatId/messages")
        val newMessageRef = messagesRef.push() // Generates unique key

        val messageWithKey = message.copy(
            key = newMessageRef.key ?: "",
            timestamp = System.currentTimeMillis()
        )
        if (!existingKeys.contains(messageWithKey.key)) {
            existingKeys.add(messageWithKey.key)
            _messages.value = _messages.value + messageWithKey
        }

        // ✅ 2. Send to Firebase
        newMessageRef.setValue(messageWithKey).addOnCompleteListener {
            if (!it.isSuccessful) {
                // Remove from local state if Firebase fails
                _messages.value = _messages.value - messageWithKey
                existingKeys.remove(messageWithKey.key)
            }
        }
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
