package com.sabidos.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sabidos.data.local.entities.PostQuizEntity

@Dao
interface QuizDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(postQuizEntity: PostQuizEntity)

    @Query("DELETE FROM post_quiz")
    fun deleteAll()

    @Query("SELECT * FROM post_quiz")
    fun getAll(): List<PostQuizEntity>

}