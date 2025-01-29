package com.example.placeKeeper.presentation.screens.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.rounded.Directions
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material.icons.rounded.Share

@Composable
fun BottomSheetActions() {
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
}

