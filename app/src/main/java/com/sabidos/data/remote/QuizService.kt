package com.sabidos.data.remote

import com.sabidos.data.remote.model.FinishRoundRequest
import com.sabidos.data.remote.model.QuizRequest
import com.sabidos.data.remote.model.QuizResponseWrapper
import retrofit2.http.*

interface QuizService {

    @GET("v1/quiz/round/{nickname}")
    suspend fun getRound(
        @Path(value = "nickname") nickname: String,
        @Query(value = "categoryId") categoryId: Int
    ): QuizResponseWrapper

    @POST("v1/quiz/{nickname}")
    suspend fun postQuiz(
        @Path(value = "nickname") nickname: String, @Body request: List<QuizRequest>
    )

    @POST("v1/quiz/round/{nickname}")
    suspend fun postRound(
        @Path(value = "nickname") nickname: String, @Body request: List<FinishRoundRequest>
    )

}