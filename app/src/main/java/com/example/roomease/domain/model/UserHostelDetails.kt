package com.example.roomease.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

enum class HostelType {
    MENS, LADIES
}

@Serializable
data class UserHostelDetails(
    @SerialName("uid")
    val userId: String,
    val phoneNumber: String,
    val hostelType: HostelType,
    val hostelBlock: String,
    val roomNumber: String
)