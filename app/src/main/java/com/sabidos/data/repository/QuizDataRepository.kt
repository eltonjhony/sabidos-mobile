package com.sabidos.data.repository

import com.sabidos.data.local.cache.AccountCache
import com.sabidos.data.mapper.FinishRoundRequestToEntity
import com.sabidos.data.mapper.QuizEntityToRequest
import com.sabidos.data.mapper.QuizRequestToEntity
import com.sabidos.data.mapper.RoundEntityToRequest
import com.sabidos.data.remote.model.FinishRoundRequest
import com.sabidos.data.remote.model.QuizRequest
import com.sabidos.data.repository.datasources.CloudQuizDataSource
import com.sabidos.data.repository.datasources.LocalAccountDataSource
import com.sabidos.data.repository.datasources.LocalQuizDataSource
import com.sabidos.domain.Quiz
import com.sabidos.domain.repository.QuizRepository
import com.sabidos.infrastructure.ResultWrapper
import com.sabidos.infrastructure.ResultWrapper.DataNotFoundError

class QuizDataRepository(
    private val localAccountDataSource: LocalAccountDataSource,
    private val cloudQuizDataSource: CloudQuizDataSource,
    private val localQuizDataSource: LocalQuizDataSource,
    private val accountCache: AccountCache
) : QuizRepository {

    override suspend fun getNextRound(categoryId: Int): ResultWrapper<Quiz> =
        localAccountDataSource.getCurrentNickname()?.let {
            cloudQuizDataSource.getNextRound(it, categoryId)
        } ?: DataNotFoundError

    override suspend fun postQuiz(request: QuizRequest): ResultWrapper<Boolean> =
        localAccountDataSource.getCurrentNickname()?.let {
            val result = cloudQuizDataSource.postQuiz(it, listOf(request))
            when (result) {
                is ResultWrapper.Success -> {
                    accountCache.invalidate()
                }
                else -> handlePostQuizError(request, result)
            }
            result
        } ?: handlePostQuizError(request)

    override suspend fun postRound(request: FinishRoundRequest): ResultWrapper<Boolean> =
        localAccountDataSource.getCurrentNickname()?.let {
            val result = cloudQuizDataSource.postRound(it, listOf(request))
            when (result) {
                is ResultWrapper.Success -> {
                    accountCache.invalidate()
                }
                else -> handlePostRoundError(request, result)
            }
            result
        } ?: handlePostRoundError(request)

    override suspend fun syncQuiz(): ResultWrapper<Boolean> =
        localAccountDataSource.getCurrentNickname()?.let { nickname ->

            localQuizDataSource.getAllFailedRound()?.let { failedRequests ->
                val result = cloudQuizDataSource.postRound(
                    nickname,
                    RoundEntityToRequest.transform(entities = failedRequests)
                )
                when (result) {
                    is ResultWrapper.Success -> {
                        accountCache.invalidate()
                        localQuizDataSource.clearAllRound()
                    }
                }
                result
            }

            localQuizDataSource.getAllFailedQuiz()?.let { failedRequests ->
                val result = cloudQuizDataSource.postQuiz(
                    nickname,
                    QuizEntityToRequest.transform(failedRequests)
                )
                when (result) {
                    is ResultWrapper.Success -> {
                        accountCache.invalidate()
                        localQuizDataSource.clearAllQuiz()
                    }
                }
                result
            } ?: DataNotFoundError
        } ?: DataNotFoundError

    private fun handlePostQuizError(
        request: QuizRequest,
        result: ResultWrapper<Boolean>? = null
    ): ResultWrapper<Boolean> {
        localQuizDataSource.insertQuiz(QuizRequestToEntity.transform(request))
        return result ?: ResultWrapper.GenericError()
    }

    private fun handlePostRoundError(
        request: FinishRoundRequest,
        result: ResultWrapper<Boolean>? = null
    ): ResultWrapper<Boolean> {
        localQuizDataSource.insertRound(FinishRoundRequestToEntity.transform(request))
        return result ?: ResultWrapper.GenericError()
    }

}