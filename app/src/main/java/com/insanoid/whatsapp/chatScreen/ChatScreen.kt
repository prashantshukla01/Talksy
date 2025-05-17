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
import androidx.compose.material.icons.filled.Mic
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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow


// Define color scheme constants
private val WhatsAppGreen = Color(0xFF273F4F)
private val WhatsAppLightGreen = Color(0xFF25D366)
private val WhatsAppBackground = Color(0xFF94B4C1)
private val WhatsAppBubbleOutgoing = Color(0xFFFFFBDE)
private val WhatsAppBubbleIncoming = Color.White
private val WhatsAppHeaderColor = Color(0xFF213448)

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
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    // Handle screen scrolling when new messages are added
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            scrollState.animateScrollToItem(messages.size - 1)
        }
    }

    // Initialize chat with contact
    LaunchedEffect(Unit) {
        viewModel.initializeChat(contactPhone)
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
            // Chat wallpaper background
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(WhatsAppBackground)
            ) {
                // Optional: Add background wallpaper image
                // Image(
                //     painter = painterResource(id = R.drawable.chat_background),
                //     contentDescription = null,
                //     modifier = Modifier.fillMaxSize(),
                //     contentScale = ContentScale.Crop,
                //     alpha = 0.2f
                // )

                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                ) {
                    // Chat date header
                    ChatDateHeader("Today")

                    // Message list
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        state = scrollState,
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items( items = messages,
                            key = { message ->
                                // Use the Firebase-generated key
                                message.key
                            }) { message ->
                            MessageBubble(
                                message = message,
                                isCurrentUser = message.senderId == FirebaseAuth.getInstance().currentUser?.uid,
                                maxWidth = screenWidth * 0.75f
                            )
                        }
                    }

                    // Input field
                    MessageInputField(
                        messageText = messageText,
                        onMessageChange = { messageText = it },
                        onSendMessage = {
                            if (messageText.isNotBlank()) {
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
        }
    )
}

@Composable
fun ChatTopAppBar(
    contactName: String,
    contactPhone: String,
    onBackClick: () -> Unit
) {
    Surface(
        color = WhatsAppHeaderColor,
        shadowElevation = 4.dp, modifier = Modifier.padding(horizontal = 0.dp, vertical = 36.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back_arrow),
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            // Contact profile picture
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            ) {
                // You can replace this with AsyncImage for loading actual profile photos
                Text(
                    text = contactName.first().toString(),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = contactName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Online",  // You can replace with dynamic status
                    fontSize = 12.sp,
                    color = Color.LightGray,
                    maxLines = 1
                )
            }

            IconButton(onClick = { /* Handle video call */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.camrecorder),
                    modifier = Modifier.size(28.dp),
                    contentDescription = "Video Call",
                    tint = Color.White
                )
            }

            IconButton(onClick = { /* Handle voice call */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.add_call),
                    contentDescription = "Call",
                    tint = Color.White
                )
            }

            IconButton(onClick = { /* Handle more options */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_more_vert),
                    contentDescription = "More options",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun ChatDateHeader(dateText: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.8f)
            ),
            shape = RoundedCornerShape(16.dp),
        ) {
            Text(
                text = dateText,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun MessageBubble(message: Message, isCurrentUser: Boolean, maxWidth: androidx.compose.ui.unit.Dp) {
    val bubbleColor = if (isCurrentUser) WhatsAppBubbleOutgoing else WhatsAppBubbleIncoming
    val alignment = if (isCurrentUser) Alignment.CenterEnd else Alignment.CenterStart
    val bubbleShape = if (isCurrentUser) {
        RoundedCornerShape(16.dp, 0.dp, 16.dp, 16.dp)
    } else {
        RoundedCornerShape(0.dp, 16.dp, 16.dp, 16.dp)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        contentAlignment = alignment
    ) {
        Card(
            modifier = Modifier.widthIn(max = maxWidth),
            colors = CardDefaults.cardColors(containerColor = bubbleColor),
            shape = bubbleShape,
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = message.text,
                    color = Color.Black,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

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

                    if (isCurrentUser) {
                        Spacer(modifier = Modifier.width(4.dp))
                        MessageStatusIndicator(status = MessageStatus.DELIVERED)
                    }
                }
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
    val shouldShowSendButton = messageText.isNotBlank()

    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 0.dp).height(62.dp),
        color = Color.White,
        shape = RoundedCornerShape(32.dp),
        shadowElevation = 8.dp

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Emoji button
            IconButton(
                onClick = { /* Handle emoji picker */ },
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.happiness),
                    contentDescription = "Emoji",
                    tint = Color.Gray
                )
            }

            // Text field
            TextField(
                value = messageText,
                onValueChange = onMessageChange,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(42.dp)),
                placeholder = { Text("Message", color = Color.Gray) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = WhatsAppGreen
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = { onSendMessage() }),
                maxLines = 6
            )

            // Attachment button
            IconButton(
                onClick = { /* Handle attachment */ },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_attach_file_24),
                    contentDescription = "Attach file",
                    tint = Color.Gray
                )
            }

            // Camera button
            IconButton(
                onClick = { /* Handle camera */ },
                modifier = Modifier.size(30.dp).padding(start = 0.dp, end = 8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.camera),
                    contentDescription = "Camera",
                    tint = Color.Gray
                )
            }

            // Send or Voice button
            AnimatedVisibility(
                visible = shouldShowSendButton,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                IconButton(
                    onClick = onSendMessage,
                    modifier = Modifier
                        .size(36.dp)
                        .background(WhatsAppGreen, CircleShape)
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send message",
                        tint = Color.White
                    )
                }
            }

            AnimatedVisibility(
                visible = !shouldShowSendButton,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                IconButton(
                    onClick = { /* Handle voice recording */ },
                    modifier = Modifier
                        .size(36.dp)
                        .background(WhatsAppGreen, CircleShape)
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Voice message",
                        tint = Color.White
                    )
                }
            }
        }
    }
}