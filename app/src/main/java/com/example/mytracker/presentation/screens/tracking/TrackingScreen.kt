package com.example.mytracker.presentation.screens.tracking

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.example.mytracker.presentation.theme.MapStyles

@Composable
fun TrackingScreen() {
    // Test with Sydney coordinates
    val sydney = LatLng(-33.8688, 151.2093)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(sydney, 12f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            mapStyleOptions = MapStyles.getMapStyleOptions()
        ),
        uiSettings = MapUiSettings(
            zoomControlsEnabled = true,
            compassEnabled = true
        )
    ) {
        // Add a test marker
        Marker(
            state = MarkerState(position = sydney),
            title = "Sydney"
        )
    }
}