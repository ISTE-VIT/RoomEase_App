package com.example.roomease.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.graphics.Color

@Composable
fun TicketIcon(
    type: String,
    imageId: Int,
    onTicketClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Use a rounded Box with a Column to neatly display the icon above the label.
   Box(
       modifier = modifier
           .padding(8.dp)
           .background(
               color = MaterialTheme.colorScheme.surfaceContainer,
               shape = RoundedCornerShape(16.dp)
           )
           .clickable { onTicketClick(type) },
       contentAlignment = Alignment.Center
   )
    {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            //verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(12.dp)
        ) {
            Image(
                painter = painterResource(id = imageId),
                contentDescription = "$type Icon",
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                text = type,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}