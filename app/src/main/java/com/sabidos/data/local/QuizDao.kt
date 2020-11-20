package com.sabidos.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sabidos.data.local.entities.PostQuizEntity
import com.sabidos.data.local.entities.PostRoundEntity

@Dao
interface QuizDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertQuiz(postQuizEntity: PostQuizEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRound(postRoundEntity: PostRoundEntity)

    @Query("DELETE FROM post_quiz")
    fun deleteAllQuiz()

    @Query("DELETE FROM post_round")
    fun deleteAllRound()

    @Query("SELECT * FROM post_quiz")
    fun getAllFailedQuiz(): List<PostQuizEntity>

    @Query("SELECT * FROM post_round")
    fun getAllFailedRound(): List<PostRoundEntity>

}