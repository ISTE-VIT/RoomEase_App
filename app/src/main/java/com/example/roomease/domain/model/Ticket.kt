package com.example.roomease.domain.model

enum class TicketCategory {
    CLEANING, ELECTRICAL, PLUMBING, AC
}

enum class TicketStatus {
    PENDING, COMPLETED
}

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
)