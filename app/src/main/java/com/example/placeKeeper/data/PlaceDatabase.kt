package com.example.placeKeeper.data

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.placeKeeper.data.dao.CategoryDao
import com.example.placeKeeper.data.dao.PlaceDao
import com.example.placeKeeper.data.entities.CategoryEntity
import com.example.placeKeeper.data.entities.PlaceEntity

@Database(
    entities = [PlaceEntity::class, CategoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class LocationDatabase : RoomDatabase() {
    abstract fun savedLocationDao(): PlaceDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        @Volatile
        private var INSTANCE: LocationDatabase? = null

        fun getDatabase(context: Context): LocationDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocationDatabase::class.java,
                    "location_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}