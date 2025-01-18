package com.example.placeKeeper.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Place
import androidx.compose.ui.graphics.vector.ImageVector


sealed class NavigationItem (
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Home: NavigationItem(
        route = "home",
        title = "Map",
        icon = Icons.Default.Place
    )
    data object Categories : NavigationItem(
        route = "categories",
        title = "Categories",
        icon = Icons.Default.Category
    )
    data object PlacesList : NavigationItem(
        route = "places_list",
        title = "Places",
        icon = Icons.AutoMirrored.Filled.List
    )
    data object Saved: NavigationItem(
        route = "saved",
        title = "Saved",
        icon = Icons.Default.Favorite
    )
}