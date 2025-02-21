package com.example.roomease.network.api

import com.example.roomease.domain.model.UserHostelDetails
import com.example.roomease.network.HttpClientFactory
import com.example.roomease.network.constructUrl
import com.example.roomease.network.safeCall
import com.example.roomease.utils.Result
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import android.util.Log

suspend fun sendHostelDetailsToBackend(details: UserHostelDetails): Boolean {
    val client = HttpClientFactory.create(engine = CIO.create())
    val url = constructUrl("/io")
    Log.d("sendHostelDetailsToBackend", "Sending details to $url")
    val result = safeCall<Unit> {
        val response = client.post(url) {
            setBody(details)
        }
        Log.d("sendHostelDetailsToBackend", "Response status: ${response.status.value}")
        Log.d("sendHostelDetailsToBackend", "Response body: ${response.bodyAsText()}")
        response
    }

    return if (result is Result.Success) {
        Log.d("sendHostelDetailsToBackend", "Details sent successfully")
        true
    } else {
        Log.e("sendHostelDetailsToBackend", "Failed to send details: $result")
        false
    }
}