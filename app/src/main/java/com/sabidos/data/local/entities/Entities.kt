package com.sabidos.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account")
data class AccountEntity(
    val name: String,
    @PrimaryKey val nickname: String,
    @Embedded val avatar: AvatarEntity?,
    @Embedded val reputation: ReputationEntity,
    val xpFactor: Int,
    val totalAnswered: Int,
    val totalHits: Int
)

@Entity(tableName = "reputation")
data class ReputationEntity(
    val level: Int,
    val stars: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

@Entity(tableName = "avatar")
data class AvatarEntity(
    @PrimaryKey @ColumnInfo(name = "avatar_id") val id: Int,
    val imageUrl: String?
)

@Entity(tableName = "category")
data class CategoryEntity(
    @PrimaryKey val id: Int,
    val description: String,
    val imageUrl: String?,
    val iconUrl: String?
)