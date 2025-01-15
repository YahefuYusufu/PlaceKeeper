package com.example.placeKeeper.presentation.screens.categories


import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.placeKeeper.domain.model.Category
import com.example.placeKeeper.presentation.screens.categories.components.CategoryGrid
import com.example.placeKeeper.presentation.screens.categories.components.CategoryList


@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun CategoriesScreen(
    navigateToAddCategory: () -> Unit,
    navigateToPlaces: (Long) -> Unit,

) {
    var isGridView by remember { mutableStateOf(true) }

    // Mock data
    val mockCategories = remember {
        listOf(
            Category(
                id = 1L,
                name = "Restaurants",
                color = "#FF4081",
                iconName = "restaurant",
                createdAt = System.currentTimeMillis()
            ),
            Category(
                id = 2L,
                name = "Parks",
                color = "#00C853",
                iconName = "park",
                createdAt = System.currentTimeMillis()
            ),
            Category(
                id = 3L,
                name = "Museums",
                color = "#FF6E40",
                iconName = "museum",
                createdAt = System.currentTimeMillis()
            ),
            Category(
                id = 4L,
                name = "Cafes",
                color = "#6200EA",
                iconName = "cafe",
                createdAt = System.currentTimeMillis()
            ),
            Category(
                id = 5L,
                name = "Shopping",
                color = "#2962FF",
                iconName = "shopping",
                createdAt = System.currentTimeMillis()
            )
        )
    }
    LaunchedEffect(Unit) {
        Log.d("CategoriesScreen", "Screen launched")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Categories") },
                actions = {
                    IconButton(onClick = { isGridView = !isGridView }) {
                        Icon(
                            if (isGridView) Icons.AutoMirrored.Filled.ViewList
                            else Icons.Default.GridView,
                            contentDescription = "View Toggle"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = navigateToAddCategory) {
                Icon(Icons.Default.Add, "Add Category")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            if (mockCategories.isEmpty()) {
                Text(
                    text = "No categories available",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            } else {
                if (isGridView) {
                    CategoryGrid(
                        modifier = Modifier.fillMaxSize(),
                        categories = mockCategories,
                        onCategoryClick = navigateToPlaces
                    )
                } else {
                    CategoryList(
                        modifier = Modifier.fillMaxSize(),
                        categories = mockCategories,
                        onCategoryClick = navigateToPlaces
                    )
                }
            }
        }
    }
}




