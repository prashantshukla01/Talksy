package com.insanoid.whatsapp.chatScreen
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Message(
    val senderId: String = "",
    val receiverId: String = "",
    val text: String = "",
    val timestamp: Long = 0L,
    @Exclude var key: String = ""
) {
    constructor() : this("", "", "", 0L) // For Firebase
}