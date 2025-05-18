package com.insanoid.whatsapp.presentation.ai

import com.insanoid.whatsapp.presentation.ai.AIMessage

data class AIChatState(
    val messages: List<AIMessage> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
