// AIChatScreen.kt
package com.insanoid.whatsapp.presentation.ai

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.insanoid.whatsapp.R
import com.insanoid.whatsapp.chatScreen.Message
import com.insanoid.whatsapp.chatScreen.MessageBubble
import com.insanoid.whatsapp.chatScreen.MessageInputField

@Composable
fun AIChatScreen(navController: NavHostController) {
    val viewModel: AIChatViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    var messageText by remember { mutableStateOf("") }
    val scrollState = rememberLazyListState()

    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) {
            scrollState.animateScrollToItem(state.messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            AIChatTopAppBar(
                onBackClick = { navController.popBackStack() }
            )
        },
        content = { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(id = R.color.bg_grey)))
                    {
                        Column(
                            modifier = Modifier
                                .padding(padding)
                                .fillMaxSize()
                        ) {
                            LazyColumn(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth(),
                                state = scrollState,
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(state.messages) { message ->
                                    MessageBubble(
                                        message = Message(
                                            text = message.text,
                                            senderId = if (message.isUser) "user" else "ai",
                                            timestamp = message.timestamp
                                        ),
                                        isCurrentUser = message.isUser,
                                        maxWidth = LocalConfiguration.current.screenWidthDp.dp * 0.75f
                                    )
                                }
                            }

                            MessageInputField(
                                messageText = messageText,
                                onMessageChange = { messageText = it },
                                onSendMessage = {
                                    if (messageText.isNotBlank()) {
                                        viewModel.sendMessage(messageText)
                                        messageText = ""
                                    }
                                }
                            )
                        }
                    }
        }
    )
}

@Composable
fun AIChatTopAppBar(onBackClick: () -> Unit) {
    Surface(
        color = colorResource(id = R.color.dark_green),
        shadowElevation = 4.dp,
        modifier = Modifier.padding(horizontal = 0.dp, vertical = 36.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                    contentDescription = "Back",
                    tint = colorResource(id = R.color.white)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Talksy AI",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.white)
                )
                Text(
                    text = "Online",
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.teal_200)
                )
            }

            Icon(
                painter = painterResource(id = R.drawable.chat_bot),
                contentDescription = "AI Status",
                tint = colorResource(id = R.color.mint_green),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}