package com.sabidos.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sabidos.data.local.entities.*
import com.sabidos.infrastructure.logging.Logger

@Database(
    entities = [AccountEntity::class, ReputationEntity::class, AvatarEntity::class, CategoryEntity::class, PostQuizEntity::class, PostRoundEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun accountDao(): AccountDao
    abstract fun categoryDao(): CategoryDao
    abstract fun avatarDao(): AvatarDao
    abstract fun quizDao(): QuizDao

    fun clearAllData() {
        runCatching {
            clearAllTables()
        }.onFailure { Logger.withTag(AppDatabase::class.java.simpleName).withCause(it) }
    }

    companion object {

        fun getDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "sabidos.db"
            ).build()
        }

    }

}