package com.example.placeKeeper.presentation.screens.addplace.components

import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.placeKeeper.presentation.screens.addplace.AddPlaceInputState
import com.example.placeKeeper.presentation.screens.addplace.AddPlaceViewModel
import com.example.placeKeeper.presentation.screens.addplace.LocationEvent
import com.example.placeKeeper.utils.PermissionUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
 import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
 import androidx.compose.runtime.setValue
import com.google.maps.android.compose.DragState
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberMarkerState

@SuppressLint("DefaultLocale")
@Composable
fun LocationSection(
    state: AddPlaceInputState,
    onLocationEvent: (LocationEvent) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddPlaceViewModel = hiltViewModel()
) {
    val locationPermissionGranted by viewModel.locationPermissionGranted.collectAsState()
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val areGranted = permissions.values.all { it }
        viewModel.updateLocationPermissionStatus(areGranted)
        if (areGranted) {
            onLocationEvent(LocationEvent.RequestCurrentLocation)
        }
    }

    val selectedPosition = if (state.latitude != 0.0 && state.longitude != 0.0) {
        LatLng(state.latitude, state.longitude)
    } else {
        LatLng(48.8566, 2.3522) // Paris coordinates
    }

    val markerState = rememberMarkerState(position = selectedPosition)

    // Camera position state
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.Builder()
            .target(selectedPosition)
            .zoom(12f)
            .build()
    }

    // Update marker position when state changes
    LaunchedEffect(state.latitude, state.longitude) {
        if (state.latitude != 0.0 && state.longitude != 0.0) {
            markerState.position = LatLng(state.latitude, state.longitude)
        }
    }

    // Update location when marker position changes
    LaunchedEffect(markerState.position) {
        if (markerState.position != selectedPosition) {
            onLocationEvent(
                LocationEvent.UpdateLocation(
                    markerState.position.latitude,
                    markerState.position.longitude
                )
            )
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(16.dp))
                .shadow(4.dp),
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 2.dp
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(
                        isMyLocationEnabled = locationPermissionGranted,
                        mapType = MapType.NORMAL,
                        isIndoorEnabled = true
                    ),
                    uiSettings = MapUiSettings(
                        zoomControlsEnabled = true,
                        zoomGesturesEnabled = true,
                        scrollGesturesEnabled = true,
                        scrollGesturesEnabledDuringRotateOrZoom = true,
                        rotationGesturesEnabled = true,
                        tiltGesturesEnabled = true,
                        compassEnabled = true,
                        mapToolbarEnabled = true
                    ),
                    onMapClick = { latLng ->
                        markerState.position = latLng
                    }
                ) {
                    // Main draggable marker
                    Marker(
                        state = markerState,
                        title = "Selected Location",
                        snippet = "Drag to adjust location",
                        draggable = true
                    )
                }

                // Location button
                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .background(
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                            RoundedCornerShape(8.dp)
                        ),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(
                        onClick = {
                            if (!locationPermissionGranted) {
                                locationPermissionLauncher.launch(PermissionUtils.REQUIRED_PERMISSIONS)
                            } else {
                                onLocationEvent(LocationEvent.RequestCurrentLocation)
                            }
                        }
                    ) {
                        Icon(
                            Icons.Default.MyLocation,
                            contentDescription = "Get Current Location",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // Instructions text
                Text(
                    text = "Click and drag the marker to set location",
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 16.dp)
                        .background(
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        if (state.latitude != 0.0 && state.longitude != 0.0) {
            Text(
                text = "📍 ${String.format("%.6f", state.latitude)}, ${
                    String.format(
                        "%.6f",
                        state.longitude
                    )
                }",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        state.locationError?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}