package com.example.roomease.di

import com.example.roomease.data.repository.TicketRepositoryImpl
import com.example.roomease.data.repository.UserRepositoryImpl
import com.example.roomease.domain.repository.TicketRepository
import com.example.roomease.domain.repository.UserRepository
import com.example.roomease.domain.usecase.CreateTicketUseCase
import com.example.roomease.domain.usecase.GetTicketsForUserUseCase
import com.example.roomease.domain.usecase.GetUserHostelDetailsUseCase
import com.example.roomease.domain.usecase.SaveUserHostelDetailsUseCase
import com.example.roomease.domain.usecase.UpdateTicketStatusUseCase
import com.example.roomease.ui.viewmodel.TicketViewModel
import com.example.roomease.ui.viewmodel.UserViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    /*-----User-related dependencies-----*/
    // Provide repository
    single<UserRepository> { UserRepositoryImpl() }

    // Provide use case
    factory { GetUserHostelDetailsUseCase(get()) }
    factory { SaveUserHostelDetailsUseCase(get()) }

    // Provide ViewModels
    viewModel { UserViewModel(get(), get()) }


    /*------Ticket-related dependencies-----*/
    // Provide repository
    single<TicketRepository> { TicketRepositoryImpl() }

    // Provide use case
    factory { CreateTicketUseCase(get()) }
    factory { UpdateTicketStatusUseCase(get()) }
    factory { GetTicketsForUserUseCase(get()) }

    // Provide ViewModels
    viewModel { TicketViewModel(get(), get(), get()) }
}