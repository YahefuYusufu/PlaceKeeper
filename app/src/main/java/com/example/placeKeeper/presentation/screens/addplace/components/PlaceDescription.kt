package com.example.placeKeeper.presentation.screens.addplace.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PlaceDescription(
    description: String,
    onDescriptionChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = description,
        onValueChange = onDescriptionChange,
        label = { Text("Description (Optional)") },
        modifier = modifier,
        minLines = 4,
        maxLines = 6,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
        )
    )
}