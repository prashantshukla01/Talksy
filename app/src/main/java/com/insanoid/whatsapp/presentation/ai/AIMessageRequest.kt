package com.insanoid.whatsapp.presentation.ai

// AIMessageRequest.kt
data class AIMessageRequest(
    val messages: List<Message>,
    val model: String = "gpt-3.5-turbo"
) {
    data class Message(
        val role: String,
        val content: String
    )
}