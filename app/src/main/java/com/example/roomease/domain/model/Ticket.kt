package com.example.roomease.domain.model

import java.util.UUID

enum class TicketCategory {
    CLEANING, ELECTRICAL, PLUMBING, AC
}

enum class TicketStatus {
    PENDING, COMPLETED
}

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

/*
data class Ticket(
    val id: String = "",
    val userId: String = "",
    val category: TicketCategory,
    val contactNumber: String,
    val hostelBlock: String,
    val roomNumber: String,
    val timeSlot: TimeSlot,
    val electricalIssueType: String? = null,
    val status: TicketStatus = TicketStatus.PENDING,
    val createdAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null
)*/

data class Ticket(
    val id: UUID = UUID.randomUUID(),
    val userId: String, // Ensure this is passed from an existing User object
    val category: TicketCategory,
    val timeSlot: TimeSlot,
    val electricalIssueType: String? = null,
    val status: TicketStatus = TicketStatus.PENDING,
    val createdAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null
)