package com.example.placeKeeper.presentation.screens.addplace.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp

@Composable
fun AddPlaceButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.inversePrimary,
        contentColor = MaterialTheme.colorScheme.primary,
        modifier = modifier
            .size(64.dp)
            .shadow(
                elevation = 6.dp,
                shape = CircleShape,
                ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
            )
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add",

            modifier = Modifier.size(28.dp)
        )
    }
}