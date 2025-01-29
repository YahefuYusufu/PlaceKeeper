package com.example.placeKeeper.presentation.screens.home.components

import android.annotation.SuppressLint
import androidx.compose.ui.Modifier
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.placeKeeper.utils.CameraUtils.zoomIn
import com.example.placeKeeper.utils.CameraUtils.zoomOut
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch

@SuppressLint("DefaultLocale")
@Composable
fun EnhancedMapControls(
    cameraPositionState: CameraPositionState,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .width(48.dp)
            .padding(4.dp)
    ) {
        IconButton(
            onClick = {
                val zoomed = cameraPositionState.zoomIn()
                if (!zoomed) {
                    scope.launch {
                        Toast.makeText(context, "Maximum zoom reached", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.primary,
                    RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                )
                .size(40.dp)
        ) {
            Icon(
                Icons.Rounded.Add,
                "Zoom in",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

        Spacer(modifier = Modifier.height(1.dp))

        // Current Zoom Level Indicator
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary)
                .size(40.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = String.format("%.0fx", cameraPositionState.position.zoom),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        Spacer(modifier = Modifier.height(1.dp))

        IconButton(
            onClick = {
                val zoomed = cameraPositionState.zoomOut()
                if (!zoomed) {
                    scope.launch {
                        Toast.makeText(context, "Minimum zoom reached", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.primary,
                    RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
                )
                .size(40.dp)
        ) {
            Icon(
                Icons.Rounded.Remove,
                "Zoom out",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}