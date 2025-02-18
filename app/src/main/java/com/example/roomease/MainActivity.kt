package com.example.roomease

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.roomease.ui.navigation.AppNavHost
import com.example.roomease.ui.theme.RoomEaseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RoomEaseTheme {
                AppNavHost()
            }
        }
    }
}
