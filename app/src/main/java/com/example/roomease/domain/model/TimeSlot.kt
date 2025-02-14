package com.example.roomease.domain.model

enum class TimeSlot(val displayName: String) {
    MORNING("Morning"),
    AFTERNOON("Afternoon"),
    EVENING("Evening");

    companion object {
        fun fromDisplayName(displayName: String): TimeSlot? {
            return entries.find { it.displayName == displayName }
        }
    }
}