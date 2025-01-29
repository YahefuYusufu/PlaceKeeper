package com.example.placeKeeper.presentation.screens.home.components

import androidx.compose.material.icons.rounded.MyLocation
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun LocationButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.surfaceContainer
    ) {
        Icon(Icons.Rounded.MyLocation, "My Location")
    }
}
