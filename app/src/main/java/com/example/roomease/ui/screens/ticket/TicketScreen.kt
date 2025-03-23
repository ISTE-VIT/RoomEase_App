package com.example.roomease.ui.screens.ticket

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.roomease.domain.model.ACDetails
import com.example.roomease.domain.model.CleaningDetails
import com.example.roomease.domain.model.ElectricalDetails
import com.example.roomease.domain.model.PlumbingDetails
import com.example.roomease.domain.model.Ticket
import com.example.roomease.domain.model.TicketDetails
import com.example.roomease.domain.model.TicketStatus
import com.example.roomease.network.api.closeTicketOnBackend
import com.example.roomease.ui.viewmodel.TicketViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketScreen(
    userId: String,
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
                TicketItem(
                    ticket = ticket,
                    onClose = {
                        if (ticket.status != TicketStatus.COMPLETED) {
                            viewModel.closeTicket(ticket.id.toString(), ticket.userId)
                        }
                    }
                )
            }
        }
    }
}

/**
 * Helper function to format a Long epoch-millis into a string.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun formatEpochMillis(epochMillis: LocalDateTime): String {
    // Use remember so we don't reformat every recomposition unnecessarily.
    return remember(epochMillis) {
        val instant = epochMillis.atZone(ZoneId.systemDefault()).toInstant()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        formatter.format(instant.atZone(ZoneId.systemDefault()))
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TicketItem(
    ticket: Ticket,
    onClose: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val viewModel: TicketViewModel = getViewModel()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Category as a small "header"
            Text(
                text = "Category: ${ticket.category}",
                style = MaterialTheme.typography.titleMedium ,
                fontWeight = FontWeight.Bold, // Makes the text bold
                color = MaterialTheme.colorScheme.primary // Sets the text color
            )

            // Sub-header row (status, created, etc.)
            Text(
                text = "Status: ${ticket.status}",
                style = MaterialTheme.typography.bodyMedium,
                color = if (ticket.status == TicketStatus.COMPLETED) Color(0xFF86BD90) else Color(0xFFE97B9F),
                modifier = Modifier.padding(top = 4.dp)
            )

            // Format createdAt
            Text(
                text = "Created At: ${formatEpochMillis(ticket.createdAt)}",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Light, // Makes the text bold
                modifier = Modifier.padding(top = 2.dp)
            )

            // Format completedAt if present
            ticket.completedAt?.let {
                Text(
                    text = "Completed At: ${formatEpochMillis(it)}",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Light, // Makes the text bold
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Show details depending on the subclass
            when (val details: TicketDetails = ticket.details) {
                is CleaningDetails -> {
                    Text(
                        text = "Time Slot: ${details.timeSlot.displayName}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                is ElectricalDetails -> {
                    Text(
                        text = "Time Slot: ${details.timeSlot.displayName}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    details.electricalIssueType?.takeIf { it.isNotBlank() }?.let { issue ->
                        Text(
                            text = "Issue: $issue",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    details.additionalDescription?.takeIf { it.isNotBlank() }?.let { desc ->
                        Text(
                            text = "Additional: $desc",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                is PlumbingDetails -> {
                    details.plumbingIssue?.takeIf { it.isNotBlank() }?.let { issue ->
                        Text(
                            text = "Plumbing Issue: $issue",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    details.additionalDescription?.takeIf { it.isNotBlank() }?.let { desc ->
                        Text(
                            text = "Additional: $desc",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                is ACDetails -> {
                    Text(
                        text = "Time Slot: ${details.timeSlot.displayName}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "AC Description: ${details.acDescription}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Close ticket button if not completed
            if (ticket.status != TicketStatus.COMPLETED) {
                Button(onClick = {
                    scope.launch {
/*
                        val closed = closeTicketOnBackend(ticket.userId, ticket.category.toString())
                        if (closed) {
                            viewModel.closeTicket(ticket.id.toString(), ticket.userId)
                            onClose()
                        } else {
                            Toast.makeText(context, "Failed to close ticket", Toast.LENGTH_SHORT).show()
                        }
*/
                        viewModel.closeTicket(ticket.id.toString(), ticket.userId)
                        onClose()
                    }
                }) {
                    Text("Close Ticket")
                }
            } else {
                Text(
                    text = "Ticket Closed",
                    fontWeight = FontWeight.Bold, // Makes the text bold
                    color = MaterialTheme.colorScheme.primary, // Sets the text color
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}