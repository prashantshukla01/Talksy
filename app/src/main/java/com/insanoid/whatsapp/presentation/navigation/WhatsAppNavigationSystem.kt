package com.insanoid.whatsapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.insanoid.whatsapp.presentation.Callscreen.CallScreen
import com.insanoid.whatsapp.presentation.communities.Communities
import com.insanoid.whatsapp.presentation.communities.communitiesScreen
import com.insanoid.whatsapp.presentation.homescreen.HomeScreen
import com.insanoid.whatsapp.presentation.profile.UserProfileSetScreen
import com.insanoid.whatsapp.presentation.splashscreen.SplashScreen
import com.insanoid.whatsapp.presentation.updatescreen.UpdateScreen
import com.insanoid.whatsapp.presentation.userregistrationscreen.UserRegistrationScreen
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
            HomeScreen()
        }
        composable<Routes.UpdateScreen> {
            UpdateScreen()
        }
        composable<Routes.CommunitiesScreen> {
            communitiesScreen()
        }
        composable<Routes.CallScreen> {
            CallScreen()
        }
        composable<Routes.UserProfileSetScreen> {
            UserProfileSetScreen(navHostController = navController)
        }
    }
}
