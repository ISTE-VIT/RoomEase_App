package com.example.roomease.domain.model

enum class HostelType {
    MENS, LADIES
}

data class UserHostelDetails(
    val userId: String = "",
    val phoneNumber: String,
    val hostelType: HostelType,
    val hostelBlock: String,
    val roomNumber: String
)
