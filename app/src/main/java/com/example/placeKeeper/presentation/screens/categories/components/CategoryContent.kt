package com.example.placeKeeper.presentation.screens.categories.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon.Companion.Text
import androidx.compose.ui.unit.dp
import com.example.placeKeeper.domain.model.Category
import com.example.placeKeeper.presentation.screens.categories.CategoryUiState


@Composable
fun CategoryContent(
    uiState: CategoryUiState,
    onCategoryClick: (Long) -> Unit,
    onDeleteClick: (Category) -> Unit,
    onRetry: () -> Unit,
    onAddCategory: () -> Unit
) {
    when {
        uiState.isLoading -> CategoryLoadingIndicator()
        uiState.error != null -> CategoryErrorContent(
            error = uiState.error,
            onRetry = onRetry
        )
        uiState.categories.isEmpty() -> CategoryEmptyContent(onAddClick = onAddCategory)
        else -> {
            if (uiState.isGridView) {
                CategoryGrid(
                    modifier = Modifier.fillMaxSize(),
                    categories = uiState.categories,
                    onCategoryClick = onCategoryClick,
                    onDeleteClick = onDeleteClick
                )
            } else {
                CategoryList(
                    modifier = Modifier.fillMaxSize(),
                    categories = uiState.categories,
                    onCategoryClick = onCategoryClick,
                    onDeleteClick = onDeleteClick
                )
            }
        }
    }
}

