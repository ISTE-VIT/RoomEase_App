package com.example.roomease.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object Login

@Serializable
object Login2

@Serializable
object Home

@Serializable
object TicketList

@Serializable
data class CreateTicket(val userId: String)