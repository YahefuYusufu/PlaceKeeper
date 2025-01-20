package com.example.placeKeeper.presentation.screens.categories

import com.example.placeKeeper.domain.model.Category

data class CategoryUiState(
    val categories: List<Category> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isGridView: Boolean = true,
    val categoryToDelete: Category? = null
)