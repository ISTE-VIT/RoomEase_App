package com.example.roomease.domain.usecase

import com.example.roomease.domain.model.User
import com.example.roomease.domain.model.UserHostelDetails
import com.example.roomease.domain.repository.UserRepository

class GetUserHostelDetailsUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(userId: String): UserHostelDetails? {
        return repository.getUserHostelDetails(userId)
    }
}

class SaveUserHostelDetailsUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(details: UserHostelDetails) {
        repository.saveUserHostelDetails(details)
    }
}

class SaveUserDetailsUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(user: User) {
        repository.saveUserDetails(user)
    }
}