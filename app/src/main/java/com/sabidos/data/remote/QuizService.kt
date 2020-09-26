package com.sabidos.data.remote

import com.sabidos.data.remote.model.QuizRequest
import com.sabidos.data.remote.model.QuizResponse
import retrofit2.http.*

interface QuizService {

    @GET("v1/quiz/{nickname}")
    suspend fun getQuiz(
        @Path(value = "nickname") nickname: String,
        @Query(value = "categoryId") categoryId: Int
    ): QuizResponse

    @POST("v1/quiz/{nickname}")
    suspend fun postQuiz(
        @Path(value = "nickname") nickname: String, @Body request: QuizRequest
    )

    @POST("v1/quiz/sync/{nickname}")
    suspend fun syncFailedQuiz(
        @Path(value = "nickname") nickname: String, @Body quiz: List<QuizRequest>
    )

}