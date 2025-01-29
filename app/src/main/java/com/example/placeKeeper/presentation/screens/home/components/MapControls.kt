package com.example.placeKeeper.presentation.screens.home.components

import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MapControls(
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
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
                onClick = onZoomIn,
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
                onClick = onZoomOut,
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
}