package com.sabidos.domain.interactor

import com.sabidos.data.remote.model.FinishRoundRequest
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

class PostRoundUseCase(private val quizRepository: QuizRepository) :
    UseCase<Boolean, PostRoundUseCase.Params>() {

    override suspend fun run(params: Params): ResultWrapper<Boolean> =
        quizRepository.postRound(params.request)

    data class Params(val request: FinishRoundRequest)
}