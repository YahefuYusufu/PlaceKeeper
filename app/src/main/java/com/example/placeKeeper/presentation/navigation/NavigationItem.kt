package com.example.placeKeeper.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Map
import androidx.compose.ui.graphics.vector.ImageVector


sealed class NavigationItem (
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home: NavigationItem(
        route = "home",
        title = "Map",
        icon = Icons.Default.Map
    )
    object Categories : NavigationItem(
        route = "categories",
        title = "Categories",
        icon = Icons.Default.Category
    )
    object PlacesList : NavigationItem(
        route = "places_list",
        title = "Places",
        icon = Icons.AutoMirrored.Filled.List
    )
}