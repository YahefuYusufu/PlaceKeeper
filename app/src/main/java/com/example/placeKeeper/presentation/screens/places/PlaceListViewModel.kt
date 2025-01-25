package com.example.placeKeeper.presentation.screens.places


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.placeKeeper.domain.model.Place
import com.example.placeKeeper.domain.repository.PlaceRepository
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


sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String?) : UiState<Nothing>()
}

@HiltViewModel
class PlaceListViewModel @Inject constructor(
    private val repository: PlaceRepository
): ViewModel() {
    private val _places = MutableStateFlow<UiState<List<Place>>>(UiState.Loading)
    val places = _places.asStateFlow()

    init {
        loadPlaces()
    }

    private fun loadPlaces() {
        viewModelScope.launch {
            repository.getAllPlaces()
                .flowOn(Dispatchers.IO)
                .catch { _places.value = UiState.Error(it.message) }
                .collect { places ->
                    _places.value = UiState.Success(places)
                }
        }
    }

    fun toggleFavorite(placeId: Long) {
        viewModelScope.launch {
            repository.toggleFavorite(placeId)
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