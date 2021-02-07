package com.sabidos.data.repository.datasources

import com.sabidos.data.mapper.ResponseToQuizMapper
import com.sabidos.data.remote.CloudApiFactory
import com.sabidos.data.remote.ErrorHandling
import com.sabidos.data.remote.NetworkHandler
import com.sabidos.data.remote.QuizService
import com.sabidos.data.remote.model.PostQuizResponse
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

    suspend fun postQuiz(nickname: String, request: QuizRequest): ResultWrapper<PostQuizResponse> {
        return when {
            networkHandler.isConnected -> {
                runCatching {
                    val service = cloudApiFactory.create(QuizService::class.java)
                    request.nickname = nickname
                    val result = service.postQuiz(request)
                    ResultWrapper.Success(result)
                }.getOrElse { ErrorHandling.parse(it) }
            }
            else -> ResultWrapper.NetworkError
        }
    }

}