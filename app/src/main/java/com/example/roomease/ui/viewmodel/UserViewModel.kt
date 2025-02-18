package com.example.roomease.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roomease.domain.model.UserHostelDetails
import com.example.roomease.domain.usecase.GetUserHostelDetailsUseCase
import com.example.roomease.domain.usecase.SaveUserHostelDetailsUseCase
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get

class UserViewModel(
    private val getUserHostelDetailsUseCase: GetUserHostelDetailsUseCase,
    private val saveUserHostelDetailsUseCase: SaveUserHostelDetailsUseCase
): ViewModel() {

    var userHostelDetails: UserHostelDetails? = null
        private set

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
}