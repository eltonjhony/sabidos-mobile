package com.sabidos.data.remote

import com.sabidos.data.remote.model.*
import retrofit2.http.*

interface AccountService {

    @POST("v1/account")
    suspend fun createAccount(@Body account: AccountRequest): AccountResponse

    @PATCH("v1/account/{uid}")
    suspend fun updateAccount(@Path(value = "uid") uid: String, @Body account: UpdateAccountRequest): AccountResponse

    @POST("v1/account/validate")
    suspend fun validateAccount(@Body account: AccountRequest)

    @GET("v1/account/nickname/{nickname}")
    suspend fun getAccountBy(@Path(value = "nickname") nickname: String): AccountResponseWrapper

    @GET("v1/account/uid/{uid}")
    suspend fun getAccountByUid(@Path(value = "uid") uid: String): AccountResponseWrapper

    @GET("v1/weekly/hits/{nickname}")
    suspend fun getWeeklyHits(
        @Path(value = "nickname") nickname: String,
        @Query(value = "endDate") endDate: String
    ): WeeklyHitsWrapperResponse

    @GET("v1/timeline/{nickname}")
    suspend fun getTimelineFor(
        @Path(value = "nickname") nickname: String,
        @Query(value = "page") page: Int,
        @Query(value = "pageSize") pageSize: Int = 20,
        @Query(value = "orderBy") orderBy: String = "date",
        @Query(value = "sort") sort: String = "DESC"
    ): TimelineWrapperResponse

}