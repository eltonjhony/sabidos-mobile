package com.sabidos.infrastructure

sealed class ResourceState {
    object Loading : ResourceState()
    object Success : ResourceState()
    object Finished : ResourceState()
    object NetworkError : ResourceState()
    object ValidationError : ResourceState()
    object ApiError : ResourceState()
    object GenericError : ResourceState()
    object DataNotFoundError : ResourceState()
}