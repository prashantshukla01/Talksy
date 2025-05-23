package com.insanoid.whatsapp.presentation.homescreen

import androidx.compose.foundation.background
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.compose.currentBackStackEntryAsState

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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.insanoid.whatsapp.R
import com.insanoid.whatsapp.presentation.bottomnavigate.BottomNavigation
import com.insanoid.whatsapp.presentation.bottomnavigation.chat_box.chatlistModel
import com.insanoid.whatsapp.presentation.navigation.Routes
import com.insanoid.whatsapp.presentation.viewmodel.BaseViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(navHostController: NavHostController, homeBaseViewModel: BaseViewModel){
    var showPopup by remember { mutableStateOf(false) }
    val chatData by homeBaseViewModel.chatList.collectAsState()
    val userId= FirebaseAuth.getInstance().currentUser?.uid
    val lifecycleOwner = rememberUpdatedState(newValue = LocalLifecycleOwner.current)
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val homeScreenRoute = Routes.HomeScreen::class.qualifiedName

    DisposableEffect(key1 = navBackStackEntry?.destination?.route) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME && currentRoute == homeScreenRoute) {
                userId?.let {
                    homeBaseViewModel.getChatForUser(it) {}
                }
            }

        }

        val lifecycle = lifecycleOwner.value.lifecycle
        lifecycle.addObserver(observer)

        onDispose {
            lifecycle.removeObserver(observer)
        }
    }



    if (userId != null) {
        LaunchedEffect(userId) {
            homeBaseViewModel.getChatForUser(userId) { chats ->

            }
        }
    }
    var showMenu by remember { mutableStateOf(false) }

    Scaffold(floatingActionButton = {
        FloatingActionButton(
            onClick = {
                showPopup= true
            },
            containerColor = colorResource(id = R.color.dark_green),
            modifier = Modifier.size(64.dp),
            contentColor = Color.White)
        {
            Icon(
                painter = painterResource(R.drawable.add_chat_icon),
                contentDescription = null,
                modifier = Modifier.size(28.dp),
                tint = Color.White
            )
        }
    }, bottomBar = {
        BottomNavigation(navHostController, selectedItem = 0, onClick = {index->
            when(index){
                0-> {navHostController.navigate(Routes.HomeScreen)}
                1-> {navHostController.navigate(Routes.UpdateScreen)}
                2-> {navHostController.navigate(Routes.CommunitiesScreen)}
                3-> {navHostController.navigate(Routes.CallScreen)}
            }

        })
    }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .background(color =colorResource(R.color.white))
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Box(modifier = Modifier.fillMaxWidth()) {
                var isSearching by remember {
                    mutableStateOf(false)
                }
                var searchText by remember {
                    mutableStateOf("")
                }
                var showMenu by remember {
                    mutableStateOf(false)
                }
                if (isSearching) {
                    TextField(
                        value = searchText,
                        onValueChange = {
                            searchText = it
                        },
                        placeholder = {
                            Text(text = "Search", color = Color.Gray)
                        },
                        singleLine = true,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 12.dp)
                            .fillMaxWidth(0.8f),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            cursorColor = colorResource(id = R.color.light_green)
                        )
                    )

                } else {
                    Text(
                        "Talksy",
                        fontSize = 28.sp,
                        color = colorResource(R.color.light_green),
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(12.dp),
                        fontWeight = FontWeight.Bold
                    )
                    Row(modifier = Modifier.align(Alignment.CenterEnd)) {
                        IconButton(onClick = {}) {
                            Icon(
                                painter = painterResource(R.drawable.camera),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        if (isSearching) {
                            IconButton(onClick = {
                                isSearching = false
                                searchText = ""
                            }) {
                                Icon(
                                    painter = painterResource(R.drawable.cross), contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )

                            }
                        }
                        else{
                            IconButton(onClick = {
                                isSearching = true

                            }) {
                                Icon(
                                    painter = painterResource(R.drawable.search),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )

                            }
                        }
                        IconButton(onClick = {
                            showMenu = !showMenu
                        }) {
                            Icon(
                                painter = painterResource(R.drawable.more),
                                contentDescription = null, modifier = Modifier.size(24.dp)
                            )
                            DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false },
                                modifier = Modifier.background(color = Color.White)) {
                                DropdownMenuItem(
                                    text = { Text("New group") },
                                    onClick = { showMenu = false })
                                DropdownMenuItem(
                                    text = { Text("New Broadcast") },
                                    onClick = { showMenu = false })

                                DropdownMenuItem(
                                    text = { Text("Linked Device") },
                                    onClick = { showMenu = false })
                                DropdownMenuItem(
                                    text = { Text("Starred Messages") },
                                    onClick = { showMenu = false })
                                DropdownMenuItem(text = { Text("Settings") }, onClick = {
                                    showMenu = false
                                    navHostController.navigate(Routes.SettingScreen)
                                })
                                DropdownMenuItem(
                                    text = { Text("Logout") },
                                    onClick = {
                                        showMenu = false
                                        FirebaseAuth.getInstance().signOut()
                                        navHostController.navigate(Routes.WelcomeScreen) {
                                            popUpTo(Routes.HomeScreen) { inclusive = true }
                                        }
                                    }
                                )

                            }
                        }
                    }
                }
                
            }
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))
            if (showPopup){
                AddUserPopup(onDismiss = { showPopup = false }, onUserAdd = { newUser ->
                    homeBaseViewModel.addChat(newUser)
                }, baseViewModel =  homeBaseViewModel)
            }

            LazyColumn(modifier = Modifier.fillMaxWidth()){
                item {
                    AIChatBar(
                        onClick = { navHostController.navigate("aiChat") },
                        baseViewModel = homeBaseViewModel
                    )
                }
                items(chatData){
                    chat ->
                    ChatDesign(chatlistModel = chat, onClick = {
                        navHostController.navigate(
                            Routes.ChatScreen.createRoute(
                                chat.name ?: "Unknown",
                                chat.phoneNumber ?: ""
                            )
                        )
                    }, baseViewModel = homeBaseViewModel)
                }
            }
        }
    }
}
@Composable
fun AddUserPopup(
    onDismiss: () -> Unit,
    onUserAdd: (chatlistModel) -> Unit,
    baseViewModel: BaseViewModel
){
    var phoneNumber by remember {
        mutableStateOf("")
    }
    var isSearching by remember {
        mutableStateOf(false)
    }

    var userFound by remember {
        mutableStateOf<chatlistModel?>(null)
    }



    Column (modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)){
        TextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Enter Phone Number") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                cursorColor = colorResource(id = R.color.light_green)
            )
        )
        Row {
            Button(onClick = {
                if (phoneNumber.isNotBlank()) {
                    baseViewModel.serachUserByPhoneNumber(phoneNumber) { user ->
                        user?.let {
                            // Create new chat with current timestamp
                            val newChat = chatlistModel(
                                name = it.name,
                                phoneNumber = it.phoneNumber,
                                time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()),
                                message = "Chat started"
                            )
                            onUserAdd(newChat)
                            onDismiss()
                        } ?: run {
                            // Show error message
                        }
                    }
                }
            }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(colorResource(R.color.light_green))){
                Text(text = "Search")

            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { onDismiss },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(colorResource(R.color.light_green))
            ) {
                Text(text = "Cancel")
            }
        }
        if (isSearching){
            Text(text = "Searching...", color = Color.Gray)

        }
        userFound?.let {
            Column {
                Text(text = "User Found ${it.name}")
                Button(onClick = {
                    onUserAdd(it)
                    onDismiss()
                }, colors = ButtonDefaults.buttonColors(colorResource(R.color.light_green))) {
                    Text("Add to Chat")
                }

            }

        }?:run{
            if (!isSearching){
                Text(text = "No User Found", color = Color.Gray)
            }
    }

    }


}
