package com.example.roomease.domain.usecase

import com.example.roomease.domain.model.Ticket
import com.example.roomease.domain.model.TicketStatus
import com.example.roomease.domain.repository.TicketRepository

class CreateTicketUseCase(private val repository: TicketRepository) {
    suspend operator fun invoke(ticket: Ticket): Result<Unit> =
        repository.createTicket(ticket)
}

class UpdateTicketStatusUseCase(private val repository: TicketRepository) {
    suspend operator fun invoke(ticketId: String, newStatus: TicketStatus): Result<Unit> =
        repository.updateTicket(ticketId, newStatus)
}

class GetTicketsForUserUseCase(private val repository: TicketRepository) {
    suspend operator fun invoke(userId: String): Result<List<Ticket>> =
        repository.getTicketForUser(userId)
}
