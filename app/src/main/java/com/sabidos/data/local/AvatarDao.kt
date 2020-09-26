package com.sabidos.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sabidos.data.local.entities.AvatarEntity

@Dao
interface AvatarDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(category: AvatarEntity)

    @Query("SELECT * FROM avatar")
    fun getAll(): List<AvatarEntity>

}