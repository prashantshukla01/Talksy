package com.insanoid.whatsapp.presentation.homescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.insanoid.whatsapp.R
import com.insanoid.whatsapp.presentation.bottomnavigation.chat_box.chatlistModel
import com.insanoid.whatsapp.presentation.viewmodel.BaseViewModel

@Composable
fun ChatDesign(
    chatlistModel: chatlistModel,
    onClick:()-> Unit,
    baseViewModel: BaseViewModel){
    Row(
        modifier = Modifier.Companion.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val profileImage = chatlistModel?.profileImage
        val bitmap = remember{
            profileImage?.let{baseViewModel.base64ToBitmap(it)}
        }
        Image(
            painter = if (bitmap != null) {
                rememberImagePainter(bitmap)
            } else {
                painterResource(R.drawable.profile_placeholder)
            },
            contentDescription = null,
            modifier = Modifier.Companion
                .size(60.dp)
                .background(color = Color.Gray)
                .clip(shape = CircleShape),
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
                    text = chatlistModel.name?:"Unknown",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Companion.Bold
                )
                Text(
                    text = chatlistModel.time?:"--:--", color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.Companion.height(4.dp))
            Text(
                text = chatlistModel.message?:"message",
                color = Color.Companion.Gray,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}