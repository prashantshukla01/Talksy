package com.insanoid.whatsapp.presentation.ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.insanoid.whatsapp.model.remote.AIService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.plus

@HiltViewModel
class AIChatViewModel @Inject constructor(
    private val aiService: AIService
) : ViewModel() {

    private val _state = MutableStateFlow(AIChatState())
    val state = _state.asStateFlow()

    fun sendMessage(message: String) {
        viewModelScope.launch {
            try {
                // Add user message
                _state.update { it.copy(
                    messages = it.messages + AIMessage(message, true),
                    isLoading = true
                )}

                // API call
                val response = aiService.getAIResponse(
                    AIMessageRequest(
                        messages = listOf(
                            AIMessageRequest.Message(
                                role = "user",
                                content = message
                            )
                        ),
                        model = "gpt-3.5-turbo"
                    )
                )

                // Add AI response
                _state.update { it.copy(
                    messages = it.messages + AIMessage(
                        text = response.choices.first().message.content,
                        isUser = false
                    ),
                    isLoading = false
                )}
            } catch (e: Exception) {
                _state.update { it.copy(
                    error = "Failed to get response: ${e.message}",
                    isLoading = false
                )}
            }
        }
    }
}