package com.example.roomease.ui.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.roomease.R
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.union
import com.example.roomease.ui.screens.ticket.CreateTicketScreen

/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(name: String) {
    // State to show/hide the ticket creation bottom sheet and store the selected ticket type.
    val showTicketSheet = remember { mutableStateOf(false) }
    val selectedTicketType = remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "Good Morning, $name",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Raise New Ticket",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            // Icons Grid: Two rows with two icons each.
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TicketIcon(type = "Cleaning", imageId = R.drawable.clean, ) { type ->
                        selectedTicketType.value = type
                        showTicketSheet.value = true
                    }
                    TicketIcon(type = "Electrical", imageId = R.drawable.elec) { type ->
                        selectedTicketType.value = type
                        showTicketSheet.value = true
                    },

                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TicketIcon(type = "Plumbing", imageId = R.drawable.plum) { type ->
                        selectedTicketType.value = type
                        showTicketSheet.value = true
                    }
                    TicketIcon(type = "AC", imageId = R.drawable.ac) { type ->
                        selectedTicketType.value = type
                        showTicketSheet.value = true
                    }
                }
            }
        }
    }

    if (showTicketSheet.value) {
        ModalBottomSheet(
            onDismissRequest = { showTicketSheet.value = false },
            dragHandle = { /* Optionally add a drag handle */ },
            modifier = Modifier
                .fillMaxHeight()
                .windowInsetsPadding(WindowInsets.navigationBars.union(WindowInsets.ime))
        ) {
            CreateTicketScreen(
                ticketType = selectedTicketType.value,
                onTicketCreated = { showTicketSheet.value = false },
                onCancel = { showTicketSheet.value = false }
            )
        }
    }

}
*/


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(name: String) {
    // State to show/hide the ticket creation bottom sheet and store the selected ticket type.
    val showTicketSheet = remember { mutableStateOf(false) }
    val selectedTicketType = remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "Good Morning, $name",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Raise New Ticket",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            // Icons Grid: Two rows with two icons each.
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    TicketIcon(
                        type = "Cleaning",
                        imageId = R.drawable.clean,
                        onTicketClick = { type ->
                            selectedTicketType.value = type
                            showTicketSheet.value = true
                        },
                        modifier = Modifier.weight(1f)
                    )
                    TicketIcon(
                        type = "Electrical",
                        imageId = R.drawable.elec,
                        onTicketClick = { type ->
                            selectedTicketType.value = type
                            showTicketSheet.value = true
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    TicketIcon(
                        type = "Plumbing",
                        imageId = R.drawable.plum,
                        onTicketClick = { type ->
                            selectedTicketType.value = type
                            showTicketSheet.value = true
                        },
                        modifier = Modifier.weight(1f)
                    )
                    TicketIcon(
                        type = "AC",
                        imageId = R.drawable.ac,
                        onTicketClick = { type ->
                            selectedTicketType.value = type
                            showTicketSheet.value = true
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }

    if (showTicketSheet.value) {
        ModalBottomSheet(
            onDismissRequest = { showTicketSheet.value = false },
            dragHandle = { /* Optionally add a drag handle */ },
            modifier = Modifier
                .fillMaxHeight()
                .windowInsetsPadding(WindowInsets.navigationBars.union(WindowInsets.ime))
        ) {
            CreateTicketScreen(
                ticketType = selectedTicketType.value,
                onTicketCreated = { showTicketSheet.value = false },
                onCancel = { showTicketSheet.value = false }
            )
        }
    }
}