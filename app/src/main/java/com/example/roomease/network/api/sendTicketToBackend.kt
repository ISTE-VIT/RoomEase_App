package com.example.roomease.network.api

import com.example.roomease.domain.model.Ticket
import com.example.roomease.network.HttpClientFactory
import com.example.roomease.network.constructUrl
import com.example.roomease.network.safeCall
import com.example.roomease.utils.network.Result
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import android.util.Log
import com.example.roomease.utils.transformTicket

suspend fun sendTicketToBackend(ticket: Ticket): Boolean {
    val client = HttpClientFactory.create(engine = CIO.create())
    val url = constructUrl("/api/tasks/create")
    val ticketJson = transformTicket(ticket)
    Log.d("sendTicketToBackend", "Sending ticket to $url")
    Log.d("sendTicketToBackend", "Ticket body: $ticketJson")
    val result = safeCall<Unit> {
        val response = client.post(url) {
            setBody(ticketJson)
        }
        Log.d("sendTicketToBackend", "Response status: ${response.status.value}")
        Log.d("sendTicketToBackend", "Response body: ${response.bodyAsText()}")
        response
    }
    return if (result is Result.Success) {
        Log.d("sendTicketToBackend", "Ticket sent successfully")
        true
    } else {
        Log.e("sendTicketToBackend", "Failed to send ticket: $result")
        false
    }
}