package com.sabidos.data.repository.datasources

import com.sabidos.data.local.QuizDao
import com.sabidos.data.local.entities.PostQuizEntity
import com.sabidos.infrastructure.logging.Logger

class LocalQuizDataSource(private val quizDao: QuizDao) {

    fun insert(entity: PostQuizEntity) = runCatching {
        quizDao.insert(entity)
    }.getOrElse {
        Logger.withTag(LocalQuizDataSource::class.java.simpleName).withCause(it)
    }

    fun getAll(): List<PostQuizEntity>? = runCatching {
        val failedRequests = quizDao.getAll()
        if (failedRequests.isEmpty()) {
            null
        } else {
            failedRequests
        }
    }.getOrNull()

    fun clear() {
        quizDao.deleteAll()
    }

}