package com.sabidos.data.remote

import com.sabidos.data.remote.model.CategoryWrapperResponse
import retrofit2.http.GET

interface CategoryService {

    @GET("v1/categories")
    suspend fun getCategories(): CategoryWrapperResponse

}