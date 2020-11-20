package com.sabidos.data.mapper

import com.sabidos.data.local.entities.AlternativeEntity
import com.sabidos.data.local.entities.PostQuizEntity
import com.sabidos.data.remote.model.QuizRequest

object QuizRequestToEntity : DataMapper<QuizRequest, PostQuizEntity>() {

    override fun transform(entity: QuizRequest): PostQuizEntity {
        return PostQuizEntity(
            quizId = entity.quizId,
            responseTime = entity.responseTime,
            alternative = AlternativeEntity(
                id = entity.quizId,
                description = entity.alternative.description,
                isCorrect = entity.alternative.isCorrect,
                percentageAnswered = entity.alternative.percentageAnswered
            )
        )
    }

    override fun transform(entities: List<QuizRequest>): List<PostQuizEntity> {
        return entities.map { transform(it) }
    }

}
