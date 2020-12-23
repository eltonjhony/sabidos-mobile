package com.sabidos.domain.repository

import com.sabidos.data.remote.model.FinishRoundRequest
import com.sabidos.data.remote.model.QuizRequest
import com.sabidos.domain.Quiz
import com.sabidos.infrastructure.ResultWrapper

interface QuizRepository {
    suspend fun getNextRound(categoryId: Int): ResultWrapper<Quiz>
    suspend fun postQuiz(request: QuizRequest): ResultWrapper<Boolean>
    suspend fun postRound(request: FinishRoundRequest): ResultWrapper<Boolean>
}