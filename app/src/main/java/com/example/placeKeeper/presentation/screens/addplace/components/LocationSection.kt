package com.example.placeKeeper.presentation.screens.addplace.components

import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
 import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import com.google.android.gms.maps.CameraUpdateFactory


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

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            "Location",
            style = MaterialTheme.typography.titleMedium
        )

        // Show selected location if available
        if (state.latitude != 0.0 && state.longitude != 0.0) {
            Text(
                text = "Selected: ${String.format("%.6f", state.latitude)}, ${String.format("%.6f", state.longitude)}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Location error message if any
        state.locationError?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        // Location selection buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = {
                    if (!locationPermissionGranted) {
                        locationPermissionLauncher.launch(PermissionUtils.REQUIRED_PERMISSIONS)
                    } else {
                        onLocationEvent(LocationEvent.RequestCurrentLocation)
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    Icons.Default.MyLocation,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text("Current Location")
            }

            OutlinedButton(
                onClick = { onLocationEvent(LocationEvent.ShowLocationDialog) },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text("Enter Manually")
            }
        }

        // Manual location input dialog
        if (state.isLocationDialogVisible) {
            LocationInputDialog(
                initialLatitude = state.latitude,
                initialLongitude = state.longitude,
                onLocationSelected = { lat, lng ->
                    onLocationEvent(LocationEvent.UpdateLocation(lat, lng))
                    onLocationEvent(LocationEvent.HideLocationDialog)
                },
                onDismiss = { onLocationEvent(LocationEvent.HideLocationDialog) }
            )
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
private fun LocationInputDialog(
    initialLatitude: Double,
    initialLongitude: Double,
    onLocationSelected: (Double, Double) -> Unit,
    onDismiss: () -> Unit,
    viewModel: AddPlaceViewModel = hiltViewModel()
) {

    val inputState by viewModel.inputState.collectAsState()
    val locationError = inputState.locationError
    var selectedPosition by remember {
        mutableStateOf(
            if (initialLatitude != 0.0 && initialLongitude != 0.0)
                LatLng(initialLatitude, initialLongitude)
            else
                LatLng(-6.200000, 106.816666) // Jakarta as default location
        )
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(selectedPosition, 12f)
    }

    LaunchedEffect(inputState.latitude, inputState.longitude) {
        if (inputState.latitude != 0.0 && inputState.longitude != 0.0) {
            val newPosition = LatLng(inputState.latitude, inputState.longitude)
            selectedPosition = newPosition
            // Animate camera to new position
            cameraPositionState.animate(
                CameraUpdateFactory.newCameraPosition(
                    CameraPosition.fromLatLngZoom(newPosition, 15f)
                )
            )
        }
    }



    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.8f),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title
                Text(
                    "Select Location",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Map
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState,
                        onMapClick = { latLng ->
                            selectedPosition = latLng
                        }
                    ) {
                        Marker(
                            state = MarkerState(position = selectedPosition),
                            title = "Selected Location"
                        )
                    }



                    // Current Location FAB
                    FloatingActionButton(
                        onClick = {
                            // Request current location
                            viewModel.onLocationEvent(LocationEvent.RequestCurrentLocation)
                        },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp),
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Icon(
                            Icons.Default.MyLocation,
                            contentDescription = "Get Current Location"
                        )
                    }
                    locationError?.let { error ->
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                // Coordinates display
                Text(
                    text = "Selected Position: ${String.format("%.6f", selectedPosition.latitude)}, " +
                            String.format("%.6f", selectedPosition.longitude),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            onLocationSelected(
                                selectedPosition.latitude,
                                selectedPosition.longitude
                            )
                        }
                    ) {
                        Text("Select")
                    }
                }
            }
        }
    }
}