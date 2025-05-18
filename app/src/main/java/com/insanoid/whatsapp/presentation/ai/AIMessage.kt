package com.insanoid.whatsapp.presentation.ai

data class AIMessage(
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)