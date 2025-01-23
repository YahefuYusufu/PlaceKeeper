package com.example.placeKeeper.presentation.screens.savedPlaces

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.placeKeeper.domain.model.Place
import com.example.placeKeeper.domain.repository.PlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SavedPlaceViewModel @Inject constructor(
    private val repository: PlaceRepository

): ViewModel() {
    private val _places = MutableStateFlow<List<Place>>(emptyList())
    val places = _places.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllPlaces().collect {places ->
                _places.value = places
            }
        }
    }
}
