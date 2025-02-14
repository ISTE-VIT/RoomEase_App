package com.example.roomease.data.repository

import com.example.roomease.domain.model.Ticket
import com.example.roomease.domain.model.TicketStatus
import com.example.roomease.domain.repository.TicketRepository

class TicketRepositoryImpl : TicketRepository {
    // Simulated in-memory store for demonstration
    private val tickets = mutableListOf<Ticket>()

    override suspend fun createTicket(ticket: Ticket): Result<Unit> {
        tickets.add(ticket)
        return Result.success(Unit)
    }

    override suspend fun updateTicket(ticketId: String, newStatus: TicketStatus): Result<Unit> {
        val index = tickets.indexOfFirst { it.id ==  ticketId }
        return if (index != -1) {
            val oldTicket = tickets[index]
            val updatedTicket = oldTicket.copy(
                status = newStatus,
                completedAt = if (newStatus == TicketStatus.COMPLETED) System.currentTimeMillis() else null
            )
            tickets[index] = updatedTicket
            Result.success(Unit)
        } else {
            Result.failure(Exception("Ticket not found"))
        }
    }

    override suspend fun getTicketForUser(userId: String): Result<List<Ticket>> {
        // For demonstration, returns all tickets.
        // In a real implementation you might filter based on a user field.
        val userTickets = tickets.filter { it.userId == userId }
        return Result.success(userTickets)
    }
}