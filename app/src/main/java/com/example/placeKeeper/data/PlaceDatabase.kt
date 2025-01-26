package com.example.placeKeeper.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.placeKeeper.data.dao.CategoryDao
import com.example.placeKeeper.data.dao.PlaceDao
import com.example.placeKeeper.data.entities.CategoryEntity
import com.example.placeKeeper.data.entities.FavoritePlaceEntity
import com.example.placeKeeper.data.entities.PlaceEntity

@Database(
    entities = [PlaceEntity::class, CategoryEntity::class, FavoritePlaceEntity::class],
    version = 2,
    exportSchema = false
)
abstract class PlaceDatabase : RoomDatabase() {
    abstract fun placeDao(): PlaceDao
    abstract fun categoryDao(): CategoryDao
}