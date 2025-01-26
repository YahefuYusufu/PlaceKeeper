package com.example.placeKeeper.presentation.screens.savedPlaces

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.placeKeeper.domain.model.Place
 import com.example.placeKeeper.presentation.screens.places.UiState
import com.example.placeKeeper.presentation.screens.places.components.PlaceCard


@Composable
fun SavedPlaceScreen(
    viewModel: SavedPlaceViewModel = hiltViewModel(),

) {
    val uiState by viewModel.savedPlaces.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Saved Places",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )

        when (uiState) {
            is UiState.Loading -> CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            is UiState.Error -> ErrorView(message = (uiState as UiState.Error).message)
            is UiState.Success -> {
                val places = (uiState as UiState.Success<List<Place>>).data
                LazyColumn(
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(places) { place ->
                        PlaceCard(
                            place = place,
                            onDelete = viewModel::deletePlace,
                            onFavoriteToggle = viewModel::toggleFavorite,
                            isFavorite = viewModel.isFavorite(place.id)
                                .collectAsStateWithLifecycle()
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun ErrorView(message: String?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Error,
            contentDescription = "Error",
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message ?: "Unknown error",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}
