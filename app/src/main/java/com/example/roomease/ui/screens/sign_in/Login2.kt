package com.example.roomease.ui.screens.sign_in

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.roomease.R
import com.example.roomease.domain.model.UserHostelDetails
import com.example.roomease.ui.viewmodel.UserViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.koin.androidx.compose.getViewModel

@Composable
fun Login2Screen(
    navController: NavController,
    onDetailsSubmitted: () -> Unit,
    onSignOut: () -> Unit
) {
    val phoneState = remember { mutableStateOf("") }
    val hostelBlockState = remember { mutableStateOf("") }
    val roomNumberState = remember { mutableStateOf("") }
    val userViewModel: UserViewModel = getViewModel()

    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize()
    ) {
        Text(
            text = "RoomEase",
            style = MaterialTheme.typography.headlineLarge
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
        OutlinedTextField(
            value = hostelBlockState.value,
            onValueChange = { hostelBlockState.value = it },
            label = { Text("Hostel-block") },
            placeholder = { Text("Enter your hostel-block") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = roomNumberState.value,
            onValueChange = { roomNumberState.value = it },
            label = { Text("Room-number") },
            placeholder = { Text("Enter your room-number") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val userId = Firebase.auth.currentUser?.uid.orEmpty()
                val details = UserHostelDetails(
                    userId = userId,
                    phoneNumber = phoneState.value,
                    hostelBlock = hostelBlockState.value,
                    roomNumber = roomNumberState.value
                )
                // Save the details via the ViewModel.
                userViewModel.saveUserDetails(details) {
                    onDetailsSubmitted()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            colors = ButtonDefaults.buttonColors(contentColor = Color(0xFF141218))
        ) {
            Text(text = "Submit", style = MaterialTheme.typography.titleMedium)
        }

        Button(
            onClick = onSignOut,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(contentColor = Color(0xFF141218))
        ) {
            Text(text = "Sign Out", style = MaterialTheme.typography.titleMedium)
        }
    }
}