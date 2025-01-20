package com.example.placeKeeper.presentation.screens.addplace

import com.example.placeKeeper.domain.model.Place

data class AddPlaceInputState(
    val id: Long = 0,
    val name: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val categoryId: Long = 0,
    val description: String = "",
    val rating: Float = 0f
)

sealed class AddPlaceUiState {
    data object Initial : AddPlaceUiState()
    data object Loading : AddPlaceUiState()
    data class Success(val placeId: Long) : AddPlaceUiState()
    data class Error(val message: String) : AddPlaceUiState()
}

// Helper function to convert input state to Place entity
fun AddPlaceInputState.toPlace() = Place(
    id = id,
    name = name.trim(),
    latitude = latitude,
    longitude = longitude,
    categoryId = categoryId,
    description = description.trim(),
    rating = rating,
    createdAt = System.currentTimeMillis(),
    updatedAt = System.currentTimeMillis()
)