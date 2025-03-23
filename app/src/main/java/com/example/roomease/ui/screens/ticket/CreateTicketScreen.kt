package com.example.roomease.ui.screens.ticket

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.roomease.domain.model.ACDetails
import com.example.roomease.domain.model.CleaningDetails
import com.example.roomease.domain.model.ElectricalDetails
import com.example.roomease.domain.model.PlumbingDetails
import com.example.roomease.domain.model.Ticket
import com.example.roomease.domain.model.TicketCategory
import com.example.roomease.domain.model.TicketDetails
import com.example.roomease.domain.model.TicketStatus
import com.example.roomease.domain.model.TimeSlot
import com.example.roomease.network.api.sendTicketToBackend
import com.example.roomease.ui.viewmodel.TicketViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTicketScreen(
    ticketType: String,
    onTicketCreated: () -> Unit,
    onCancel: () -> Unit
) {
    val viewModel: TicketViewModel = getViewModel()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // The ticket category is fixed based on the selection from Home.
    val category by remember { mutableStateOf(ticketType.uppercase()) }

    // Determine which UI to show.
    val isCleaning = category == "CLEANING"
    val isElectrical = category == "ELECTRICAL"
    val isPlumbing = category == "PLUMBING"
    val isAC = category == "AC"

    // Common timeslot options (used by CLEANING, ELECTRICAL, AC)
    val timeSlotOptions = TimeSlot.entries.map { it.displayName }
    var timeSlot by remember { mutableStateOf("") }
    val timeSlotExpanded = remember { mutableStateOf(false) }

    // Electrical-specific
    val electricalOptions = listOf("Light", "Fan", "Switch", "Wiring", "Socket")
    var electricalIssue by remember { mutableStateOf("") }
    val electricalExpanded = remember { mutableStateOf(false) }
    var electricalDescription by remember { mutableStateOf("") }

    // Plumbing-specific
    val plumbingOptions = listOf("Water Cooler", "Water Filter", "Tap", "Geyser", "Washrooms", "Toilets")
    var plumbingIssue by remember { mutableStateOf("") }
    val plumbingExpanded = remember { mutableStateOf(false) }
    var plumbingDescription by remember { mutableStateOf("") }

    // AC-specific
    var acDescription by remember { mutableStateOf("") }

    // If "other" categories exist, they'd have their own states or fallback logic
    // Just for demonstration, we'll have a 'description' fallback
    var generalDescription by remember { mutableStateOf("") }

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

                // Show relevant input UI based on category
                when {
                    isCleaning -> {
                        // TimeSlot drop-down for cleaning
                        ExposedDropdownMenuBox(
                            expanded = timeSlotExpanded.value,
                            onExpandedChange = { timeSlotExpanded.value = !timeSlotExpanded.value }
                        ) {
                            OutlinedTextField(
                                value = timeSlot,
                                onValueChange = { /* no-op */ },
                                readOnly = true,
                                label = { Text("Time Slot") },
                                placeholder = {
                                    if (timeSlot.isEmpty()) Text("Select Time Slot")
                                },
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
                    }

                    isElectrical -> {
                        // TimeSlot drop-down for electrical
                        ExposedDropdownMenuBox(
                            expanded = timeSlotExpanded.value,
                            onExpandedChange = { timeSlotExpanded.value = !timeSlotExpanded.value }
                        ) {
                            OutlinedTextField(
                                value = timeSlot,
                                onValueChange = { /* no-op */ },
                                readOnly = true,
                                label = { Text("Time Slot") },
                                placeholder = {
                                    if (timeSlot.isEmpty()) Text("Select Time Slot")
                                },
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

                        // Electrical issue drop-down
                        ExposedDropdownMenuBox(
                            expanded = electricalExpanded.value,
                            onExpandedChange = {
                                electricalExpanded.value = !electricalExpanded.value
                            }
                        ) {
                            OutlinedTextField(
                                value = electricalIssue,
                                onValueChange = { /* no-op */ },
                                readOnly = true,
                                label = { Text("Electrical Issue") },
                                placeholder = {
                                    if (electricalIssue.isEmpty()) Text("Select Issue")
                                },
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

                        // Additional description field
                        OutlinedTextField(
                            value = electricalDescription,
                            onValueChange = { electricalDescription = it },
                            label = { Text("Additional Description (optional)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    isPlumbing -> {
                        // Plumbing issue drop-down
                        ExposedDropdownMenuBox(
                            expanded = plumbingExpanded.value,
                            onExpandedChange = {
                                plumbingExpanded.value = !plumbingExpanded.value
                            }
                        ) {
                            OutlinedTextField(
                                value = plumbingIssue,
                                onValueChange = { /* no-op */ },
                                readOnly = true,
                                label = { Text("Plumbing Issue") },
                                placeholder = {
                                    if (plumbingIssue.isEmpty()) Text("Select Issue")
                                },
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

                        // Additional description field
                        OutlinedTextField(
                            value = plumbingDescription,
                            onValueChange = { plumbingDescription = it },
                            label = { Text("Additional Description") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    isAC -> {
                        // AC also has a time slot plus description
                        ExposedDropdownMenuBox(
                            expanded = timeSlotExpanded.value,
                            onExpandedChange = { timeSlotExpanded.value = !timeSlotExpanded.value }
                        ) {
                            OutlinedTextField(
                                value = timeSlot,
                                onValueChange = { /* no-op */ },
                                readOnly = true,
                                label = { Text("Time Slot") },
                                placeholder = {
                                    if (timeSlot.isEmpty()) Text("Select Time Slot")
                                },
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

                        OutlinedTextField(
                            value = acDescription,
                            onValueChange = { acDescription = it },
                            label = { Text("AC Description") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    else -> {
                        // Any other categories? Show a simple description
                        OutlinedTextField(
                            value = generalDescription,
                            onValueChange = { generalDescription = it },
                            label = { Text("Description") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = {
                            val userId = Firebase.auth.currentUser?.uid.orEmpty()

                            // Build the sealed details object based on category
                            val details: TicketDetails = when {
                                isCleaning -> {
                                    val slot = TimeSlot.fromDisplayName(timeSlot) ?: TimeSlot.MORNING
                                    CleaningDetails(timeSlot = slot)
                                }
                                isElectrical -> {
                                    val slot = TimeSlot.fromDisplayName(timeSlot) ?: TimeSlot.MORNING
                                    ElectricalDetails(
                                        timeSlot = slot,
                                        electricalIssueType = electricalIssue.takeIf { it.isNotBlank() },
                                        additionalDescription = electricalDescription.takeIf { it.isNotBlank() }
                                    )
                                }
                                isPlumbing -> {
                                    PlumbingDetails(
                                        plumbingIssue = plumbingIssue.takeIf { it.isNotBlank() },
                                        additionalDescription = plumbingDescription.takeIf { it.isNotBlank() }
                                    )
                                }
                                isAC -> {
                                    val slot = TimeSlot.fromDisplayName(timeSlot) ?: TimeSlot.MORNING
                                    ACDetails(
                                        timeSlot = slot,
                                        acDescription = acDescription
                                    )
                                }
                                else -> {
                                    // Fallback for unhandled categories
                                    // Could store a minimal details object or error out
                                    ACDetails(TimeSlot.MORNING, generalDescription)
                                }
                            }

                            val finalTicket = Ticket(
                                userId = userId,
                                status = TicketStatus.PENDING,
                                category = TicketCategory.valueOf(category),
                                details = details
                            )

                            // Launch a coroutine to send the ticket to the backend.
                            // Integration with backend:
                            scope.launch {
/*
                                val sent = sendTicketToBackend(finalTicket)
                                if (sent) {
                                    // Optionally, save locally in ViewModel
                                    viewModel.createTicket(finalTicket)
                                    onTicketCreated()
                                } else {
                                    Toast.makeText(
                                        // Use context to show a toast
                                        context,
                                        "Failed to create ticket",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
*/
                                viewModel.createTicket(finalTicket)
                                onTicketCreated()
                            }

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