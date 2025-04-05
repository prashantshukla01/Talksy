package com.insanoid.whatsapp.presentation.updatescreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.insanoid.whatsapp.R
import com.insanoid.whatsapp.presentation.bottomnavigate.BottomNavigation
import com.insanoid.whatsapp.presentation.navigation.Routes

@Composable
fun UpdateScreen(navHostController: NavHostController){
    val scrollState= rememberScrollState()

    val samplestatus= listOf(
        statusData(image = R.drawable.disha_patani, name = "Disha Patani", time = "10 min ago"),
        statusData(image = R.drawable.ajay_devgn, name = "Ajay Devgn", time = "18 min ago"),
        statusData(image = R.drawable.tripti_dimri, name = "Tripti", time = "1 hour ago")
    )

    val samplechannel= listOf(
        Channels(image = R.drawable.neat_roots , name = "Neat Roots", description = "Latest news in tech"),
        Channels(image = R.drawable.meta , name = "Meta", description = "Explore the world"),
        Channels(image = R.drawable.img , name = "Gennnie", description = "Get healthy food")
    )
    Scaffold (floatingActionButton = {
        FloatingActionButton(onClick = { },
            containerColor = colorResource(id=R.color.light_green),
            modifier = Modifier.size(64.dp),
            contentColor = Color.White) {
            Icon(painter = painterResource(id= R.drawable.baseline_photo_camera_24), contentDescription = null)
        }
    },
        bottomBar = {
            BottomNavigation(navHostController, selectedItem = 0, onClick = {index->
                when(index){
                    0-> {navHostController.navigate(Routes.HomeScreen)}
                    1-> {navHostController.navigate(Routes.UpdateScreen)}
                    2-> {navHostController.navigate(Routes.CommunitiesScreen)}
                    3-> {navHostController.navigate(Routes.CallScreen)}
                }

            })
        },
        topBar = {
            TopBar()
        }
    ) {
        Column(modifier = Modifier.padding(it)
            .fillMaxSize()
            .verticalScroll(scrollState)) {
            Text(text ="Status", fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color =Color.Black,
                modifier =  Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            )
            MyStatus()
            samplestatus.forEach {
                StatusItem(statusData= it)
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(
                color=Color.Gray
            )

            Text(text="Channels",
                fontSize = 20.sp ,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(text = "stay updated on topics that matter to you.Find channels to follow below")

                Spacer(modifier = Modifier.height(32.dp))
                Text(text = "Find channels to follow")
            }
            Spacer(modifier = Modifier.height(16.dp))
            samplechannel.forEach {
                 ChannelItemDesign(channel = it)
            }
        }
    }
}