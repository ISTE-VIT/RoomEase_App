package com.example.roomease.domain.repository

import com.example.roomease.domain.model.UserHostelDetails

interface UserRepository {
    suspend fun getUserHostelDetails(userId: String): UserHostelDetails?
    suspend fun saveUserHostelDetails(details: UserHostelDetails)
}