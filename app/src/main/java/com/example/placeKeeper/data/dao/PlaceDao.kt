package com.example.placeKeeper.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.placeKeeper.data.entities.PlaceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaceDao {
    @Query("SELECT * FROM place")
    fun getAllLocations(): Flow<List<PlaceEntity>>

    @Query("SELECT * FROM place WHERE category_id = :categoryId")
    fun getLocationsByCategory(categoryId: Long): Flow<List<PlaceEntity>>

    @Query("SELECT * FROM place WHERE id = :locationId")
    suspend fun getLocationById(locationId: Long): PlaceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: PlaceEntity): Long

    @Update
    suspend fun updateLocation(location: PlaceEntity)

    @Delete
    suspend fun deleteLocation(location: PlaceEntity)

    @Query("SELECT * FROM place WHERE name LIKE '%' || :query || '%'")
    fun searchLocations(query: String): Flow<List<PlaceEntity>>
}