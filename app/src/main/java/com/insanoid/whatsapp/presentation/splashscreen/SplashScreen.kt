package com.insanoid.whatsapp.presentation.splashscreen


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.insanoid.whatsapp.R
import com.insanoid.whatsapp.presentation.navigation.Routes
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(navHostController: NavHostController){
    val context = LocalContext.current
    val firebaseUser = FirebaseAuth.getInstance().currentUser

    LaunchedEffect(Unit){
        delay(2000)
        if (firebaseUser != null) {
            navHostController.navigate(Routes.HomeScreen) {
                popUpTo(Routes.SplashScreen) { inclusive = true }
            }
        } else {
            navHostController.navigate(Routes.WelcomeScreen) {
                popUpTo(Routes.SplashScreen) { inclusive = true }
            }
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = colorResource(R.color.white))){
        Image(painter = painterResource(id = R.drawable.talksy),
            contentDescription = null,
            modifier = Modifier.size(288.dp )
                .align(Alignment.Center)
        )
        Column(modifier = Modifier.align(Alignment.BottomCenter), horizontalAlignment = Alignment.CenterHorizontally ) {
            Text(text = "From", fontSize = 18.sp,
                fontWeight = FontWeight.Bold)

            Row {
                Icon(painter =painterResource(id = R.drawable.meta), contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = colorResource(R.color.light_green)
                )
                Text(text = "Meta", fontSize = 18.sp,
                    fontWeight= FontWeight.Bold)
            }
        }
    }
}