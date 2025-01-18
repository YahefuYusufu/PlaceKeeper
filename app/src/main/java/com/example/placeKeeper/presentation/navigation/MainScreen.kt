package com.example.placeKeeper.presentation.navigation

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.placeKeeper.presentation.screens.categories.AddCategoryScreen
import com.example.placeKeeper.presentation.screens.categories.CategoriesScreen
import com.example.placeKeeper.presentation.screens.home.HomeScreen
import com.example.placeKeeper.presentation.screens.places.PlacesListScreen
import com.example.placeKeeper.presentation.screens.places.PlacesScreen
import com.example.placeKeeper.presentation.screens.savedPlaces.SavedScreen
import com.example.placeKeeper.utils.NavigationUtils.navigateToTab

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.Categories,
        NavigationItem.PlacesList,
        NavigationItem.Saved
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                NavigationBar(
                    modifier = Modifier.height(90.dp),
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    tonalElevation = 2.dp
                ) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    // First two items (Map and Categories)
                    items.take(2).forEach { item ->
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

                    // Empty middle slot for FAB
                    NavigationBarItem(
                        icon = { Box(modifier = Modifier.size(56.dp)) },
                        label = { },
                        selected = false,
                        onClick = { },
                        enabled = false,
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.Transparent
                        )
                    )

                    // Last two items (Places and Saved)
                    items.takeLast(2).forEach { item ->
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
                // Your existing routes...
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
                composable(route = NavigationItem.Saved.route) {
                    SavedScreen()
                }
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
                        onAddPlace = {
                            // We'll implement this later
                            // navController.navigate("add_place/$id")
                        }
                    )
                }
                composable(route = "add_category") {
                    AddCategoryScreen(
                        onNavigateBack = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }

        // Add button floating above everything
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-45).dp)
                .zIndex(1f)
        ) {
            AddButton(
                onClick = { /* Handle add click */ },
                modifier = Modifier
            )
        }
    }
}

@Composable
fun AddButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        modifier = modifier
            .size(56.dp)
            .shadow(
                elevation = 6.dp,
                shape = CircleShape,
                ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
            )
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add",
            modifier = Modifier.size(24.dp)
        )
    }
}