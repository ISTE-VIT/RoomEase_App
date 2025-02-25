package com.example.roomease.domain.model

import java.util.UUID

data class User(
    val userId: String,
    val username: String = "",
    val role: String = "STUDENT",
    val email: String? = null,
    val profilePictureUrl: String? = null
)