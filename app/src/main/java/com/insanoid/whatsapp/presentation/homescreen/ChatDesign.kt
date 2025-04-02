package com.insanoid.whatsapp.presentation.homescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.insanoid.whatsapp.presentation.bottomnavigation.chat_box.chatlistModel

@Composable
fun ChatDesign(chatlistModel: chatlistModel){
    Row(
        modifier = Modifier.Companion.padding(8.dp),
        verticalAlignment = Alignment.Companion.CenterVertically
    ) {
        Image(
            painter = painterResource(id = chatlistModel.image),
            contentDescription = null,
            modifier = Modifier.Companion.size(60.dp).clip(shape = CircleShape),
            contentScale = ContentScale.Companion.Crop
        )
        Spacer(modifier = Modifier.Companion.height(12.dp))

        Column (modifier = Modifier
            .padding(start = 8.dp)
            .weight(1f)){
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.Companion.fillMaxWidth()
            ) {
                Text(
                    text = chatlistModel.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Companion.Bold
                )
                Text(
                    text = chatlistModel.time
                )
            }
            Spacer(modifier = Modifier.Companion.height(4.dp))
            Text(
                text = chatlistModel.message,
                color = Color.Companion.Gray,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}