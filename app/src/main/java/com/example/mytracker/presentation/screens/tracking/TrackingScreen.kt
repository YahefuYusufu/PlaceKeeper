package com.example.mytracker.presentation.screens.tracking

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.example.mytracker.presentation.components.MapTypeSelector
import com.example.mytracker.presentation.theme.MapStyles

@Composable
fun TrackingScreen() {
    var mapType by remember { mutableStateOf(MapType.NORMAL) }
    val sydney = LatLng(-33.8688, 151.2093)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(sydney, 12f)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                mapType = mapType,
                mapStyleOptions = MapStyles.getMapStyleOptions()
            ),
            uiSettings = MapUiSettings(
                zoomControlsEnabled = true,
                compassEnabled = true
            )
        ) {
            Marker(
                state = MarkerState(position = sydney),
                title = "Sydney"
            )
        }

        MapTypeSelector(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(bottom = 22.dp, start = 8.dp),
            onMapTypeSelect = { newType ->
                mapType = newType
            }
        )
    }
}