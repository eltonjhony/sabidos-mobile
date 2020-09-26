package com.sabidos.data.mapper

import com.sabidos.data.remote.model.AlternativeResponse
import com.sabidos.data.remote.model.QuizResponse
import com.sabidos.domain.Alternative
import com.sabidos.domain.Explanation
import com.sabidos.domain.Quiz

object ResponseToQuizMapper : DataMapper<QuizResponse, Quiz>() {

    override fun transform(entity: QuizResponse): Quiz {
        return Quiz(
            id = entity.id,
            imageUrl = entity.imageUrl,
            description = entity.description,
            alternatives = ResponseToAlternativeMapper.transform(entity.alternatives),
            explanation = Explanation(
                description = entity.explanation.description,
                resource = entity.explanation.resource
            )
        )
    }

    override fun transform(entities: List<QuizResponse>): List<Quiz> {
        return entities.map { transform(it) }
    }

}

object ResponseToAlternativeMapper : DataMapper<AlternativeResponse, Alternative>() {

    override fun transform(entity: AlternativeResponse): Alternative {
        return Alternative(
            description = entity.description,
            isCorrect = entity.isCorrect,
            percentageAnswered = entity.percentageAnswered
        )
    }

    override fun transform(entities: List<AlternativeResponse>): List<Alternative> {
        return entities.map { transform(it) }
    }

}