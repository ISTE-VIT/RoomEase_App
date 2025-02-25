package com.example.roomease.utils

import com.example.roomease.domain.model.CleaningDetails
import com.example.roomease.domain.model.ElectricalDetails
import com.example.roomease.domain.model.PlumbingDetails
import com.example.roomease.domain.model.ACDetails
import com.example.roomease.domain.model.Ticket
import com.example.roomease.domain.model.TicketCategory
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.JsonObject

@OptIn(InternalSerializationApi::class)
fun transformTicket(ticket: Ticket): String {
    val json = Json { prettyPrint = true }
    val base = buildJsonObject {
        put("firebaseUid", JsonPrimitive(ticket.userId))
        put("createdAt", JsonPrimitive(ticket.createdAt.toString())) // Adjust formatting if needed
        put("closedAt", ticket.completedAt?.toString()?.let { JsonPrimitive(it) } ?: JsonNull)
        put("category", JsonPrimitive(ticket.category.toString()))
    }
    // Merge in type-specific fields
    val modified = when(ticket.category) {
        TicketCategory.CLEANING -> {
            val details = ticket.details as CleaningDetails
            base.toMutableMap().apply {
                put("timeSlot", JsonPrimitive(details.timeSlot.displayName))
            }
        }
        TicketCategory.ELECTRICAL -> {
            val details = ticket.details as ElectricalDetails
            base.toMutableMap().apply {
                put("timeSlot", JsonPrimitive(details.timeSlot.displayName))
                put("electricalIssue", details.electricalIssueType?.let { JsonPrimitive(it) } ?: JsonNull)
                put("description", details.additionalDescription?.let { JsonPrimitive(it) } ?: JsonNull)
            }
        }
        TicketCategory.PLUMBING -> {
            val details = ticket.details as PlumbingDetails
            base.toMutableMap().apply {
                put("plumbingIssue", details.plumbingIssue?.let { JsonPrimitive(it) } ?: JsonNull)
                put("description", details.additionalDescription?.let { JsonPrimitive(it) } ?: JsonNull)
            }
        }
        TicketCategory.AC -> {
            val details = ticket.details as ACDetails
            base.toMutableMap().apply {
                put("timeSlot", JsonPrimitive(details.timeSlot.displayName))
                put("description", JsonPrimitive(details.acDescription))
            }
        }
    }
    val finalJsonObject = buildJsonObject {
        modified.forEach { (key, value) -> put(key, value) }
    }
    // Encode the final map into JSON.
    return json.encodeToString(JsonObject.serializer(), finalJsonObject)
}