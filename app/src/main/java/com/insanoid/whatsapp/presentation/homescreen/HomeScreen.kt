package com.insanoid.whatsapp.presentation.homescreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.insanoid.whatsapp.R
import com.insanoid.whatsapp.presentation.bottomnavigate.BottomNavigation
import com.insanoid.whatsapp.presentation.bottomnavigation.chat_box.chatlistModel

@Composable
@Preview(showSystemUi = true)
fun HomeScreen(){

    val chatdata = listOf(
        chatlistModel(
            R.drawable.salmankhan,
            name = "Salman Khan",
            time = "10:00 AM",
            message = "hello"
        ),

        chatlistModel(
            image = R.drawable.rashmika,
            name = "Rashmika",
            time = "10:22 PM",
            message = "What are you doing"
        ),
        chatlistModel(
            image = R.drawable.sharukhkhan,
            name = "Shahrukh Khan",
            time = "02:45 PM",
            message = "Reaching there"
        ),
        chatlistModel(
            image = R.drawable.sharadhakapoor,
            name = "Shraddha Kapoor",
            time = "12:22 AM",
            message = "call me later"
        ),
        chatlistModel(
            image = R.drawable.rajkummar_rao,
            name = "Raj Kumar",
            time = "yesterday",
            message = "How are you"
        )
    )

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { },
                containerColor = colorResource(R.color.light_green),
                modifier = Modifier.Companion.size(64.dp), contentColor = Color.Companion.White
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.chat_icon),
                    contentDescription = null,
                    modifier = Modifier.Companion.size(28.dp)
                )
            }
        },
        bottomBar = { BottomNavigation() }  // Ensure BottomNavigation() is correctly called
    )
    {
        Column(modifier = Modifier.Companion.padding(it)) {
            Box(modifier = Modifier.Companion.fillMaxWidth()) {
                Text(
                    text = "WhatsApp",
                    fontSize = 28.sp,
                    color = colorResource(id = R.color.light_green),
                    modifier = Modifier.Companion.align(Alignment.Companion.CenterStart)
                        .padding(start = 16.dp),
                    fontWeight = FontWeight.Companion.Bold
                )
                Row(modifier = Modifier.Companion.align(Alignment.Companion.CenterEnd)) {
                    IconButton(onClick = { }) {
                        Icon(
                            painter = painterResource(id = R.drawable.camera),
                            contentDescription = null,
                            modifier = Modifier.Companion.size(24.dp),
                        )
                    }
                    IconButton(onClick = { }) {
                        Icon(
                            painter = painterResource(id = R.drawable.search),
                            contentDescription = null,
                            modifier = Modifier.Companion.size(24.dp),
                        )
                    }
                    IconButton(onClick = { }) {
                        Icon(
                            painter = painterResource(id = R.drawable.more),
                            contentDescription = null,
                            modifier = Modifier.Companion.size(24.dp),
                        )
                    }
                }
            }
            HorizontalDivider()

            LazyColumn {
                items(chatdata) {
                    ChatDesign(chatlistModel = it)
                }
            }
        }
    }
}