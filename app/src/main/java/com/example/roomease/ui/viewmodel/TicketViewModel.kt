package com.example.roomease.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roomease.domain.model.Ticket
import com.example.roomease.domain.model.TicketStatus
import com.example.roomease.domain.usecase.CreateTicketUseCase
import com.example.roomease.domain.usecase.GetTicketsForUserUseCase
import com.example.roomease.domain.usecase.UpdateTicketStatusUseCase
import kotlinx.coroutines.launch

class TicketViewModel(
    private val createTicketUseCase: CreateTicketUseCase,
    private val updateTicketStatusUseCase: UpdateTicketStatusUseCase,
    private val getTicketsForUserUseCase: GetTicketsForUserUseCase
) : ViewModel() {

    private val _tickets = MutableLiveData<List<Ticket>>()
    val tickets: LiveData<List<Ticket>> = _tickets

    private val _operationResult = MutableLiveData<Result<Unit>>()
    val operationResult: LiveData<Result<Unit>> = _operationResult

    fun fetchTickets(userId: String) {
        viewModelScope.launch {
            val result = getTicketsForUserUseCase(userId)
            _tickets.value = result.getOrDefault(emptyList())
        }
    }

    fun createTicket(ticket: Ticket) {
        viewModelScope.launch {
            val result = createTicketUseCase(ticket)
            _operationResult.value = result
            // Refresh ticket list after creation
            fetchTickets(ticket.userId)
        }
    }

    fun closeTicket(ticketId: String, userId: String) {
        viewModelScope.launch {
            val result = updateTicketStatusUseCase(ticketId, TicketStatus.COMPLETED)
            _operationResult.value = result
            // Refresh list after updating
            fetchTickets(userId)
        }
    }
}