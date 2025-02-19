package com.example.roomease.ui.screens.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.roomease.ui.navigation.EditProfile
import com.example.roomease.ui.navigation.Login
import com.example.roomease.ui.screens.ticket.TicketScreen
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    data object Home : BottomNavItem("bottom_home", "Home", Icons.Filled.Home)
    data object Tickets : BottomNavItem("bottom_tickets", "Tickets", Icons.AutoMirrored.Filled.List)
    data object Profile : BottomNavItem("bottom_profile", "Profile", Icons.Filled.Person)
    data object EditProfile : BottomNavItem("edit_profile", "Edit Profile", Icons.Filled.Person)
}

@Composable
fun MainBottomNavScreen(parentNavController: NavHostController) {
    // This nested NavController handles bottom navigation among the main sections.
    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                listOf(
                    BottomNavItem.Home,
                    BottomNavItem.Tickets,
                    BottomNavItem.Profile
                ).forEach { item ->
                    NavigationBarItem(
                        selected = currentRoute == item.route,
                        onClick = {
                            bottomNavController.navigate(item.route) {
                                popUpTo(bottomNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                        label = { Text(item.title) }
                    )
                }
            }
        }
    ) { innerPadding ->
        // Nested NavHost to handle bottom nav screens.
        NavHost(
            navController = bottomNavController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Home.route) {
                // HomePage takes the parent navController for any navigation actions.
                HomePage(name = "Ashwani")
            }
            composable(BottomNavItem.Tickets.route) {
                // TicketScreen shows current and historical tickets.
                TicketScreen(
                    userId = Firebase.auth.currentUser?.uid ?: "",
                    onNavigateToCreateTicket = {
                        // Navigate using the parent's navController to display the full-screen create ticket page.
                        parentNavController.navigate("new_ticket/ELECTRICAL")
                    }
                )
            }
            composable(BottomNavItem.Profile.route) {
                ProfileScreen(
                    onLogout = {
                        // On logout, navigate back to the login screen.
                        parentNavController.navigate(Login) {
                            popUpTo("bottom_home") { inclusive = true }
                        }
                    },
                    onEditProfile = {
                        parentNavController.navigate(EditProfile)
                    }
                )
            }
        }
    }
}