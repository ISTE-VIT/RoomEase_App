package com.example.roomease.ui.errors

import android.content.Context
import com.example.roomease.R
import com.example.roomease.utils.network.NetworkError

fun NetworkError.toString(context: Context): String {
    val resId = when (this) {
        NetworkError.NO_INTERNET -> R.string.error_no_internet
        NetworkError.REQUEST_TIMEOUT -> R.string.error_too_many_requests
        NetworkError.TOO_MANY_REQUESTS -> R.string.error_no_internet
        NetworkError.SERVER_ERROR -> R.string.error_unknown
        NetworkError.SERIALIZATION_ERROR -> R.string.error_serialization
        NetworkError.UNKNOWN -> R.string.error_unknown
    }

    return context.getString(resId)
}