package com.sabidos.data.remote

import android.content.Context
import com.sabidos.infrastructure.extensions.networkInfo

class NetworkHandler(private val context: Context) {
    val isConnected get() = context.networkInfo?.isConnected ?: false
}