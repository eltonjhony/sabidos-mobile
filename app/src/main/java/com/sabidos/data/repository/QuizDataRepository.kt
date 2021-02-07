package com.sabidos.data.repository

import com.sabidos.data.local.cache.AccountCache
import com.sabidos.data.remote.model.PostQuizResponse
import com.sabidos.data.remote.model.QuizRequest
import com.sabidos.data.repository.datasources.CloudQuizDataSource
import com.sabidos.data.repository.datasources.LocalAccountDataSource
import com.sabidos.domain.Quiz
import com.sabidos.domain.repository.QuizRepository
import com.sabidos.infrastructure.ResultWrapper
import com.sabidos.infrastructure.ResultWrapper.DataNotFoundError

class QuizDataRepository(
    private val localAccountDataSource: LocalAccountDataSource,
    private val cloudQuizDataSource: CloudQuizDataSource,
    private val accountCache: AccountCache
) : QuizRepository {

    override suspend fun getNextRound(categoryId: Int): ResultWrapper<Quiz> =
        localAccountDataSource.getCurrentNickname()?.let {
            cloudQuizDataSource.getNextRound(it, categoryId)
        } ?: DataNotFoundError

    override suspend fun postQuiz(request: QuizRequest): ResultWrapper<PostQuizResponse> =
        localAccountDataSource.getCurrentNickname()?.let {
            val result = cloudQuizDataSource.postQuiz(it, request)
            if (result is ResultWrapper.Success) {
                accountCache.invalidate()
            }
            result
        } ?: DataNotFoundError

}