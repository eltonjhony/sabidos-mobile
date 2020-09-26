package com.sabidos.infrastructure

import com.sabidos.data.remote.model.ErrorResponse

data class Resource<out T> constructor(
    val state: ResourceState,
    val data: T? = null,
    val error: Error? = null,
    val errorResponse: ErrorResponse? = null
)