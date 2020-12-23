package com.sabidos.data.repository.datasources

import com.sabidos.data.mapper.ResponseToQuizMapper
import com.sabidos.data.remote.CloudApiFactory
import com.sabidos.data.remote.ErrorHandling
import com.sabidos.data.remote.NetworkHandler
import com.sabidos.data.remote.QuizService
import com.sabidos.data.remote.model.FinishRoundRequest
import com.sabidos.data.remote.model.QuizRequest
import com.sabidos.domain.Quiz
import com.sabidos.infrastructure.ResultWrapper

class CloudQuizDataSource(
    private val cloudApiFactory: CloudApiFactory,
    private val networkHandler: NetworkHandler
) {

    suspend fun getNextRound(
        nickname: String,
        categoryId: Int
    ): ResultWrapper<Quiz> {
        return when {
            networkHandler.isConnected -> {
                runCatching {
                    val service = cloudApiFactory.create(QuizService::class.java)
                    val response = service.getRound(nickname, categoryId)
                    ResultWrapper.Success(ResponseToQuizMapper.transform(response))
                }.getOrElse { ErrorHandling.parse(it) }
            }
            else -> ResultWrapper.NetworkError
        }
    }

    suspend fun postQuiz(nickname: String, request: List<QuizRequest>): ResultWrapper<Boolean> {
        return when {
            networkHandler.isConnected -> {
                runCatching {
                    val service = cloudApiFactory.create(QuizService::class.java)
                    service.postQuiz(nickname, request)
                    ResultWrapper.Success(true)
                }.getOrElse { ErrorHandling.parse(it) }
            }
            else -> ResultWrapper.NetworkError
        }
    }

    suspend fun postRound(nickname: String, request: FinishRoundRequest): ResultWrapper<Boolean> {
        return when {
            networkHandler.isConnected -> {
                runCatching {
                    val service = cloudApiFactory.create(QuizService::class.java)
                    request.nickname = nickname
                    service.postRound(request)
                    ResultWrapper.Success(true)
                }.getOrElse { ErrorHandling.parse(it) }
            }
            else -> ResultWrapper.NetworkError
        }
    }

}