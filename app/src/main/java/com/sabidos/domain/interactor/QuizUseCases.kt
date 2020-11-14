package com.sabidos.domain.interactor

import com.sabidos.data.remote.model.QuizRequest
import com.sabidos.domain.Quiz
import com.sabidos.domain.repository.QuizRepository
import com.sabidos.infrastructure.ResultWrapper

class GetNextRoundUseCase(private val quizRepository: QuizRepository) :
    UseCase<Quiz, GetNextRoundUseCase.Params>() {

    override suspend fun run(params: Params): ResultWrapper<Quiz> =
        quizRepository.getNextRound(params.categoryId)

    data class Params(val categoryId: Int)
}

class PostQuizUseCase(private val quizRepository: QuizRepository) :
    UseCase<Boolean, PostQuizUseCase.Params>() {

    override suspend fun run(params: Params): ResultWrapper<Boolean> =
        quizRepository.postQuiz(params.request)

    data class Params(val request: QuizRequest)
}

class SyncQuizUseCase(private val quizRepository: QuizRepository) :
    UseCase<Boolean, None>() {

    override suspend fun run(params: None): ResultWrapper<Boolean> =
        quizRepository.syncQuiz()
}