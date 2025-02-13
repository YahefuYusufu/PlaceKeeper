package com.example.placeKeeper.presentation.screens.categories.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.placeKeeper.domain.model.Category




@Composable
fun CategoryList(
    modifier: Modifier = Modifier,
    onCategoryClick: (Long) -> Unit,
    onDeleteClick: (Category) -> Unit,
    categories: List<Category>
) {


    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            CategoryListItem(
                category = category,
                onClick = { onCategoryClick(category.id) },
                onDeleteClick = { onDeleteClick(category) }

            )
        }
    }
}