package com.insanoid.whatsapp.presentation.ai

data class AIMessageRequest(
    val message: String,
    val context: String,
    val model: String = "gpt-3.5-turbo"
)