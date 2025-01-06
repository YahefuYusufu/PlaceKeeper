package com.example.placeKeeper.presentation.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.placeKeeper.presentation.components.map.MapTypeSelector
import com.example.placeKeeper.presentation.theme.MapStyles
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    var showBottomSheet by remember { mutableStateOf(false) }
    var mapType by remember { mutableStateOf(MapType.NORMAL) }
    val defaultLocation = LatLng(-33.8688, 151.2093)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 12f)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                mapType = mapType,
                isMyLocationEnabled = false,  // We'll enable this later with permissions
                mapStyleOptions = MapStyles.getMapStyleOptions()
            ),
            uiSettings = MapUiSettings(
                zoomControlsEnabled = true,  // Hide default zoom controls
                mapToolbarEnabled = true,    // Hide default toolbar
                myLocationButtonEnabled = true, // Hide default location button
                compassEnabled = true
            )
        ) {
            Marker(
                state = MarkerState(position = defaultLocation),
                title = "Default Location",
                onClick = {
                    showBottomSheet = true
                    true
                }
            )
        }

        // Map controls in better position
        MapTypeSelector(
            modifier = Modifier
                .align(androidx.compose.ui.Alignment.BottomStart)
                .padding(bottom = 32.dp, start = 16.dp),
            onMapTypeSelect = { newType ->
                mapType = newType
            }
        )

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = rememberModalBottomSheetState(),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Location Details",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Add details about this location",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}