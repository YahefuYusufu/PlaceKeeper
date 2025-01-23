package com.example.placeKeeper.presentation.screens.addplace

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.placeKeeper.domain.location.LocationManager
import com.example.placeKeeper.domain.model.Category
import com.example.placeKeeper.domain.repository.CategoryRepository
import com.example.placeKeeper.domain.repository.PlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPlaceViewModel @Inject constructor(
    private val placeRepository: PlaceRepository,
    private val categoryRepository: CategoryRepository,
    private val locationManager: LocationManager
) : ViewModel() {
    private val TAG = "AddPlaceViewModel"

    private val _inputState = MutableStateFlow(AddPlaceInputState())
    val inputState = _inputState.asStateFlow()

    private val _uiState = MutableStateFlow<AddPlaceUiState>(AddPlaceUiState.Initial)
    val uiState = _uiState.asStateFlow()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories = _categories.asStateFlow()

    val locationPermissionGranted = locationManager.locationPermissionGranted

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            try {
                // Collect the Flow into a List
                categoryRepository.getAllCategories()
                    .collect { categoriesList ->
                        _categories.value = categoriesList
                    }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading categories", e)
                _uiState.value = AddPlaceUiState.Error("Failed to load categories")
            }
        }
    }

    fun onLocationEvent(event: LocationEvent) {
        when (event) {
            is LocationEvent.ShowLocationDialog -> {
                _inputState.update { it.copy(isLocationDialogVisible = true) }
            }

            is LocationEvent.HideLocationDialog -> {
                _inputState.update {
                    it.copy(
                        isLocationDialogVisible = false,
                        locationError = null
                    )
                }
            }

            is LocationEvent.UpdateLocation -> {
                updateLocation(event.latitude, event.longitude)
            }

            is LocationEvent.RequestCurrentLocation -> {
                getCurrentLocation()
            }

            is LocationEvent.ClearLocationError -> {
                _inputState.update { it.copy(locationError = null) }
            }
        }
    }

    fun updateLocationPermissionStatus(granted: Boolean) {
        locationManager.updatePermissionStatus(granted)
    }

    private fun getCurrentLocation() {
        viewModelScope.launch {
            try {
                // Add log to check if this function is being called
                Log.d(TAG, "Requesting current location...")

                // Check if permission is granted first
                if (!locationManager.locationPermissionGranted.value) {
                    Log.d(TAG, "Location permission not granted")
                    _inputState.update {
                        it.copy(locationError = "Location permission not granted")
                    }
                    return@launch
                }

                locationManager.getCurrentLocation()
                    .onSuccess { location ->
                        Log.d(TAG, "Location received: ${location.latitude}, ${location.longitude}")
                        updateLocation(location.latitude, location.longitude)
                    }
                    .onFailure { exception ->
                        Log.e(TAG, "Failed to get location", exception)
                        _inputState.update {
                            it.copy(locationError = exception.message ?: "Failed to get location")
                        }
                    }
            } catch (e: Exception) {
                Log.e(TAG, "Error getting location", e)
                _inputState.update {
                    it.copy(locationError = e.message ?: "Failed to get location")
                }
            }
        }
    }

    fun updateName(name: String) {
        _inputState.update { it.copy(name = name) }
        resetErrorIfAny()
    }

    private fun updateLocation(latitude: Double, longitude: Double) {
        val currentState = _inputState.value
        if (!currentState.isValidLatitude() || !currentState.isValidLongitude()) {
            _inputState.update {
                it.copy(locationError = "Invalid coordinates")
            }
            return
        }

        _inputState.update {
            it.copy(
                latitude = latitude,
                longitude = longitude,
                locationError = null
            )
        }
        resetErrorIfAny()
    }

    fun updateCategory(categoryId: Long) {
        _inputState.update { it.copy(categoryId = categoryId) }
    }

    fun updateDescription(description: String) {
        _inputState.update { it.copy(description = description) }
    }

    fun updateRating(rating: Float) {
        _inputState.update { it.copy(rating = rating) }
    }

    fun addPlace() {
        val currentState = _inputState.value

        if (!validateInputs(currentState)) {
            return
        }

        viewModelScope.launch {
            try {
                // Detailed logging of all properties before saving
                Log.d(TAG, """
                Saving Place with details:
                ID: ${currentState.id}
                Name: ${currentState.name}
                Location: (${currentState.latitude}, ${currentState.longitude})
                Category ID: ${currentState.categoryId}
                Description: ${currentState.description}
                Rating: ${currentState.rating}
                Dialog Visible: ${currentState.isLocationDialogVisible}
                Location Error: ${currentState.locationError}
                """.trimIndent()
                )

                _uiState.value = AddPlaceUiState.Loading

                val place = currentState.toPlace()
                val placeId = placeRepository.insertPlace(place)

                if (placeId > 0) {
                    // Log successful save with the new ID
                    Log.d(TAG, """
                    Successfully saved place:
                    New ID: $placeId
                    Name: ${currentState.name}
                    Category: ${currentState.categoryId}
                    Location: (${currentState.latitude}, ${currentState.longitude})
                    Time: ${System.currentTimeMillis()}
                    """.trimIndent()
                    )
                    _uiState.value = AddPlaceUiState.Success(placeId)
                } else {
                    Log.e(TAG, "Failed to add place - invalid ID returned")
                    _uiState.value = AddPlaceUiState.Error("Failed to add place")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error adding place", e)
                // Log the error details
                Log.e(TAG, """
                Failed to save place with details:
                Name: ${currentState.name}
                Error: ${e.message}
                Stack trace: ${e.stackTraceToString()}
                """.trimIndent()
                )
                _uiState.value = AddPlaceUiState.Error(
                    e.message ?: "Unknown error occurred"
                )
            }
        }
    }

    private fun validateInputs(state: AddPlaceInputState): Boolean {
        val validationError = state.getValidationError()
        if (validationError != null) {
            _uiState.value = AddPlaceUiState.Error(validationError)
            return false
        }
        return true
    }


    private fun resetErrorIfAny() {
        if (_uiState.value is AddPlaceUiState.Error) {
            _uiState.value = AddPlaceUiState.Initial
        }
    }

    fun resetState() {
        _inputState.value = AddPlaceInputState()
        _uiState.value = AddPlaceUiState.Initial
    }
}