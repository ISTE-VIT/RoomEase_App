package com.example.roomease.ui.screens.ticket

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.roomease.domain.model.Ticket
import com.example.roomease.domain.model.TicketCategory
import com.example.roomease.domain.model.TicketStatus
import com.example.roomease.domain.model.TimeSlot
import com.example.roomease.ui.viewmodel.ticket.TicketViewModel
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTicketScreen(
    onTicketCreated: () -> Unit,
    onCancel: () -> Unit,
) {
    val viewModel: TicketViewModel = getViewModel()

    var category by remember { mutableStateOf(TicketCategory.CLEANING.name) }
    var contactNumber by remember { mutableStateOf("") }
    var hostelBlock by remember { mutableStateOf("") }
    var roomNumber by remember { mutableStateOf("") }
    var timeSlot by remember { mutableStateOf(TimeSlot.MORNING.name) }
    var electricalIssue by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Create Ticket") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Category") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = contactNumber,
                onValueChange = { contactNumber = it },
                label = { Text("Contact Number") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = hostelBlock,
                onValueChange = { hostelBlock = it },
                label = { Text("Hostel Block") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = roomNumber,
                onValueChange = { roomNumber = it },
                label = { Text("Room Number") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = timeSlot,
                onValueChange = { timeSlot = it },
                label = { Text("Time Slot") },
                modifier = Modifier.fillMaxWidth()
            )
            if (category == "Electrical") {
                OutlinedTextField(
                    value = electricalIssue,
                    onValueChange = { electricalIssue = it },
                    label = { Text("Electrical Issue") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                Button(
                    onClick = {
                        val ticket = Ticket(
                            id = "",
                            userId = "user123",
                            category = TicketCategory.valueOf(category),
                            contactNumber = contactNumber,
                            hostelBlock = hostelBlock,
                            roomNumber = roomNumber,
                            timeSlot = TimeSlot.valueOf(timeSlot),
                            electricalIssueType = if (category == TicketCategory.ELECTRICAL.name) electricalIssue else null,
                            status = TicketStatus.PENDING,
                            createdAt = System.currentTimeMillis(),
                            completedAt = null
                        )
                        viewModel.createTicket(ticket)
                        onTicketCreated()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Create Ticket")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel")
                }
            }
        }
    }
}