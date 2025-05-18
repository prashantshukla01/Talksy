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
    private val apiService: AIService
) : ViewModel() {

    private val _state = MutableStateFlow(AIChatState())
    val state = _state.asStateFlow()

    fun sendMessage(message: String) {
        viewModelScope.launch {
            _state.update { currentState ->
                currentState.copy(
                    messages = currentState.messages + AIMessage(
                        text = message,
                        isUser = true
                    ),
                    isLoading = true,
                    error = null
                )
            }

            try {
                val response = apiService.getAIResponse(
                    AIMessageRequest(
                        message = message,
                        context = "chat"
                    )
                )

                _state.update { currentState ->
                    currentState.copy(
                        messages = currentState.messages + AIMessage(
                            text = response.answer,
                            isUser = false
                        ),
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        error = "Failed to get response: ${e.localizedMessage}"
                    )
                }
            }
        }
    }
}