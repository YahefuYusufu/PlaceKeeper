package com.example.placeKeeper.di
import android.content.Context
import androidx.room.Room
import com.example.placeKeeper.data.PlaceDatabase
import com.example.placeKeeper.data.dao.CategoryDao
import com.example.placeKeeper.data.dao.PlaceDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providePlaceDatabase(
        @ApplicationContext context: Context
    ): PlaceDatabase {
        return Room.databaseBuilder(
            context,
            PlaceDatabase::class.java,
            "place_database"
        ).build()
    }

    @Provides
    fun providePlaceDao(database: PlaceDatabase): PlaceDao {
        return database.placeDao()
    }

    @Provides
    fun provideCategoryDao(database: PlaceDatabase): CategoryDao {
        return database.categoryDao()
    }
}