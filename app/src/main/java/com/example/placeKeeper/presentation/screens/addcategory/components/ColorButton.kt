package com.example.placeKeeper.presentation.screens.addcategory.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.placeKeeper.utils.ColorOption

@Composable
  fun ColorButton(
    colorOption: ColorOption,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val defaultColor = MaterialTheme.colorScheme.primary
    val buttonColor = remember(colorOption.hex) {
        try {
            Color(android.graphics.Color.parseColor(colorOption.hex))
        } catch (e: IllegalArgumentException) {
            defaultColor
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            onClick = onClick,
            color = buttonColor,
            shape = MaterialTheme.shapes.small,
            border = if (isSelected) {
                BorderStroke(2.dp, MaterialTheme.colorScheme.onSurface)
            } else null,
            modifier = Modifier.size(48.dp)
        ) { }
        if (isSelected) {
            Text(
                text = colorOption.name,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}