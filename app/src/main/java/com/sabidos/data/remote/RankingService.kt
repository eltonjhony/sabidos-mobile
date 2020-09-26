package com.sabidos.data.remote

import com.sabidos.data.remote.model.RankingWrapperResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RankingService {

    @GET("v1/weekly/ranking/{nickname}")
    suspend fun getWeeklyRanking(
        @Path(value = "nickname") nickname: String,
        @Query(value = "endDate") endDate: String
    ): RankingWrapperResponse

}