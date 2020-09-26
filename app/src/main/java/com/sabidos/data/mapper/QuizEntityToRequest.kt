package com.sabidos.data.mapper

import com.sabidos.data.local.entities.PostQuizEntity
import com.sabidos.data.remote.model.QuizRequest
import com.sabidos.domain.Alternative

object QuizEntityToRequest : DataMapper<PostQuizEntity, QuizRequest>() {

    override fun transform(entity: PostQuizEntity): QuizRequest {
        return QuizRequest(
            quizId = entity.quizId,
            timeToAnswer = entity.timeToAnswer,
            alternative = Alternative(
                description = entity.alternative.description,
                isCorrect = entity.alternative.isCorrect,
                percentageAnswered = entity.alternative.percentageAnswered
            )
        )
    }

    override fun transform(entities: List<PostQuizEntity>): List<QuizRequest> {
        return entities.map { transform(it) }
    }

}
