package com.sabidos.data.remote

import com.sabidos.data.remote.model.AvatarWrapperResponse
import retrofit2.http.GET

interface AvatarService {

    @GET("v1/avatars")
    suspend fun getAvatars(): AvatarWrapperResponse

}