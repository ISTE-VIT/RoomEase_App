package com.example.roomease.domain.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.roomease.utils.serializer.LocalDateTimeSerializer
import com.example.roomease.utils.serializer.UUIDSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID
import java.time.LocalDateTime

@Serializable
enum class TicketCategory {
    CLEANING, ELECTRICAL, PLUMBING, AC
}

@Serializable
enum class TicketStatus {
    PENDING, COMPLETED
}

@Serializable
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

@Serializable
sealed interface TicketDetails

@Serializable
data class CleaningDetails(
    val timeSlot: TimeSlot
) : TicketDetails

@Serializable
data class ElectricalDetails(
    val timeSlot: TimeSlot,
    val electricalIssueType: String?,
    val additionalDescription: String?
) : TicketDetails

@Serializable
data class PlumbingDetails(
    val plumbingIssue: String?,
    val additionalDescription: String?
) : TicketDetails

@Serializable
data class ACDetails(
    val timeSlot: TimeSlot,
    val acDescription: String
) : TicketDetails

@Serializable
@RequiresApi(Build.VERSION_CODES.O)
data class Ticket @RequiresApi(Build.VERSION_CODES.O) constructor(
    @Serializable(with = UUIDSerializer::class) val id: UUID = UUID.randomUUID(),
    @SerialName("firebaseUid")
    val userId: String,
    val status: TicketStatus,
    val category: TicketCategory,
    @Serializable(with = LocalDateTimeSerializer::class) val createdAt: LocalDateTime = LocalDateTime.now(),
    @Serializable(with = LocalDateTimeSerializer::class) val completedAt: LocalDateTime? = null,
    val details: TicketDetails
)