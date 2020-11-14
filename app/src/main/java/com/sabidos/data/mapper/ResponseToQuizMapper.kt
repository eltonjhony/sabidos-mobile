package com.sabidos.data.mapper

import com.sabidos.data.remote.model.AlternativeResponse
import com.sabidos.data.remote.model.QuizResponse
import com.sabidos.data.remote.model.QuizResponseWrapper
import com.sabidos.domain.Alternative
import com.sabidos.domain.Explanation
import com.sabidos.domain.Quiz
import com.sabidos.domain.QuizItem

object ResponseToQuizMapper : DataMapper<QuizResponseWrapper, Quiz>() {

    override fun transform(entity: QuizResponseWrapper): Quiz {
        return Quiz(
            id = entity.id,
            numberOfQuestions = entity.numberOfQuestions,
            questions = ResponseToQuizItemMapper.transform(entities = entity.questions)
        )
    }

    override fun transform(entities: List<QuizResponseWrapper>): List<Quiz> {
        return entities.map { transform(it) }
    }

}

object ResponseToQuizItemMapper : DataMapper<QuizResponse, QuizItem>() {

    override fun transform(entity: QuizResponse): QuizItem {
        return QuizItem(
            id = entity.id,
            position = entity.position,
            imageUrl = entity.imageUrl,
            description = entity.description,
            quizLimitInSeconds = entity.quizLimitInSeconds,
            category = ResponseToCategoryMapper.transform(entity.category),
            alternatives = ResponseToAlternativeMapper.transform(entity.alternatives),
            explanation = Explanation(
                description = entity.explanation.description,
                resource = entity.explanation.resource
            )
        )
    }

    override fun transform(entities: List<QuizResponse>): List<QuizItem> {
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