package com.example.placeKeeper.presentation.screens.addplace.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PlaceNameInput(
    name: String,
    onNameChange: (String) -> Unit,
    isError: Boolean,
    errorMessage: String?,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = name,
        onValueChange = onNameChange,
        label = { Text("Place Name") },
        modifier = modifier.fillMaxWidth(),
        isError = isError,
        supportingText = {
            if (isError && errorMessage != null) {
                Text(errorMessage)
            } else {
                Text("${name.length}/10")
            }
        },
        singleLine = true
    )
}