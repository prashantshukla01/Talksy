package com.insanoid.whatsapp.chatScreen
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Message(
    val text: String = "",
    val timestamp: Long = 0L,
    val senderId: String = "",
    val receiverId: String = "",
    val status: String = "SENT"
) {
    @Exclude
    var key: String = ""

    constructor() : this("", 0L, "", "", "SENT")
}
