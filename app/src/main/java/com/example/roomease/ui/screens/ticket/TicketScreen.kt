package com.example.roomease.ui.screens.ticket

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.roomease.domain.model.Ticket
import com.example.roomease.domain.model.TicketStatus
import com.example.roomease.ui.viewmodel.ticket.TicketViewModel
import org.koin.androidx.compose.getViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketScreen(userId: String,
                 onNavigateToCreateTicket: () -> Unit
) {
    val viewModel: TicketViewModel = getViewModel()

    // Load tickets when the screen is displayed
    LaunchedEffect(userId) {
        viewModel.fetchTickets(userId)
    }
    val tickets by viewModel.tickets.observeAsState(emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Tickets") },
                actions = {
                    IconButton(onClick = onNavigateToCreateTicket) {
                        Icon(Icons.Default.Add, contentDescription = "Create Ticket")
                    }
                }
                )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            items(tickets) { ticket ->
                TicketItem(ticket = ticket, onClose = {
                    if (ticket.status != TicketStatus.COMPLETED) {
                        viewModel.closeTicket(ticket.id, ticket.userId)
                    }
                })

            }
        }
    }

}

@Composable
fun TicketItem(ticket: Ticket, onClose: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical =8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Category: ${ticket.category}")
            Text("Time Slot: ${ticket.timeSlot.displayName}")
            if (ticket.category.name == "ELECTRICAL" && ticket.electricalIssueType != null) {
                Text("Issue: ${ticket.electricalIssueType}")
            }
            Text("Status: ${ticket.status}")
            Spacer(modifier = Modifier.height(8.dp))
            if (ticket.status != TicketStatus.COMPLETED) {
                Button(onClick = onClose) {
                    Text("Close Ticket")
                }
            } else {
                Text("Ticket Closed", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}