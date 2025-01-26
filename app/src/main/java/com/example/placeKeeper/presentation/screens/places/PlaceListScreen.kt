package com.example.placeKeeper.presentation.screens.places

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.placeKeeper.domain.model.Place
import com.example.placeKeeper.presentation.screens.places.components.PlaceCard
import com.example.placeKeeper.presentation.screens.savedPlaces.SavedPlaceViewModel

@Composable
fun PlacesListScreen(
    viewModel: PlaceListViewModel = hiltViewModel(),
    savedViewModel: SavedPlaceViewModel = hiltViewModel(),

    ) {
    val uiState by viewModel.places.collectAsStateWithLifecycle()
    var selectedView by remember { mutableStateOf(ViewType.GRID) }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                            MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f),
                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)
                        )
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Places",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.5.sp,
                    color = MaterialTheme.colorScheme.primary,
                    shadow = Shadow(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                        offset = Offset(2f, 2f),
                        blurRadius = 3f
                    )
                )
            )
            Row(
                modifier = Modifier
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                            )
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(4.dp)
            ) {
                IconButton(
                    onClick = { selectedView = ViewType.LIST },
                    modifier = Modifier
                        .background(
                            brush = if (selectedView == ViewType.LIST)
                                Brush.linearGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)
                                    )
                                )
                            else Brush.linearGradient(listOf(Color.Transparent, Color.Transparent)),
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ViewList,
                        contentDescription = "List view",
                        tint = if (selectedView == ViewType.LIST)
                            MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
                IconButton(
                    onClick = { selectedView = ViewType.GRID },
                    modifier = Modifier
                        .background(
                            brush = if (selectedView == ViewType.GRID)
                                Brush.linearGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)
                                    )
                                )
                            else Brush.linearGradient(listOf(Color.Transparent, Color.Transparent)),
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.GridView,
                        contentDescription = "Grid view",
                        tint = if (selectedView == ViewType.GRID)
                            MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }
        }

        when (uiState) {
            is UiState.Loading -> CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            is UiState.Error -> ErrorView(message = (uiState as UiState.Error).message)
            is UiState.Success -> {
                val places =
                    (uiState as UiState.Success<List<Place>>).data.sortedByDescending { it.createdAt }
                when (selectedView) {
                    ViewType.GRID -> PlacesGrid(
                        places = places,
                        onDelete = viewModel::deletePlace,
                        onFavoriteToggle = savedViewModel::toggleFavorite,
                        getIsFavorite = { id -> savedViewModel.isFavorite(id).collectAsStateWithLifecycle() }
                    )
                    ViewType.LIST -> PlacesList(
                        places = places,
                        onDelete = viewModel::deletePlace,
                        onFavoriteToggle = savedViewModel::toggleFavorite,
                        getIsFavorite = { id -> savedViewModel.isFavorite(id).collectAsStateWithLifecycle() }
                    )
                }
            }
        }
    }
}

@Composable
private fun PlacesGrid(
    places: List<Place>,
    onDelete: (Place) -> Unit,
    onFavoriteToggle: (Long) -> Unit,
    getIsFavorite: @Composable (Long) -> State<Boolean>
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(places) { place ->
            PlaceCard(
                place = place,
                onDelete = onDelete,
                onFavoriteToggle = onFavoriteToggle,
                isFavorite = getIsFavorite(place.id)
            )
        }
    }
}

@Composable
private fun PlacesList(
    places: List<Place>,
    onDelete: (Place) -> Unit,
    onFavoriteToggle: (Long) -> Unit,
    getIsFavorite: @Composable (Long) -> State<Boolean>
) {
    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(places) { place ->
            PlaceCard(
                place = place,
                onDelete = onDelete,
                onFavoriteToggle = onFavoriteToggle,
                isFavorite = getIsFavorite(place.id)
            )
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

private enum class ViewType {
    LIST, GRID
}