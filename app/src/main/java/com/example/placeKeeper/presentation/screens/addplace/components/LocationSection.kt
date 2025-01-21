package com.example.placeKeeper.presentation.screens.addplace.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts


@Composable
fun LocationSection(
    latitude: Double,
    longitude: Double,
    onLocationSelected: (Double, Double) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddPlaceViewModel = hiltViewModel()
) {
    var showLocationDialog by remember { mutableStateOf(false) }
    val locationPermissionGranted by viewModel.locationPermissionGranted.collectAsState()

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val areGranted = permissions.values.all { it }
        viewModel.updateLocationPermissionStatus(areGranted)
        if (areGranted) {
            viewModel.getCurrentLocation()
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

        // Location display
        if (latitude != 0.0 && longitude != 0.0) {
            Text(
                text = "Selected: ${String.format("%.6f", latitude)}, ${String.format("%.6f", longitude)}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
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
                        viewModel.getCurrentLocation()
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
                onClick = { showLocationDialog = true },
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
        if (showLocationDialog) {
            LocationInputDialog(
                initialLatitude = latitude,
                initialLongitude = longitude,
                onLocationSelected = { lat, lng ->
                    onLocationSelected(lat, lng)
                    showLocationDialog = false
                },
                onDismiss = { showLocationDialog = false }
            )
        }
    }
}

@Composable
private fun LocationInputDialog(
    initialLatitude: Double,
    initialLongitude: Double,
    onLocationSelected: (Double, Double) -> Unit,
    onDismiss: () -> Unit
) {
    var latitudeText by remember { mutableStateOf(if (initialLatitude != 0.0) initialLatitude.toString() else "") }
    var longitudeText by remember { mutableStateOf(if (initialLongitude != 0.0) initialLongitude.toString() else "") }
    var latitudeError by remember { mutableStateOf<String?>(null) }
    var longitudeError by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Enter Location") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = latitudeText,
                    onValueChange = {
                        latitudeText = it
                        latitudeError = null
                    },
                    label = { Text("Latitude") },
                    isError = latitudeError != null,
                    supportingText = latitudeError?.let { { Text(it) } },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true
                )

                OutlinedTextField(
                    value = longitudeText,
                    onValueChange = {
                        longitudeText = it
                        longitudeError = null
                    },
                    label = { Text("Longitude") },
                    isError = longitudeError != null,
                    supportingText = longitudeError?.let { { Text(it) } },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val lat = latitudeText.toDoubleOrNull()
                    val lng = longitudeText.toDoubleOrNull()

                    when {
                        lat == null -> latitudeError = "Invalid latitude"
                        lng == null -> longitudeError = "Invalid longitude"
                        !LocationManager.isValidLatitude(lat) ->
                            latitudeError = "Latitude must be between -90 and 90"
                        !LocationManager.isValidLongitude(lng) ->
                            longitudeError = "Longitude must be between -180 and 180"
                        else -> onLocationSelected(lat, lng)
                    }
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}