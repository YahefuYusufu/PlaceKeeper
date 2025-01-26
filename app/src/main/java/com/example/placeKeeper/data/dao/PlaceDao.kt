package com.example.placeKeeper.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.placeKeeper.data.entities.FavoritePlaceEntity
import com.example.placeKeeper.data.entities.PlaceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaceDao {
    @Query("SELECT * FROM place")
    fun getAllPlaces(): Flow<List<PlaceEntity>>

    @Query("SELECT * FROM place WHERE category_id = :categoryId")
    fun getPlacesByCategory(categoryId: Long): Flow<List<PlaceEntity>>

    @Query("SELECT * FROM place WHERE id = :placeId")
    suspend fun getPlaceById(placeId: Long): PlaceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlace(place: PlaceEntity): Long

    @Update
    suspend fun updatePlace(place: PlaceEntity)

    @Delete
    suspend fun deletePlace(place: PlaceEntity)

    @Query("SELECT * FROM place WHERE name LIKE '%' || :query || '%'")
    fun searchPlaces(query: String): Flow<List<PlaceEntity>>

    // Updated favorite queries
    @Query("SELECT EXISTS(SELECT 1 FROM favorite_places WHERE placeId = :placeId)")
    fun isPlaceFavorite(placeId: Long): Flow<Boolean>

    @Insert
    suspend fun addToFavorites(favorite: FavoritePlaceEntity)

    @Query("DELETE FROM favorite_places WHERE placeId = :placeId")
    suspend fun removeFromFavorites(placeId: Long)

    @Query("SELECT p.* FROM place p INNER JOIN favorite_places fp ON p.id = fp.placeId")
    fun getFavoritePlaces(): Flow<List<PlaceEntity>>

}