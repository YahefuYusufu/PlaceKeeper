package com.example.placeKeeper.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_places")
data class FavoritePlaceEntity(
    @PrimaryKey
    val placeId: Long,
    val createdAt: Long = System.currentTimeMillis()
)