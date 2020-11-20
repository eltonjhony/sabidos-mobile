package com.sabidos.data.repository.datasources

import com.sabidos.data.local.QuizDao
import com.sabidos.data.local.entities.PostQuizEntity
import com.sabidos.data.local.entities.PostRoundEntity
import com.sabidos.infrastructure.logging.Logger

class LocalQuizDataSource(private val quizDao: QuizDao) {

    fun insertQuiz(entity: PostQuizEntity) = runCatching {
        quizDao.insertQuiz(entity)
    }.getOrElse {
        Logger.withTag(LocalQuizDataSource::class.java.simpleName).withCause(it)
    }

    fun insertRound(entity: PostRoundEntity) = runCatching {
        quizDao.insertRound(entity)
    }.getOrElse {
        Logger.withTag(LocalQuizDataSource::class.java.simpleName).withCause(it)
    }

    fun getAllFailedQuiz(): List<PostQuizEntity>? = runCatching {
        val failedRequests = quizDao.getAllFailedQuiz()
        if (failedRequests.isEmpty()) {
            null
        } else {
            failedRequests
        }
    }.getOrNull()

    fun getAllFailedRound(): List<PostRoundEntity>? = runCatching {
        val failedRequests = quizDao.getAllFailedRound()
        if (failedRequests.isEmpty()) {
            null
        } else {
            failedRequests
        }
    }.getOrNull()

    fun clearAllQuiz() {
        quizDao.deleteAllQuiz()
    }

    fun clearAllRound() {
        quizDao.deleteAllRound()
    }

}