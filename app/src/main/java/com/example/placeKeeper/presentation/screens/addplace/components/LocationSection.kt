package com.example.placeKeeper.presentation.screens.addplace.components

import android.annotation.SuppressLint
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
 import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
 import androidx.compose.material3.Text
 import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
 import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
 import com.example.placeKeeper.presentation.screens.addplace.AddPlaceInputState
import com.example.placeKeeper.presentation.screens.addplace.AddPlaceViewModel
import com.example.placeKeeper.presentation.screens.addplace.LocationEvent
import com.example.placeKeeper.utils.PermissionUtils
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
 import androidx.compose.material3.FloatingActionButton
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.ripple.rememberRipple
 import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings


@SuppressLint("DefaultLocale")
@Composable
fun LocationSection(
    state: AddPlaceInputState,
    onLocationEvent: (LocationEvent) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddPlaceViewModel = hiltViewModel()
) {
    var isExpanded by remember { mutableStateOf(false) }
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
        LatLng(-6.200000, 106.816666) // Jakarta as default
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(selectedPosition, 12f)
    }

    // Track the last position to detect changes
    val lastPosition = remember { mutableStateOf(selectedPosition) }

    // Center map when location changes or view state changes
    LaunchedEffect(state.latitude, state.longitude, isExpanded) {
        if (state.latitude != 0.0 && state.longitude != 0.0) {
            val newPosition = LatLng(state.latitude, state.longitude)
            // Only animate if position actually changed
            if (newPosition != lastPosition.value) {
                lastPosition.value = newPosition
                val zoomLevel = if (isExpanded) 15f else 17f
                cameraPositionState.animate(
                    update = CameraUpdateFactory.newCameraPosition(
                        CameraPosition.builder()
                            .target(newPosition)
                            .zoom(zoomLevel)
                            .build()
                    ),
                    durationMs = 750  // Slightly longer animation for smoother movement
                )
            }
        }
    }




    if (isExpanded) {
        // Expanded map dialog
        Dialog(
            onDismissRequest = { isExpanded = false },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = false
            )
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.9f)    // Slightly smaller width
                    .fillMaxHeight(0.7f)
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(16.dp),
                        spotColor = MaterialTheme.colorScheme.outline
                    ),
                shape = RoundedCornerShape(16.dp),
                tonalElevation = 2.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    MapContent(
                        state = state,
                        onLocationEvent = onLocationEvent,
                        locationPermissionGranted = locationPermissionGranted,
                        locationPermissionLauncher = locationPermissionLauncher,
                        showZoomControls = true,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Close button
                    IconButton(
                        onClick = { isExpanded = false },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Close Map",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }

    // Compact map view
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(12.dp))
                .shadow(
                    elevation = 2.dp,
                    shape = RoundedCornerShape(12.dp),
                    spotColor = MaterialTheme.colorScheme.outline
                )
                .hoverable(interactionSource = remember { MutableInteractionSource() })
                .indication(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = true)
                )
        ) {
            MapContent(
                state = state,
                onLocationEvent = onLocationEvent,
                locationPermissionGranted = locationPermissionGranted,
                locationPermissionLauncher = locationPermissionLauncher,
                showZoomControls = false,
                modifier = Modifier.fillMaxSize()
            )

            // Expand button
            IconButton(
                onClick = { isExpanded = true },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                Icon(
                    Icons.Default.Fullscreen,
                    contentDescription = "Expand Map",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        // Location coordinates if available
        if (state.latitude != 0.0 && state.longitude != 0.0) {
            Text(
                text = "Location: ${String.format("%.6f", state.latitude)}, ${String.format("%.6f", state.longitude)}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        // Error message if any
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

@Composable
private fun MapContent(
    state: AddPlaceInputState,
    onLocationEvent: (LocationEvent) -> Unit,
    locationPermissionGranted: Boolean,
    locationPermissionLauncher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>,
    showZoomControls: Boolean,
    modifier: Modifier = Modifier
) {
    val selectedPosition = if (state.latitude != 0.0 && state.longitude != 0.0) {
        LatLng(state.latitude, state.longitude)
    } else {
        LatLng(-6.200000, 106.816666) // Jakarta as default
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(selectedPosition, 12f)
    }

    Box(modifier = modifier) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                isMyLocationEnabled = locationPermissionGranted,
                mapType = MapType.NORMAL
            ),
            uiSettings = MapUiSettings(
                zoomControlsEnabled = showZoomControls,
                myLocationButtonEnabled = false,
                zoomGesturesEnabled = true,
                scrollGesturesEnabled = true,
                rotationGesturesEnabled = true,
                tiltGesturesEnabled = true,
                compassEnabled = true,
                scrollGesturesEnabledDuringRotateOrZoom = true,
            ),
            onMapClick = { latLng ->
                onLocationEvent(LocationEvent.UpdateLocation(latLng.latitude, latLng.longitude))
            }
        ) {
            if (state.latitude != 0.0 && state.longitude != 0.0) {
                Marker(
                    state = MarkerState(position = selectedPosition),
                    title = "Selected Location"
                )
            }
        }

        // Current Location FAB
        FloatingActionButton(
            onClick = {
                if (!locationPermissionGranted) {
                    locationPermissionLauncher.launch(PermissionUtils.REQUIRED_PERMISSIONS)
                } else {
                    onLocationEvent(LocationEvent.RequestCurrentLocation)
                }
            },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp),
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ) {
            Icon(
                Icons.Default.MyLocation,
                contentDescription = "Get Current Location"
            )
        }
    }
}