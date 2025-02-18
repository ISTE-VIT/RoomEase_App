package com.example.roomease.ui.screens.home

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
import com.example.roomease.domain.model.HostelType
import com.example.roomease.domain.model.UserHostelDetails
import com.example.roomease.ui.viewmodel.UserViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onProfileUpdated: () -> Unit,
    onCancel: () -> Unit,
) {
    val userViewModel: UserViewModel = getViewModel()
    // Retrieve current user details.
    val currentDetails = userViewModel.userHostelDetails

    // Initialize state with current details or default values.
    var phone by remember { mutableStateOf(currentDetails?.phoneNumber ?: "") }
    var hostelType by remember { mutableStateOf(currentDetails?.hostelType?.let { if(it == HostelType.MENS) "Mens" else "Ladies" } ?: "Mens") }
    var hostelBlock by remember { mutableStateOf(currentDetails?.hostelBlock ?: "") }
    var roomNumber by remember { mutableStateOf(currentDetails?.roomNumber ?: "") }

    // Define drop-down options.
    val hostelTypeOptions = listOf("Mens", "Ladies")
    val mensHostelBlocks = listOf("A", "B", "C", "D", "E", "F", "G", "H", "K", "L", "M", "N", "P", "Q", "R", "S", "T")
    val ladiesHostelBlocks = listOf("A", "B", "C", "D", "E", "F", "G", "H", "I")
    val hostelBlockOptions = if (hostelType == "Mens") mensHostelBlocks else ladiesHostelBlocks

    // States to control dropdown expansion.
    var hostelTypeExpanded by remember { mutableStateOf(false) }
    var hostelBlockExpanded by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Edit Profile", style = MaterialTheme.typography.titleLarge) })
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
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone Number") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Hostel Type Dropdown
                ExposedDropdownMenuBox(
                    expanded = hostelTypeExpanded,
                    onExpandedChange = { hostelTypeExpanded = !hostelTypeExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = hostelType,
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Hostel Type") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = hostelTypeExpanded)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = hostelTypeExpanded,
                        onDismissRequest = { hostelTypeExpanded = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        hostelTypeOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    hostelType = option
                                    hostelTypeExpanded = false
                                    // Reset hostel block when hostel type changes.
                                    hostelBlock = ""
                                }
                            )
                        }
                    }
                }

                // Hostel Block Dropdown
                ExposedDropdownMenuBox(
                    expanded = hostelBlockExpanded,
                    onExpandedChange = { hostelBlockExpanded = !hostelBlockExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = hostelBlock,
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Hostel Block") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = hostelBlockExpanded)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = hostelBlockExpanded,
                        onDismissRequest = { hostelBlockExpanded = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        hostelBlockOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    hostelBlock = option
                                    hostelBlockExpanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = roomNumber,
                    onValueChange = { roomNumber = it },
                    label = { Text("Room Number") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = {
                            val userId = Firebase.auth.currentUser?.uid.orEmpty()
                            val hostelTypeEnum = if (hostelType == "Mens") HostelType.MENS else HostelType.LADIES
                            val updatedDetails = UserHostelDetails(
                                userId = userId,
                                phoneNumber = phone,
                                hostelType = hostelTypeEnum,
                                hostelBlock = hostelBlock,
                                roomNumber = roomNumber
                            )
                            userViewModel.updateUserDetails(updatedDetails) {
                                onProfileUpdated()
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Update Profile", style = MaterialTheme.typography.bodyLarge)
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
