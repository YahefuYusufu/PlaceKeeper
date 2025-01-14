package com.example.placeKeeper.domain.repository

import com.example.placeKeeper.domain.model.Place
import kotlinx.coroutines.flow.Flow

interface PlaceRepository {
    fun getAllPlaces(): Flow<List<Place>>
    fun getPlacesByCategory(categoryId: Long): Flow<List<Place>>
    suspend fun getPlaceById(placeId: Long): Place?
    suspend fun insertPlace(place: Place): Long
    suspend fun updatePlace(place: Place)
    suspend fun deletePlace(place: Place)
    fun searchPlaces(query: String): Flow<List<Place>>
}