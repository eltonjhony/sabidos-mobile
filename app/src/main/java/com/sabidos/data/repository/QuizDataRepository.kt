package com.sabidos.data.repository

import com.sabidos.data.local.cache.AccountCache
import com.sabidos.data.mapper.QuizEntityToRequest
import com.sabidos.data.mapper.QuizRequestToEntity
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
            val result = cloudQuizDataSource.postQuiz(it, request)
            when (result) {
                is ResultWrapper.Success -> {
                    accountCache.invalidate()
                }
                else -> handlePostQuizError(request, result)
            }
            result
        } ?: handlePostQuizError(request)

    override suspend fun syncQuiz(): ResultWrapper<Boolean> =
        localAccountDataSource.getCurrentNickname()?.let { nickname ->
            localQuizDataSource.getAll()?.let { failedRequests ->
                val result = cloudQuizDataSource.syncQuiz(
                    nickname,
                    QuizEntityToRequest.transform(failedRequests)
                )
                when (result) {
                    is ResultWrapper.Success -> {
                        accountCache.invalidate()
                        localQuizDataSource.clear()
                    }
                }
                result
            } ?: DataNotFoundError
        } ?: DataNotFoundError

    private fun handlePostQuizError(
        request: QuizRequest,
        result: ResultWrapper<Boolean>? = null
    ): ResultWrapper<Boolean> {
        localQuizDataSource.insert(QuizRequestToEntity.transform(request))
        return result ?: ResultWrapper.GenericError()
    }

}