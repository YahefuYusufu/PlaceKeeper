package com.example.mytracker.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import com.google.maps.android.compose.MapType

@Composable
fun MapTypeSelector(
    modifier: Modifier = Modifier,
    onMapTypeSelect: (MapType) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        IconButton(
            onClick = { expanded = true },
            modifier = Modifier
                .shadow(
                    elevation = 6.dp,
                    shape = CircleShape,
                    spotColor = Color.Black.copy(alpha = 0.25f)
                )
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.85f))
                .padding(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Layers,
                contentDescription = "Change map type",
                tint = MaterialTheme.colorScheme.primary
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            MapType.entries.forEach { type ->
                DropdownMenuItem(
                    text = { Text(
                        type.name,
                        color = MaterialTheme.colorScheme.onSurface
                    ) },
                    onClick = {
                        onMapTypeSelect(type)
                        expanded = false
                    }
                )
            }
        }
    }
}