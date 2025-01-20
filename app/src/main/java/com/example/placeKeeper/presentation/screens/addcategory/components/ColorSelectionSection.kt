package com.example.placeKeeper.presentation.screens.addcategory.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
 import com.example.placeKeeper.utils.CategoryConstants

@Composable
 fun ColorSelectionSection(
    selectedColor: String,
    onColorSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            "Select Color",
            style = MaterialTheme.typography.titleMedium
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(top = 8.dp)
        ) {
            items(CategoryConstants.AVAILABLE_COLORS) { colorOption ->
                ColorButton(
                    colorOption = colorOption,
                    isSelected = colorOption.hex == selectedColor,
                    onClick = { onColorSelected(colorOption.hex) }
                )
            }
        }
    }
}