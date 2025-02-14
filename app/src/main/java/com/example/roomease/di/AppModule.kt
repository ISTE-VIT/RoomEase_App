package com.example.roomease.di

import com.example.roomease.data.repository.TicketRepositoryImpl
import com.example.roomease.domain.repository.TicketRepository
import com.example.roomease.domain.usecase.CreateTicketUseCase
import com.example.roomease.domain.usecase.GetTicketsForUserUseCase
import com.example.roomease.domain.usecase.UpdateTicketStatusUseCase
import com.example.roomease.ui.viewmodel.ticket.TicketViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Provide repository
    single<TicketRepository> { TicketRepositoryImpl() }

    // Provide use case
    factory { CreateTicketUseCase(get()) }
    factory { UpdateTicketStatusUseCase(get()) }
    factory { GetTicketsForUserUseCase(get()) }

    // Provide ViewModels
    viewModel() { TicketViewModel(get(), get(), get()) }
}