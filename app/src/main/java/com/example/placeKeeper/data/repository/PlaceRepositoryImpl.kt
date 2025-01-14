package com.example.placeKeeper.data.repository

import com.example.placeKeeper.data.dao.PlaceDao
import com.example.placeKeeper.data.mappers.toPlace
import com.example.placeKeeper.data.mappers.toPlaceEntity
import com.example.placeKeeper.domain.model.Place
import com.example.placeKeeper.domain.repository.PlaceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlaceRepositoryImpl @Inject constructor(
    private val placeDao: PlaceDao
) : PlaceRepository {

    override fun getAllPlaces(): Flow<List<Place>> {
        return placeDao.getAllPlaces().map { entities ->
            entities.map { it.toPlace() }
        }
    }

    override fun getPlacesByCategory(categoryId: Long): Flow<List<Place>> {
        return placeDao.getPlacesByCategory(categoryId).map { entities ->
            entities.map { it.toPlace() }
        }
    }

    override suspend fun getPlaceById(placeId: Long): Place? {
        return placeDao.getPlaceById(placeId)?.toPlace()
    }

    override suspend fun insertPlace(place: Place): Long {
        return placeDao.insertPlace(place.toPlaceEntity())
    }

    override suspend fun updatePlace(place: Place) {
        placeDao.updatePlace(place.toPlaceEntity())
    }

    override suspend fun deletePlace(place: Place) {
        placeDao.deletePlace(place.toPlaceEntity())
    }

    override fun searchPlaces(query: String): Flow<List<Place>> {
        return placeDao.searchPlaces(query).map { entities ->
            entities.map { it.toPlace() }
        }
    }
}