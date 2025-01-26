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
import com.example.placeKeeper.utils.MapUtils
import com.example.placeKeeper.utils.PermissionUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState


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
        LatLng(-6.200000, 106.816666)
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.Builder()
            .target(selectedPosition)
            .zoom(8f)
            .bearing(0f)
            .tilt(0f)
            .build()
    }

    LaunchedEffect(state.latitude, state.longitude) {
        if (state.latitude != 0.0 && state.longitude != 0.0) {
            val newPosition = LatLng(state.latitude, state.longitude)
            cameraPositionState.animate(
                update = CameraUpdateFactory.newCameraPosition(
                    CameraPosition.Builder()
                        .target(newPosition)
                        .zoom(12f)
                        .bearing(0f)
                        .tilt(0f)
                        .build()
                ),
                durationMs = 500
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
                    properties = MapUtils.defaultProperties(locationPermissionGranted),
                    uiSettings = MapUtils.defaultUiSettings(),
                    onMapClick = { latLng ->
                        onLocationEvent(
                            LocationEvent.UpdateLocation(
                                latLng.latitude,
                                latLng.longitude
                            )
                        )
                    },
                    onPOIClick = { poi ->
                        onLocationEvent(
                            LocationEvent.UpdateLocation(
                                poi.latLng.latitude,
                                poi.latLng.longitude
                            )
                        )
                    }
                ) {
                    if (state.latitude != 0.0 && state.longitude != 0.0) {
                        Marker(
                            state = MarkerState(position = selectedPosition),
                            title = "Selected Location"
                        )
                    }
                }

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


