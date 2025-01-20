package com.example.placeKeeper.presentation.screens.categories.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryTopBar(
    isGridView: Boolean,
    onToggleView: () -> Unit
) {
    TopAppBar(
        title = { Text("Categories") },
        actions = {
            IconButton(onClick = onToggleView) {
                Icon(
                    if (isGridView) Icons.AutoMirrored.Filled.ViewList
                    else Icons.Default.GridView,
                    contentDescription = "View Toggle"
                )
            }
        }
    )
}