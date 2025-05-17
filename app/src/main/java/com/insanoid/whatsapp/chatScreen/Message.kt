package com.insanoid.whatsapp.chatScreen
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Message(
    val key: String = "", // Must be present
    val text: String = "",
    val senderId: String = "",
    val receiverId: String = "",
    val timestamp: Long = 0L
) {
    constructor() : this("", "", "", "", 0L)
}