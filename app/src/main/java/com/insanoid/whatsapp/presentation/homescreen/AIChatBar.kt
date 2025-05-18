package com.insanoid.whatsapp.presentation.homescreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.insanoid.whatsapp.R
import com.insanoid.whatsapp.presentation.viewmodel.BaseViewModel

@Composable
fun AIChatBar(
    onClick: () -> Unit,
    baseViewModel: BaseViewModel
) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ai_chat), // Create this drawable
            contentDescription = "AI Chat",
            modifier = Modifier
                .size(60.dp),
            tint = colorResource(id = R.color.mint_green)
        )

        Column(
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(1f)
        ) {
            Text(
                text = "Talksy AI",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Ask me anything!",
                color = colorResource(id = R.color.teal_200),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Icon(
            painter = painterResource(R.drawable.chat_bot), // Create this drawable
            contentDescription = "AI Status",
            tint = colorResource(id = R.color.light_green),
            modifier = Modifier.size(24.dp)
        )
    }
}