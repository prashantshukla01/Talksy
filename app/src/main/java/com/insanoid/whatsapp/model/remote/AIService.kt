package com.insanoid.whatsapp.model.remote

import com.insanoid.whatsapp.presentation.ai.AIMessageRequest
import com.insanoid.whatsapp.presentation.ai.AIResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AIService {
    @POST("chat/completions")
    suspend fun getAIResponse(
        @Body request: AIMessageRequest
    ): AIResponse
}