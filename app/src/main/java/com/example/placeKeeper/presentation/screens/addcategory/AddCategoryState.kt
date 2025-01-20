package com.example.placeKeeper.presentation.screens.addcategory

import com.example.placeKeeper.domain.model.Category
import com.example.placeKeeper.utils.CategoryConstants

data class CategoryInputState(
    val id: Long = 0,
    val name: String = "",
    val color: String = CategoryConstants.AVAILABLE_COLORS[0].hex,
    val iconName: String = CategoryConstants.AVAILABLE_ICONS[0].iconName
)

// UI State for operations
sealed class CategoryUiState {
    data object Initial : CategoryUiState()
    data object Loading : CategoryUiState()
    data class Error(val message: String) : CategoryUiState()
    data class Success(val categoryId: Long) : CategoryUiState()
}

// Convert to Category entity
fun CategoryInputState.toCategory() = Category(
    id = id,
    name = name.trim(),
    color = color,
    iconName = iconName,
    createdAt = System.currentTimeMillis()
)