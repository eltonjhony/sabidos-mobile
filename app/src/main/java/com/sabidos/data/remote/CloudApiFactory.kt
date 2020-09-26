package com.sabidos.data.remote

interface CloudApiFactory {
    suspend fun <T> create(service: Class<T>): T
}