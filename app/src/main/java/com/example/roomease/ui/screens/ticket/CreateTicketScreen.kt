package com.example.roomease.ui.screens.ticket

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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

    // The ticket category is fixed based on the selection from Home.
    val category by remember { mutableStateOf(ticketType) }

    // Determine which UI to show.
    val isCleaning = category.equals("Cleaning", ignoreCase = true)
    val isElectrical = category.equals("Electrical", ignoreCase = true)
    val isPlumbing = category.equals("Plumbing", ignoreCase = true)

    // For Cleaning tickets: TimeSlot drop-down.
    val timeSlotOptions = TimeSlot.entries.map { it.displayName }
    var timeSlot by remember { mutableStateOf("") }
    val timeSlotExpanded = remember { mutableStateOf(false) }

    // For Electrical tickets: Dropdown plus additional description.
    val electricalOptions = listOf("Light", "Fan", "Switch", "Wiring", "Socket")
    var electricalIssue by remember { mutableStateOf("") }
    val electricalExpanded = remember { mutableStateOf(false) }
    var electricalDescription by remember { mutableStateOf("") }

    // For Plumbing tickets: Dropdown plus additional description.
    val plumbingOptions = listOf("Water-Cooler", "Water-Filter", "Tap", "Geyser", "Washrooms", "Toilets")
    var plumbingIssue by remember { mutableStateOf("") }
    val plumbingExpanded = remember { mutableStateOf(false) }
    var plumbingDescription by remember { mutableStateOf("") }

    // For other tickets: Use a description field.
    var description by remember { mutableStateOf("") }

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
            // Wrap content with verticalScroll and imePadding to handle keyboard.
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .imePadding()
                    .padding(padding)
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Read-only Category field.
                OutlinedTextField(
                    value = category,
                    onValueChange = { /* fixed category */ },
                    label = { Text("Category") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false
                )

                if (isCleaning) {
                    // TimeSlot drop-down for cleaning tickets.
                    ExposedDropdownMenuBox(
                        expanded = timeSlotExpanded.value,
                        onExpandedChange = { timeSlotExpanded.value = !timeSlotExpanded.value }
                    ) {
                        OutlinedTextField(
                            value = timeSlot,
                            onValueChange = { /* no-op */ },
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
                } else if (isElectrical) {
                    // Electrical issue drop-down.
                    ExposedDropdownMenuBox(
                        expanded = electricalExpanded.value,
                        onExpandedChange = { electricalExpanded.value = !electricalExpanded.value }
                    ) {
                        OutlinedTextField(
                            value = electricalIssue,
                            onValueChange = { /* no-op */ },
                            readOnly = true,
                            label = { Text("Electrical Issue") },
                            placeholder = { if (electricalIssue.isEmpty()) Text("Select Issue") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = electricalExpanded.value)
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = electricalExpanded.value,
                            onDismissRequest = { electricalExpanded.value = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            electricalOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        electricalIssue = option
                                        electricalExpanded.value = false
                                    }
                                )
                            }
                        }
                    }
                    // Additional description field for electrical tickets.
                    OutlinedTextField(
                        value = electricalDescription,
                        onValueChange = { electricalDescription = it },
                        label = { Text("Additional Description (optional)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                } else if (isPlumbing) {
                    // Plumbing issue drop-down.
                    ExposedDropdownMenuBox(
                        expanded = plumbingExpanded.value,
                        onExpandedChange = { plumbingExpanded.value = !plumbingExpanded.value }
                    ) {
                        OutlinedTextField(
                            value = plumbingIssue,
                            onValueChange = { /* no-op */ },
                            readOnly = true,
                            label = { Text("Plumbing Issue") },
                            placeholder = { if (plumbingIssue.isEmpty()) Text("Select Issue") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = plumbingExpanded.value)
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = plumbingExpanded.value,
                            onDismissRequest = { plumbingExpanded.value = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            plumbingOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        plumbingIssue = option
                                        plumbingExpanded.value = false
                                    }
                                )
                            }
                        }
                    }
                    // Additional description field for plumbing tickets.
                    OutlinedTextField(
                        value = plumbingDescription,
                        onValueChange = { plumbingDescription = it },
                        label = { Text("Additional Description (optional)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    // For other ticket types, show a description field.
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") },
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
                            val finalTicket: Ticket = when {
                                isCleaning -> {
                                    // For cleaning, use the selected time slot.
                                    val selectedTimeSlot: TimeSlot = TimeSlot.fromDisplayName(timeSlot)
                                        ?: TimeSlot.MORNING
                                    Ticket(
                                        userId = userId,
                                        category = TicketCategory.valueOf(category.uppercase()),
                                        timeSlot = selectedTimeSlot,
                                        electricalIssueType = null
                                    )
                                }
                                isElectrical -> {
                                    // Combine electrical dropdown selection and additional description if provided.
                                    val combined = if (electricalDescription.isNotBlank())
                                        "$electricalIssue - $electricalDescription"
                                    else electricalIssue
                                    Ticket(
                                        userId = userId,
                                        category = TicketCategory.valueOf(category.uppercase()),
                                        timeSlot = TimeSlot.MORNING, // Default (not used)
                                        electricalIssueType = combined
                                    )
                                }
                                isPlumbing -> {
                                    val combined = if (plumbingDescription.isNotBlank())
                                        "$plumbingIssue - $plumbingDescription"
                                    else plumbingIssue
                                    Ticket(
                                        userId = userId,
                                        category = TicketCategory.valueOf(category.uppercase()),
                                        timeSlot = TimeSlot.MORNING, // Default (not used)
                                        electricalIssueType = combined
                                    )
                                }
                                else -> {
                                    Ticket(
                                        userId = userId,
                                        category = TicketCategory.valueOf(category.uppercase()),
                                        timeSlot = TimeSlot.MORNING, // Default
                                        electricalIssueType = description
                                    )
                                }
                            }
                            viewModel.createTicket(finalTicket)
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