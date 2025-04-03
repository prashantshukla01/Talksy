package com.insanoid.whatsapp.presentation.welcomescreen


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.navigation.NavHostController
import com.insanoid.whatsapp.R
import com.insanoid.whatsapp.presentation.navigation.Routes


@Composable
fun WelcomeScreen(navHostController: NavHostController){
    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement =  Arrangement.Center
        ){
        Image(painter = painterResource(id = R.drawable.whatsapp_sticker),
            contentDescription = null,
            modifier = Modifier.size(300.dp)
        )
            Text(text = "Welcome to WhatsApp", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))
        Row {
            Text(text = "Read our ", color= Color.Gray)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Privacy Policy", color = colorResource(id=R.color.light_green))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Tap 'Agree and Continue' to", color= Color.Gray)
            Text(text = "", color = colorResource(id=R.color.light_green))
        }

        Row {
            Text(text= "Accept the ", color= Color.Gray)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Terms and Services", color = colorResource(id=R.color.light_green))
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button( onClick = {navHostController.navigate( Routes.UserRegistrationScreen) },
            modifier = Modifier.size(280.dp,42.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id=R.color.dark_green))
        ){
        Text(text ="Agree and Continue", fontSize = 16.sp)
        }

    }
}
