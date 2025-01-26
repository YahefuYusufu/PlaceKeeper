package com.example.placeKeeper.presentation.screens.savedPlaces

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.placeKeeper.domain.model.Place
import com.example.placeKeeper.domain.repository.PlaceRepository
import com.example.placeKeeper.presentation.screens.places.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SavedPlaceViewModel @Inject constructor(
    private val repository: PlaceRepository
) : ViewModel() {
    private val _savedPlaces = MutableStateFlow<UiState<List<Place>>>(UiState.Loading)
    val savedPlaces = _savedPlaces.asStateFlow()

    init {
        loadSavedPlaces()
    }

    private fun loadSavedPlaces() {
        viewModelScope.launch {
            repository.getFavoritePlaces()
                .flowOn(Dispatchers.IO)
                .catch { _savedPlaces.value = UiState.Error(it.message) }
                .collect { places ->
                    _savedPlaces.value = UiState.Success(places)
                }
        }
    }

    fun deletePlace(place: Place) {
        viewModelScope.launch {
            repository.deletePlace(place)
        }
    }

    fun toggleFavorite(placeId: Long) {
        viewModelScope.launch {
            try {
                Log.d("SavedPlaceViewModel", "Toggling favorite for place $placeId")
                repository.toggleFavorite(placeId)
                Log.d("SavedPlaceViewModel", "Successfully toggled favorite")
            } catch (e: Exception) {
                Log.e("SavedPlaceViewModel", "Error toggling favorite: ${e.message}", e)
            }
        }
    }

    val isFavorite = { placeId: Long ->
        repository.isPlaceFavorite(placeId).stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            false
        )
    }
}