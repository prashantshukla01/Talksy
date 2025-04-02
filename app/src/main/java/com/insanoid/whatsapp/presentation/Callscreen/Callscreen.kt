package com.insanoid.whatsapp.presentation.Callscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.insanoid.whatsapp.R
import com.insanoid.whatsapp.presentation.bottomnavigate.BottomNavigation

@Composable
@Preview(showSystemUi = true)
fun CallScreen() {
    val sampleCall = listOf(
        Call(
            image = R.drawable.bhuvan_bam,
            name = "Bhuvan Bam",
            time = "Yesterday, 10:03 AM",
            isMissed = false
        ),
        Call(
            image = R.drawable.sharukhkhan,
            name = "Shahrukh Khan",
            time = "Today, 02:15 AM",
            isMissed = true
        ),
        Call(
            image = R.drawable.akshay_kumar,
            name = "Akshay Kumar",
            time = "Nov, 08:43 AM",
            isMissed = true
        ),
        Call(
            image = R.drawable.mrbeast,
            name = "Mr Beast",
            time = "Today, 07:03 AM",
            isMissed = false
        ),
        Call(
            image = R.drawable.sharukhkhan,
            name = "Shahrukh Khan",
            time = "Today, 02:15 AM",
            isMissed = true
        ),
        Call(
            image = R.drawable.akshay_kumar,
            name = "Akshay Kumar",
            time = "Nov, 08:43 AM",
            isMissed = true
        ),
    )

    var isSearching by remember {
        mutableStateOf(false)
    }
    var search by remember {
        mutableStateOf("")
    }
    var showmenu by remember {
        mutableStateOf(false)
    }
    Scaffold(
        topBar = {
            Box(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Row(modifier = Modifier.padding(top = 40.dp)) {
                        if (isSearching) {
                            TextField(
                                value = search, onValueChange = {
                                    search = it
                                }, placeholder = {
                                    Text(text = "Search")
                                }, colors = TextFieldDefaults.colors(
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ), modifier = Modifier.padding(start = 12.dp), singleLine = true
                            )
                        } else {
                            Text(
                                text = "Calls",
                                fontSize = 28.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(start = 12.dp, top = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        if (isSearching) {
                            IconButton(onClick = {
                                isSearching = false
                                search = ""
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.cross),
                                    contentDescription = null,
                                    modifier = Modifier.size(15.dp)
                                )
                            }

                        } else {
                            IconButton(onClick = { isSearching = true }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.search),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            IconButton(onClick = {
                                showmenu = true
                            }
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.more),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )
                                DropdownMenu(
                                    expanded = showmenu,
                                    onDismissRequest = { showmenu = false }) {
                                    DropdownMenuItem(
                                        text = { Text(text = "Settings") },
                                        onClick = { showmenu = false })
                                }
                            }
                        }

                    }
                    HorizontalDivider()
                }
            }
        },
        bottomBar = {
            BottomNavigation()
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { },
                containerColor = colorResource(id=R.color.light_green),
                modifier = Modifier.size(64.dp),
                contentColor = Color.White) {
                Icon(painter = painterResource(id= R.drawable.add_call), contentDescription = null)
            }
        },
    )
    {
        Column(modifier = Modifier.padding(it)) {
            Spacer(modifier = Modifier.height(16.dp))
            FavoritesScreen()

            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.light_green)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Start a new call",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Recent calls",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            LazyColumn {
                items(sampleCall){ data->
                    CallItemDesign(data)
                }
            }


        }
    }
}
