package com.example.placeKeeper.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Directions
import androidx.compose.material.icons.rounded.MyLocation
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
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
    var searchQuery by remember { mutableStateOf("") }
    val defaultLocation = LatLng(-33.8688, 151.2093)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 12f)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Main Map
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
            Marker(
                state = MarkerState(position = defaultLocation),
                title = "Default Location",
                onClick = {
                    showBottomSheet = true
                    true
                }
            )
        }

        // Floating Search Bar
        Surface(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(56.dp)
                .align(Alignment.TopCenter),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.primary,
            tonalElevation = 6.dp,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.primaryContainer
                )
                Spacer(modifier = Modifier.width(8.dp))
                BasicTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    textStyle = TextStyle(MaterialTheme.colorScheme.primaryContainer),
                    modifier = Modifier.weight(1f),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (searchQuery.isEmpty()) {
                                Text(
                                    "Search places...",
                                    color = MaterialTheme.colorScheme.primaryContainer,

                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }
        }

        // Map Controls Card
        Surface(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 75.dp, end = 16.dp),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.primary,
            tonalElevation = 4.dp
        ) {
            Column(
                modifier = Modifier
                    .width(48.dp)
                    .padding(4.dp)
            ) {
                IconButton(
                    onClick = {
                        cameraPositionState.position = CameraPosition.fromLatLngZoom(
                            cameraPositionState.position.target,
                            cameraPositionState.position.zoom + 1f
                        )
                    },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        Icons.Rounded.Add,
                        "Zoom in",
                        modifier = Modifier.size(24.dp)
                    )
                }
                HorizontalDivider(modifier = Modifier.padding(horizontal = 8.dp))
                IconButton(
                    onClick = {
                        cameraPositionState.position = CameraPosition.fromLatLngZoom(
                            cameraPositionState.position.target,
                            cameraPositionState.position.zoom - 1f
                        )
                    },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        Icons.Rounded.Remove,
                        "Zoom out",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // Enhanced Map Type Selector
        MapTypeSelector(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(bottom =  88.dp, start = 16.dp)
                .shadow(elevation = 4.dp, shape = RoundedCornerShape(12.dp)),
            onMapTypeSelect = { newType -> mapType = newType }
        )

        // Location FAB
        FloatingActionButton(
            onClick = { /* Handle my location */ },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 88.dp, end = 16.dp),
            containerColor = MaterialTheme.colorScheme.
            primary,
            contentColor = MaterialTheme.colorScheme.primaryContainer
        ) {
            Icon(Icons.Rounded.MyLocation, "My Location")
        }

        // Bottom Sheet
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = rememberModalBottomSheetState(),
                dragHandle = { BottomSheetDefaults.DragHandle() }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Location Details",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Default Location",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    // Add your location details here
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ActionButton(
                            icon = Icons.Rounded.Directions,
                            text = "Directions"
                        )
                        ActionButton(
                            icon = Icons.Rounded.Save,
                            text = "Save"
                        )
                        ActionButton(
                            icon = Icons.Rounded.Share,
                            text = "Share"
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun ActionButton(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.secondaryContainer,
                    CircleShape
                )
                .padding(8.dp)
        ) {
            Icon(
                icon,
                contentDescription = text,
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}