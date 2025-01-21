package com.example.placeKeeper.presentation.screens.addplace

import com.example.placeKeeper.domain.model.Place

data class AddPlaceInputState(
    val id: Long = 0,
    val name: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val categoryId: Long = 0,
    val description: String = "",
    val rating: Float = 0f,
    //   location-specific states
    val isLocationDialogVisible: Boolean = false,
    val locationError: String? = null
) {
    // Add validation functions
    fun isValidLatitude() = latitude in -90.0..90.0
    fun isValidLongitude() = longitude in -180.0..180.0
    fun hasValidLocation() = latitude != 0.0 && longitude != 0.0 &&
            isValidLatitude() && isValidLongitude()

    fun hasValidName() = name.trim().length >= 3
    fun hasValidCategory() = categoryId > 0
    fun hasValidRating() = rating in 0f..5f

    // Check if all required fields are valid
    fun isValid() = hasValidName() && hasValidLocation() &&
            hasValidCategory() && hasValidRating()
}

// Expand location-related events
sealed class LocationEvent {
    data object ShowLocationDialog : LocationEvent()
    data object HideLocationDialog : LocationEvent()
    data class UpdateLocation(val latitude: Double, val longitude: Double) : LocationEvent()
    data object RequestCurrentLocation : LocationEvent()
    data object ClearLocationError : LocationEvent()
}

sealed class AddPlaceUiState {
    data object Initial : AddPlaceUiState()
    data object Loading : AddPlaceUiState()
    data class Success(val placeId: Long) : AddPlaceUiState()
    data class Error(val message: String) : AddPlaceUiState()
}

// Enhanced helper function with validation
fun AddPlaceInputState.toPlace(): Place {
    require(isValid()) { "Invalid place data" }

    return Place(
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
}

// Add extension function for validation messages
fun AddPlaceInputState.getValidationError(): String? {
    return when {
        !hasValidName() -> "Name must be at least 3 characters"
        !hasValidLocation() -> "Invalid location coordinates"
        !hasValidCategory() -> "Please select a category"
        !hasValidRating() -> "Rating must be between 0 and 5"
        else -> null
    }
}
