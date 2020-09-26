package com.sabidos.infrastructure.extensions

import androidx.lifecycle.MutableLiveData
import com.sabidos.data.remote.model.ErrorResponse
import com.sabidos.infrastructure.Resource
import com.sabidos.infrastructure.ResourceState

fun <T> MutableLiveData<Resource<T>>.setSuccess(data: T? = null) {
    postValue(Resource(ResourceState.Success, data))
}

fun <T> MutableLiveData<Resource<T>>.loading() =
    postValue(Resource(ResourceState.Loading))

fun <T> MutableLiveData<Resource<T>>.setGenericFailure(error: Error? = null) =
    postValue(Resource(ResourceState.GenericError, error = error))

fun <T> MutableLiveData<Resource<T>>.setDataNotFound(error: Error? = null) =
    postValue(Resource(ResourceState.DataNotFoundError, error = error))

fun <T> MutableLiveData<Resource<T>>.setNetworkFailure(error: Error? = null) =
    postValue(Resource(ResourceState.NetworkError, error = error))

fun <T> MutableLiveData<Resource<T>>.setValidationFailure(error: Error? = null) =
    postValue(Resource(ResourceState.ValidationError, error = error))

fun <T> MutableLiveData<Resource<T>>.setApiFailure(errorResponse: ErrorResponse? = null) =
    postValue(Resource(ResourceState.ApiError, errorResponse = errorResponse))