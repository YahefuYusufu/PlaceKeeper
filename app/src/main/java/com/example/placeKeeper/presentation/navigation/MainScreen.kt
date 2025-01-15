package com.example.placeKeeper.presentation.navigation

import android.util.Log
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.placeKeeper.presentation.screens.categories.AddCategoryScreen
import com.example.placeKeeper.presentation.screens.categories.CategoriesScreen
import com.example.placeKeeper.presentation.screens.home.HomeScreen
import com.example.placeKeeper.presentation.screens.places.PlacesListScreen
import com.example.placeKeeper.presentation.screens.places.PlacesScreen
import com.example.placeKeeper.utils.NavigationUtils.navigateToTab


@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.Categories,
        NavigationItem.PlacesList
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                modifier = Modifier.height(90.dp),
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                tonalElevation = 2.dp
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                items.forEach { item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                modifier = Modifier.size(28.dp)
                            )
                        },
                        label = {
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = if (currentRoute == item.route)
                                    FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        selected = currentRoute == item.route,
                        onClick = { navController.navigateToTab(item.route) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NavigationItem.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(route = NavigationItem.Home.route) {
                HomeScreen()
            }
            composable(route = NavigationItem.Categories.route) {
                CategoriesScreen(
                    navigateToAddCategory = {
                        Log.d("Navigation", "Attempting to navigate to add category")
                        try {
                            navController.navigate("add_category")
                        } catch (e: Exception) {
                            Log.e("Navigation", "Navigation failed", e)
                        }
                    },
                    navigateToPlaces = { categoryId ->
                        Log.d("Navigation", "Attempting to navigate to places with id: $categoryId")
                        try {
                            navController.navigate("places/$categoryId")
                        } catch (e: Exception) {
                            Log.e("Navigation", "Navigation failed", e)
                        }
                    }
                )
            }

            composable(route = NavigationItem.PlacesList.route) {
                PlacesListScreen()
            }

            // Add these new routes
            composable(
                route = "places/{categoryId}",
                arguments = listOf(
                    navArgument("categoryId") { type = NavType.LongType }
                )
            ) { backStackEntry ->
                val categoryId = backStackEntry.arguments?.getLong("categoryId") ?: return@composable
                PlacesScreen(
                    categoryId = categoryId,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onAddPlace = { id ->
                        // We'll implement this later
                        // navController.navigate("add_place/$id")
                    }
                )
            }
            composable(route = "add_category") {
                AddCategoryScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },

                )
            }
        }
    }
}