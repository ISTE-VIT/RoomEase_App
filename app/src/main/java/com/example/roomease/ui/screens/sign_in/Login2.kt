package com.example.roomease.ui.screens.sign_in

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.roomease.R
import com.example.roomease.domain.model.HostelType
import com.example.roomease.domain.model.UserHostelDetails
import com.example.roomease.network.api.sendHostelDetailsToBackend
import com.example.roomease.ui.viewmodel.UserViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login2Screen(
    onDetailsSubmitted: () -> Unit,
    onSignOut: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    // States for phone and room number.
    val phoneState = remember { mutableStateOf("") }
    val roomNumberState = remember { mutableStateOf("") }

    // Options for hostel type and hostel blocks.
    val hostelTypeOptions = listOf("Mens", "Ladies")
    val mensHostelBlocks = listOf("A", "B", "C", "D", "E", "F", "G", "H", "K", "L", "M", "N", "P", "Q", "R", "S", "T")
    val ladiesHostelBlocks = listOf("A", "B", "C", "D", "E", "F", "G", "H", "J")

    // States for dropdown selections.
    val hostelTypeState = remember { mutableStateOf("Mens") }
    val hostelBlockState = remember { mutableStateOf("") }

    // States for dropdown expansion.
    val hostelTypeExpanded = remember { mutableStateOf(false) }
    val hostelBlockExpanded = remember { mutableStateOf(false) }

    // Options for hostel blocks based on the selected hostel type.
    val hostelBlockOptions = if (hostelTypeState.value == "Mens") mensHostelBlocks else ladiesHostelBlocks

    val userViewModel: UserViewModel = getViewModel()

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(), // optional: to account for navigation bar insets
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(20.dp)
                .fillMaxSize()
        ) {
            Text(
                text = "RoomEase",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            OutlinedTextField(
                value = phoneState.value,
                onValueChange = { phoneState.value = it },
                label = { Text("Phone-number") },
                placeholder = { Text("Enter your phone-number") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )

            // Hostel Type Dropdown using ExposedDropdownMenuBox.
            ExposedDropdownMenuBox(
                expanded = hostelTypeExpanded.value,
                onExpandedChange = { hostelTypeExpanded.value = !hostelTypeExpanded.value }
            ) {
                OutlinedTextField(
                    value = hostelTypeState.value,
                    onValueChange = { /* no-op */ },
                    label = { Text("Hostel Type") },
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = hostelTypeExpanded.value)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = hostelTypeExpanded.value,
                    onDismissRequest = { hostelTypeExpanded.value = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    hostelTypeOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                hostelTypeState.value = option
                                hostelTypeExpanded.value = false
                                // Reset the hostel block when type changes.
                                hostelBlockState.value = ""
                            }
                        )
                    }
                }
            }

            // Hostel Block Dropdown using ExposedDropdownMenuBox.
            ExposedDropdownMenuBox(
                expanded = hostelBlockExpanded.value,
                onExpandedChange = { hostelBlockExpanded.value = !hostelBlockExpanded.value }
            ) {
                OutlinedTextField(
                    value = hostelBlockState.value,
                    onValueChange = { /* no-op */ },
                    label = { Text("Hostel Block") },
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = hostelBlockExpanded.value)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = hostelBlockExpanded.value,
                    onDismissRequest = { hostelBlockExpanded.value = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    hostelBlockOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                hostelBlockState.value = option
                                hostelBlockExpanded.value = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = roomNumberState.value,
                onValueChange = { roomNumberState.value = it },
                label = { Text("Room-number") },
                placeholder = { Text("Enter your room-number") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    val firebaseUid = Firebase.auth.currentUser?.uid.orEmpty()
                    val hostelTypeEnum = if (hostelTypeState.value == "Mens") HostelType.MENS else HostelType.LADIES
                    val details = UserHostelDetails(
                        userId = firebaseUid,
                        phoneNumber = phoneState.value,
                        roomNumber = roomNumberState.value,
                        hostelBlock = hostelBlockState.value,
                        hostelType = hostelTypeEnum
                    )
                    scope.launch {
                        val success = sendHostelDetailsToBackend(details)
                        if (success) {
                            userViewModel.saveUserDetails(details, onSuccess = {
                                onDetailsSubmitted()
                            })
                        } else {
                            // Handle failure
                            Toast.makeText(context, "Failed to send hostel details", Toast.LENGTH_SHORT).show()
                            onDetailsSubmitted()  // Added for testing purpose
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(text = "Submit", style = MaterialTheme.typography.titleMedium)
            }

            Button(
                onClick = onSignOut,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                )
            ) {
                Text(text = "Sign Out", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}