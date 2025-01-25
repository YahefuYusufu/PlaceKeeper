package com.example.placeKeeper.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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

    companion object {
        @Volatile
        private var INSTANCE: PlaceDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS favorite_places (
                        placeId LONG NOT NULL PRIMARY KEY,
                        createdAt LONG NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (placeId) REFERENCES place (id) ON DELETE CASCADE
                    )
                """)
            }
        }

        fun getDatabase(context: Context): PlaceDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PlaceDatabase::class.java,
                    "place_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}