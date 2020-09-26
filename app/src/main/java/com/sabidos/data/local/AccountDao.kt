package com.sabidos.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sabidos.data.local.entities.AccountEntity

@Dao
interface AccountDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(account: AccountEntity)

    @Query("SELECT * FROM account LIMIT 1")
    fun getCurrentAccount(): AccountEntity

    @Query("SELECT nickname FROM account LIMIT 1")
    fun getNickName(): String

}