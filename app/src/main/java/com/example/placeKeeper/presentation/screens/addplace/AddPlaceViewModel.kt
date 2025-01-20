package com.example.placeKeeper.presentation.screens.addplace

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.placeKeeper.domain.model.Category
import com.example.placeKeeper.domain.repository.CategoryRepository
import com.example.placeKeeper.domain.repository.PlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPlaceViewModel @Inject constructor(
    private val placeRepository: PlaceRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    private val TAG = "AddPlaceViewModel"

    private val _inputState = MutableStateFlow(AddPlaceInputState())
    val inputState = _inputState.asStateFlow()

    private val _uiState = MutableStateFlow<AddPlaceUiState>(AddPlaceUiState.Initial)
    val uiState = _uiState.asStateFlow()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories = _categories.asStateFlow()

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

                        // Set first category as default if available and no category is selected
                        if (_inputState.value.categoryId == 0L) {
                            categoriesList.firstOrNull()?.let { category ->
                                _inputState.update { it.copy(categoryId = category.id) }
                            }
                        }
                    }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading categories", e)
                _uiState.value = AddPlaceUiState.Error("Failed to load categories")
            }
        }
    }

    fun updateName(name: String) {
        _inputState.update { it.copy(name = name) }
        resetErrorIfAny()
    }

    fun updateLocation(latitude: Double, longitude: Double) {
        _inputState.update {
            it.copy(
                latitude = latitude,
                longitude = longitude
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
                Log.d(TAG, "Adding new place: ${currentState.name}")
                _uiState.value = AddPlaceUiState.Loading

                val place = currentState.toPlace()
                val placeId = placeRepository.insertPlace(place)

                if (placeId > 0) {
                    Log.d(TAG, "Successfully added place with ID: $placeId")
                    _uiState.value = AddPlaceUiState.Success(placeId)
                } else {
                    Log.e(TAG, "Failed to add place - invalid ID returned")
                    _uiState.value = AddPlaceUiState.Error("Failed to add place")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error adding place", e)
                _uiState.value = AddPlaceUiState.Error(
                    e.message ?: "Unknown error occurred"
                )
            }
        }
    }

    private fun validateInputs(state: AddPlaceInputState): Boolean {
        when {
            state.name.length < 3 -> {
                _uiState.value = AddPlaceUiState.Error("Name must be at least 3 characters")
                return false
            }
            state.categoryId == 0L -> {
                _uiState.value = AddPlaceUiState.Error("Please select a category")
                return false
            }
            state.latitude == 0.0 && state.longitude == 0.0 -> {
                _uiState.value = AddPlaceUiState.Error("Please select a location")
                return false
            }
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