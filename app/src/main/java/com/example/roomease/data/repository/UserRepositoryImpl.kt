package com.example.roomease.data.repository

import com.example.roomease.domain.model.User
import com.example.roomease.domain.model.UserHostelDetails
import com.example.roomease.domain.repository.UserRepository

class UserRepositoryImpl: UserRepository {
    // In a real implementation, this might be backed by local storage or a remote DB.
    private var storedDetails: UserHostelDetails? = null
    private var userDetails: User? = null  // Store user details

    override suspend fun getUserHostelDetails(userId: String): UserHostelDetails? {
        return storedDetails?.takeIf { it.userId == userId }
    }

    override suspend fun saveUserHostelDetails(details: UserHostelDetails) {
        storedDetails =  details
    }

    override suspend fun saveUserDetails(user: User) {
        userDetails = user  // Save user details
    }
}