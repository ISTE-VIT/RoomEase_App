package com.example.roomease.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object TicketList

@Serializable
data class CreateTicket(val userId: String)
