package com.example.roomease.domain.repository

import com.example.roomease.domain.model.Ticket
import com.example.roomease.domain.model.TicketStatus

interface TicketRepository {
    suspend fun createTicket(ticket: Ticket): Result<Unit>
    suspend fun updateTicket(ticketId: String, newStatus: TicketStatus): Result<Unit>
    suspend fun getTicketForUser(userId: String): Result<List<Ticket>>
}