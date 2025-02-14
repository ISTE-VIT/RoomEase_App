package com.example.roomease.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.roomease.ui.screens.ticket.CreateTicketScreen
import com.example.roomease.ui.screens.ticket.TicketScreen

@SuppressLint("RestrictedApi")
@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val userId = "user123" // Replace with actual user ID logic

    NavHost(navController, startDestination = TicketList) {
        composable<TicketList> { backStackEntry ->
            TicketScreen(
                userId = userId,
                onNavigateToCreateTicket = {
                    navController.navigate(CreateTicket(userId = userId))
                }
            )
        }
        composable<CreateTicket> { backStackEntry ->
            val createTicketArgs: CreateTicket = backStackEntry.toRoute()
            CreateTicketScreen(
                onTicketCreated = {
                    // Navigate back to the ticket list after ticket creation
                    navController.navigate(TicketList) {
                        popUpTo(TicketList::class) { inclusive = true }
                    }
                },
                onCancel = {
                    navController.popBackStack()
                }
            )
        }
    }
}