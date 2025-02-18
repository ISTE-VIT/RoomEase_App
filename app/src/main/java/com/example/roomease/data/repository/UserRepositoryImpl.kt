package com.example.roomease.data.repository

import com.example.roomease.domain.model.UserHostelDetails
import com.example.roomease.domain.repository.UserRepository

class UserRepositoryImpl: UserRepository {
    // In a real implementation, this might be backed by local storage or a remote DB.
    private var storedDetails: UserHostelDetails? = null

    override suspend fun getUserHostelDetails(userId: String): UserHostelDetails? {
        return storedDetails?.takeIf { it.userId == userId }
    }

    override suspend fun saveUserHostelDetails(details: UserHostelDetails) {
        storedDetails =  details
    }
}