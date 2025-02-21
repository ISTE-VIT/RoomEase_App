package com.example.roomease.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roomease.domain.model.User
import com.example.roomease.domain.model.UserHostelDetails
import com.example.roomease.domain.usecase.GetUserHostelDetailsUseCase
import com.example.roomease.domain.usecase.SaveUserDetailsUseCase
import com.example.roomease.domain.usecase.SaveUserHostelDetailsUseCase
import kotlinx.coroutines.launch

class UserViewModel(
    private val getUserHostelDetailsUseCase: GetUserHostelDetailsUseCase,
    private val saveUserHostelDetailsUseCase: SaveUserHostelDetailsUseCase,
    private val saveUserDetailsUseCase: SaveUserDetailsUseCase
): ViewModel() {

    var userHostelDetails: UserHostelDetails? = null
        private set

    var loggedInUser: User? = null
        private set

    // New function to store user information after login
    fun storeUser(user: User, onSuccess: () -> Unit) {
        viewModelScope.launch {
            saveUserDetailsUseCase(user)
            loggedInUser = user
            onSuccess()
        }
    }

    fun loadUserDetails(userId: String) {
        viewModelScope.launch {
            userHostelDetails = getUserHostelDetailsUseCase(userId)
        }
    }

    fun saveUserDetails(details: UserHostelDetails, onSuccess: () -> Unit) {
        viewModelScope.launch {
            saveUserHostelDetailsUseCase(details)
            // Optionally, reload the details after saving.
            userHostelDetails = details
            onSuccess()
        }
    }

    fun updateUserDetails(updatedDetails: UserHostelDetails, onSuccess: () -> Unit) {
        viewModelScope.launch {
            saveUserHostelDetailsUseCase(updatedDetails)
            userHostelDetails = updatedDetails
            onSuccess()
        }
    }
}