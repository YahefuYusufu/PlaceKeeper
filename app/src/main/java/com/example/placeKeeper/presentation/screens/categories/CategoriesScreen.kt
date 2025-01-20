package com.example.placeKeeper.presentation.screens.categories

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.placeKeeper.presentation.screens.categories.components.*

@Composable
fun CategoriesScreen(
    navigateToAddCategory: () -> Unit,
    navigateToPlaces: (Long) -> Unit,
    viewModel: CategoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        Log.d("CategoriesScreen", "Screen launched")
    }

    Scaffold(
        topBar = {
            CategoryTopBar(
                isGridView = uiState.isGridView,
                onToggleView = viewModel::toggleViewMode
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToAddCategory,
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                Icon(Icons.Default.Add, "Add Category")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            CategoryContent(
                uiState = uiState,
                onCategoryClick = navigateToPlaces,
                onDeleteClick = viewModel::showDeleteConfirmation,
                onRetry = viewModel::clearError,
                onAddCategory = navigateToAddCategory
            )
        }

        uiState.categoryToDelete?.let { category ->
            DeleteConfirmationDialog(
                categoryName = category.name,
                onConfirm = viewModel::deleteCategory,
                onDismiss = viewModel::dismissDeleteConfirmation
            )
        }
    }
}