package com.insanoid.whatsapp.model

data class Message(
    val senderPhoneNumber: String = "",
    val message: String ="",
    val timestamp: Long = 0L
)
