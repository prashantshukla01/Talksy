package com.insanoid.whatsapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.insanoid.whatsapp.chatScreen.ChatScreen
import com.insanoid.whatsapp.presentation.Callscreen.CallScreen
import com.insanoid.whatsapp.presentation.communities.communitiesScreen
import com.insanoid.whatsapp.presentation.homescreen.HomeScreen
import com.insanoid.whatsapp.presentation.profile.UserProfileSetScreen
import com.insanoid.whatsapp.presentation.splashscreen.SplashScreen
import com.insanoid.whatsapp.presentation.updatescreen.UpdateScreen
import com.insanoid.whatsapp.presentation.userregistrationscreen.UserRegistrationScreen
import com.insanoid.whatsapp.presentation.viewmodel.BaseViewModel
import com.insanoid.whatsapp.presentation.welcomescreen.WelcomeScreen

@Composable
fun WhatsAppNavigationSystem() {
    val navController = rememberNavController()
    NavHost(startDestination = Routes.SplashScreen, navController = navController) {
        composable<Routes.SplashScreen> {
            SplashScreen(navController)
        }
        composable<Routes.WelcomeScreen> {
            WelcomeScreen(navController)
        }
        composable<Routes.UserRegistrationScreen> {
            UserRegistrationScreen( navController)
        }
        composable<Routes.HomeScreen> {
            val baseViewModel: BaseViewModel = hiltViewModel()
            HomeScreen(navController,baseViewModel)
        }
        composable<Routes.UpdateScreen> {
            UpdateScreen(navController)
        }
        composable<Routes.CommunitiesScreen> {
            communitiesScreen(navController)
        }
        composable<Routes.CallScreen> {
            CallScreen(navController)
        }
        composable<Routes.UserProfileSetScreen> {
            UserProfileSetScreen(navHostController = navController)
        }
        composable(
            route = "chat_screen/{contactName}/{contactPhone}",
            arguments = listOf(
                navArgument("contactName") {
                    type = NavType.StringType
                },
                navArgument("contactPhone") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val contactName = backStackEntry.arguments?.getString("contactName") ?: ""
            val contactPhone = backStackEntry.arguments?.getString("contactPhone") ?: ""

            ChatScreen(
                contactName = contactName,
                contactPhone = contactPhone,
                navController = navController
            )
        }

        
    }
}

