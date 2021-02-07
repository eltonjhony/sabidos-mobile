package com.sabidos.data.remote

import com.sabidos.data.remote.model.PostQuizResponse
import com.sabidos.data.remote.model.QuizRequest
import com.sabidos.data.remote.model.QuizResponseWrapper
import retrofit2.http.*

interface QuizService {

    @GET("v1/quiz/round/{nickname}")
    suspend fun getRound(
        @Path(value = "nickname") nickname: String,
        @Query(value = "categoryId") categoryId: Int
    ): QuizResponseWrapper

    @POST("v1/quiz")
    suspend fun postQuiz(@Body request: QuizRequest): PostQuizResponse

}