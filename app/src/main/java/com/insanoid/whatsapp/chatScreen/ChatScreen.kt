package com.insanoid.whatsapp.chatScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.insanoid.whatsapp.R
import com.insanoid.whatsapp.presentation.chat.ChatViewModel
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun ChatScreen(
    contactName: String,
    contactPhone: String,
    navController: NavHostController,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val messages by viewModel.messages.collectAsState()
    var messageText by remember { mutableStateOf("") }
    val scrollState = rememberLazyListState()


    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            scrollState.animateScrollToItem(messages.size - 1)
        }
    }

    LaunchedEffect(Unit) { // CHANGE 1: Use Unit instead of contactPhone
        viewModel.initializeChat(

            contactPhone
        )
    }


    @Composable
    fun ChatTopAppBar(
        contactName: String,
        contactPhone: String,
        onBackClick: () -> Unit
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(40.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back_arrow),
                    contentDescription = "Back"
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = contactName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = contactPhone,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
            IconButton(onClick = { /* Handle more options */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_more_vert),
                    contentDescription = "More options"
                )
            }
        }
    }

    Scaffold(
        topBar = {
            ChatTopAppBar(
                contactName = contactName,
                contactPhone = contactPhone,
                onBackClick = { navController.popBackStack() }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .background(Color(0xFFECE5DD))
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    state = scrollState,
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(messages, key = { it.timestamp }) { message ->
                        MessageBubble(
                            message = message,
                            isCurrentUser = message.senderId == FirebaseAuth.getInstance().currentUser?.uid
                        )
                    }
                }

                MessageInputField(
                    messageText = messageText,
                    onMessageChange = { messageText = it },
                    // In your ChatScreen composable
                    onSendMessage = {
                        if (messageText.isNotBlank()) {
                            // Send message
                            viewModel.sendMessage(
                                Message(
                                    text = messageText,
                                    senderId = FirebaseAuth.getInstance().currentUser?.uid ?: "",
                                    receiverId = contactPhone,
                                    timestamp = System.currentTimeMillis()
                                )
                            )
                            messageText = ""
                        }
                    }
                )
            }
        }
    )
}

@Composable
fun MessageBubble(message: Message, isCurrentUser: Boolean) {
    val bubbleColor = if (isCurrentUser) Color(0xFFDCF8C6) else Color.White
    val alignment = if (isCurrentUser) Alignment.End else Alignment.Start

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = alignment as Alignment
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(bubbleColor)
                .padding(12.dp)
        ) {
            Text(
                text = message.text,
                color = Color.Black,
                fontSize = 16.sp
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = SimpleDateFormat("hh:mm a", Locale.getDefault())
                        .format(message.timestamp),
                    color = Color.Gray,
                    fontSize = 10.sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                MessageStatusIndicator(status = MessageStatus.SENT)
            }
        }
    }
}

@Composable
fun MessageStatusIndicator(status: MessageStatus) {
    val icon = when (status) {
        MessageStatus.SENT -> Icons.Default.Check
        MessageStatus.DELIVERED -> Icons.Default.DoneAll
        MessageStatus.READ -> Icons.Default.DoneAll
    }
    val color = when (status) {
        MessageStatus.READ -> Color.Blue
        else -> Color.Gray
    }

    Icon(
        imageVector = icon,
        contentDescription = "Message status",
        tint = color,
        modifier = Modifier.size(12.dp)
    )
}

@Composable
fun MessageInputField(
    messageText: String,
    onMessageChange: (String) -> Unit,
    onSendMessage: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.White, RoundedCornerShape(24.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = messageText,
            onValueChange = onMessageChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text("Type a message") },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            keyboardActions = KeyboardActions(onSend = { onSendMessage() })
        )

        IconButton(onClick = onSendMessage) {
            Icon(
                painter = painterResource(R.drawable.ic_send),
                contentDescription = "Send message",
                tint = Color(0xFF075E54)
            )
        }
    }
}