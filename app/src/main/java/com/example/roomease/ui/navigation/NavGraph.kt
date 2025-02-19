package com.example.roomease.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.roomease.ui.screens.home.EditProfileScreen
import com.example.roomease.ui.screens.home.MainBottomNavScreen
import com.example.roomease.ui.screens.sign_in.Login2Screen
import com.example.roomease.ui.screens.sign_in.SignInScreen
import com.example.roomease.ui.screens.ticket.CreateTicketScreen
import com.example.roomease.ui.viewmodel.UserViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import org.koin.androidx.compose.getViewModel

@SuppressLint("RestrictedApi")
@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val userId = Firebase.auth.currentUser?.uid ?: ""
    val isUserLoggedIn = Firebase.auth.currentUser != null && !Firebase.auth.currentUser!!.isAnonymous

    // Obtain the UserViewModel (using Koin) and load the details.
    val userViewModel: UserViewModel = getViewModel()
    LaunchedEffect(userId) {
        if (userId.isNotEmpty()) {
            userViewModel.loadUserDetails(userId)
        }
    }
    val isUserDetailsProvider = userViewModel.userHostelDetails != null

    // Start destination:
    // • If not signed in => Login (SignInScreen)
    // • If signed in but no additional details saved => Login2 (Login2Screen)
    // • Otherwise, go to Home.
    val startDestination = when {
        !isUserLoggedIn -> Login
        isUserLoggedIn && !isUserDetailsProvider -> Login2
        else -> Home
    }

    NavHost(navController, startDestination = startDestination) {
        composable<Login> {
            SignInScreen(onLoginSuccess = {
                // Instead of navigating to Home, navigate to Login2 to collect extra details.
                navController.navigate(Login2) {
                    popUpTo(Login::class) { inclusive = true }
                }
            })
        }

        composable<Login2> {
            Login2Screen(
                onDetailsSubmitted = {
                    navController.navigate(Home) {
                        popUpTo(Login2::class) { inclusive = true }
                    }
                },
                onSignOut = {
                    navController.navigate(Login) {
                        popUpTo(Login2::class) { inclusive = true }
                    }
                }
            )
        }

        composable<Home> {
            MainBottomNavScreen(parentNavController = navController)
        }

        composable("new_ticket/{ticketType}") { backStackEntry ->
            val ticketType = backStackEntry.arguments?.getString("ticketType") ?: "UNKNOWN"
            CreateTicketScreen(
                ticketType = ticketType,
                onTicketCreated = {
                    navController.navigate(Home) {
                        popUpTo(Home::class) { inclusive = true }
                    }
                },
                onCancel = {
                    navController.popBackStack()
                }
            )
        }

        composable<EditProfile> {
            EditProfileScreen(
                onProfileUpdated = { navController.popBackStack() },
                onCancel = { navController.popBackStack() }
            )
        }
    }
}