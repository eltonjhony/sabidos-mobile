package com.sabidos.domain.interactor

import com.sabidos.data.remote.model.PostQuizResponse
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
    UseCase<PostQuizResponse, PostQuizUseCase.Params>() {

    override suspend fun run(params: Params): ResultWrapper<PostQuizResponse> =
        quizRepository.postQuiz(params.request)

    data class Params(val request: QuizRequest)
}