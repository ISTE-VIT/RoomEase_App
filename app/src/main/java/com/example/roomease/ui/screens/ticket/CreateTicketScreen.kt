package com.example.roomease.ui.screens.ticket

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.roomease.domain.model.Ticket
import com.example.roomease.domain.model.TicketCategory
import com.example.roomease.domain.model.TimeSlot
import com.example.roomease.ui.viewmodel.TicketViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTicketScreen(
    ticketType: String,
    onTicketCreated: () -> Unit,
    onCancel: () -> Unit,
) {
    val viewModel: TicketViewModel = getViewModel()

    // "category" is fixed based on the ticket type selected from Home.
    val category by remember { mutableStateOf(ticketType) }

    // Time slot drop-down variables.
    val timeSlotOptions = TimeSlot.entries.map { it.displayName }
    var timeSlot by remember { mutableStateOf("") }
    val timeSlotExpanded = remember { mutableStateOf(false) }

    var electricalIssue by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Create Ticket", style = MaterialTheme.typography.titleLarge) }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding)
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = category,
                    onValueChange = { /* Category is fixed */ },
                    label = { Text("Category") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false // read-only
                )

                // Time Slot drop-down menu.
                ExposedDropdownMenuBox(
                    expanded = timeSlotExpanded.value,
                    onExpandedChange = { timeSlotExpanded.value = !timeSlotExpanded.value }
                ) {
                    OutlinedTextField(
                        value = timeSlot,
                        onValueChange = { /* no-op, selection happens via dropdown */ },
                        readOnly = true,
                        label = { Text("Time Slot") },
                        placeholder = { if (timeSlot.isEmpty()) Text("Select Time Slot") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = timeSlotExpanded.value)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = timeSlotExpanded.value,
                        onDismissRequest = { timeSlotExpanded.value = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        timeSlotOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    timeSlot = option
                                    timeSlotExpanded.value = false
                                }
                            )
                        }
                    }
                }

                if (category.equals("Electrical", ignoreCase = true)) {
                    OutlinedTextField(
                        value = electricalIssue,
                        onValueChange = { electricalIssue = it },
                        label = { Text("Electrical Issue") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = {
                            val userId = Firebase.auth.currentUser?.uid.orEmpty()
                            // Convert the selected time slot into a TimeSlot enum
                            val selectedTimeSlot: TimeSlot = TimeSlot.fromDisplayName(timeSlot) ?: TimeSlot.MORNING
                            val ticket = Ticket(
                                userId = userId,
                                category = TicketCategory.valueOf(category.uppercase()),
                                timeSlot = selectedTimeSlot,
                                electricalIssueType = if (category.equals("Electrical", ignoreCase = true)) electricalIssue else null
                            )
                            viewModel.createTicket(ticket)
                            onTicketCreated()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Create Ticket", style = MaterialTheme.typography.bodyLarge)
                    }
                    Button(
                        onClick = onCancel,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}