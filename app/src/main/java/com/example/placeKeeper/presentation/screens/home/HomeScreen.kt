package com.example.placeKeeper.presentation.screens.home

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.placeKeeper.domain.model.Place
import com.example.placeKeeper.presentation.components.map.MapTypeSelector
import com.example.placeKeeper.presentation.screens.home.components.LocationBottomSheet
import com.example.placeKeeper.presentation.screens.home.components.LocationButton
import com.example.placeKeeper.presentation.screens.home.components.MapControls
import com.example.placeKeeper.presentation.screens.home.components.MapMarkers
import com.example.placeKeeper.presentation.screens.home.components.PlaceSearchBar
import com.example.placeKeeper.presentation.screens.places.PlaceListViewModel
import com.example.placeKeeper.presentation.screens.places.UiState
import com.example.placeKeeper.presentation.theme.MapStyles
import com.example.placeKeeper.utils.LocationHandler
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    viewModel: PlaceListViewModel = hiltViewModel(),
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    var mapType by remember { mutableStateOf(MapType.NORMAL) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedPlace by remember { mutableStateOf<Place?>(null) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val locationHandler = remember { LocationHandler(context) }
    val placesState by viewModel.places.collectAsState()
    val cameraPositionState = rememberCameraPositionState()


    fun updateToUserLocation() {
        scope.launch {
            try {
                val location = locationHandler.getCurrentLocation()
                location?.let {
                    cameraPositionState.animate(
                        CameraUpdateFactory.newLatLngZoom(it, 15f),
                        1000
                    )
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Unable to get location", Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(placesState) {
        when (placesState) {
            is UiState.Success -> {
                val places = (placesState as UiState.Success<List<Place>>).data
                if (places.isNotEmpty()) {
                    val bounds = LatLngBounds.Builder().apply {
                        places.forEach { place ->
                            include(LatLng(place.latitude, place.longitude))
                        }
                    }.build()

                    cameraPositionState.move(
                        CameraUpdateFactory.newLatLngBounds(bounds, 100)
                    )
                } else {
                    // If no places, show user's location
                    updateToUserLocation()
                }
            }
            else -> {} // Handle other states if needed
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                mapType = mapType,
                isMyLocationEnabled = true,
                mapStyleOptions = MapStyles.getMapStyleOptions()
            ),
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,
                mapToolbarEnabled = false,
                myLocationButtonEnabled = false,
                compassEnabled = true
            )
        ) {
            when (placesState) {
                is UiState.Success -> {
                    val places = (placesState as UiState.Success<List<Place>>).data
                    places.forEach { place ->
                        Marker(
                            state = MarkerState(position = LatLng(place.latitude, place.longitude)),
                            title = place.name,
                            snippet = place.description.takeIf { it.isNotEmpty() }
                                ?: "No description",
                            icon = BitmapDescriptorFactory.defaultMarker(
                                MapMarkers.getMarkerIcon(place.categoryId)
                            ),
                            onClick = {
                                selectedPlace = place
                                showBottomSheet = true
                                true
                            }
                        )
                    }
                }

                else -> {}
            }
        }

        // Search Bar
        PlaceSearchBar(
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it },
            modifier = Modifier.align(Alignment.TopCenter)
        )

        // Map Controls
        MapControls(
            onZoomIn = {
                cameraPositionState.position = CameraPosition.fromLatLngZoom(
                    cameraPositionState.position.target,
                    cameraPositionState.position.zoom + 1f
                )
            },
            onZoomOut = {
                cameraPositionState.position = CameraPosition.fromLatLngZoom(
                    cameraPositionState.position.target,
                    cameraPositionState.position.zoom - 1f
                )
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 75.dp, end = 16.dp)
        )

        // Map Type Selector
        MapTypeSelector(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(bottom = 88.dp, start = 16.dp)
                .shadow(elevation = 4.dp, shape = RoundedCornerShape(12.dp)),
            onMapTypeSelect = { newType -> mapType = newType }
        )

        // Location Button
        LocationButton(
            onClick = { updateToUserLocation() },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 88.dp, end = 16.dp)
        )

        // Loading and Error States
        when (placesState) {
            is UiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            is UiState.Error -> {
                Text(
                    text = (placesState as UiState.Error).message ?: "Unknown error occurred",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            }

            else -> {}
        }

        // Bottom Sheet
        if (showBottomSheet && selectedPlace != null) {
            LocationBottomSheet(
                onDismiss = {
                    showBottomSheet = false
                    selectedPlace = null
                },
                place = selectedPlace!!,
                onDirectionsClick = {
                    // Open Google Maps with directions
                    val uri =
                        Uri.parse("google.navigation:q=${selectedPlace!!.latitude},${selectedPlace!!.longitude}")
                    val mapIntent = Intent(Intent.ACTION_VIEW, uri).apply {
                    }
                    try {
                        context.startActivity(mapIntent)
                    } catch (e: ActivityNotFoundException) {
                        Toast.makeText(
                            context,
                            "Google Maps app is not installed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                onDeleteClick = {
                    viewModel.deletePlace(selectedPlace!!)
                    showBottomSheet = false
                    selectedPlace = null
                },
                onShareClick = {
                    // Share location
                    val shareIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(
                            Intent.EXTRA_TEXT,
                            "Check out ${selectedPlace!!.name} at " +
                                    "https://www.google.com/maps/search/?api=1&query=${selectedPlace!!.latitude},${selectedPlace!!.longitude}"
                        )
                        type = "text/plain"
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share location"))
                }
            )
        }


    }
}