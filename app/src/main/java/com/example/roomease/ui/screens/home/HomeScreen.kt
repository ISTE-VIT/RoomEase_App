package com.example.roomease.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.roomease.R

@Composable
fun HomePage(navController: NavController, name: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxSize()
    ) {
        Text(
            text = "Good Morning, $name",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(16.dp)
        )

        Text(
            text = "Raise New Ticket",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(16.dp)
        )

        Column {
            Row(modifier = Modifier.padding(2.dp)) {
                TicketIcon(type = "Cleaning", imageId = R.drawable.clean, navController = navController)
                TicketIcon(type = "Electrical", imageId = R.drawable.elec, navController = navController)
            }
            Row(modifier = Modifier.padding(2.dp)) {
                TicketIcon(type = "Plumbing", imageId = R.drawable.plum, navController = navController)
                TicketIcon(type = "AC", imageId = R.drawable.ac, navController = navController)
            }
        }
    }
}

@Composable
fun TicketIcon(type: String, imageId: Int, navController: NavController) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .size(160.dp)
            .background(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFF28262B)
            )
            .clickable {
                navController.navigate("new_ticket/$type")
            },
        contentAlignment = Alignment.BottomCenter
    ) {
        Image(
            painter = painterResource(id = imageId),
            contentDescription = "$type Image",
            modifier = Modifier
                .size(120.dp)
                .padding(bottom = 70.dp)
        )
        Text(
            text = type,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.headlineSmall
        )
    }
}