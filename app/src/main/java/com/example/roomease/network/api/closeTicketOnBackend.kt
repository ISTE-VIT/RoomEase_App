package com.example.roomease.network.api

import android.util.Log
import com.example.roomease.network.HttpClientFactory
import com.example.roomease.network.constructUrl
import com.example.roomease.network.safeCall
import com.example.roomease.utils.network.Result
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.put
import io.ktor.client.statement.bodyAsText

suspend fun closeTicketOnBackend(firebaseUid: String, category: String): Boolean {
    val client = HttpClientFactory.create(CIO.create())

    // This path matches the @PutMapping in the backend("/close/{firebaseUid}/{category}")
    val url = constructUrl("/api/tasks/close/$firebaseUid/$category")
    Log.d("closeTicketOnBackend", "Closing ticket on $url")

    val result = safeCall<Unit> {
        val response = client.put(url)
        Log.d("closeTicketOnBackend", "Response status: ${response.status.value}")
        Log.d("closeTicketOnBackend", "Response body: ${response.bodyAsText()}")
        response
    }

    return if (result is Result.Success) {
        Log.d("closeTicketOnBackend", "Ticket closed successfully")
        true
    } else {
        Log.d("closeTicketOnBackend", "Failed to close ticket: $result")
        false
    }
}