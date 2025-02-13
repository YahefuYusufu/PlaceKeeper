package com.example.placeKeeper.presentation.navigation

import android.util.Log
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.placeKeeper.presentation.screens.addplace.AddPlaceScreen
import com.example.placeKeeper.presentation.screens.addcategory.AddCategoryScreen
import com.example.placeKeeper.presentation.screens.categories.CategoriesScreen
import com.example.placeKeeper.presentation.screens.home.HomeScreen
import com.example.placeKeeper.presentation.screens.places.PlacesListScreen
import com.example.placeKeeper.presentation.screens.places.PlacesScreen
import com.example.placeKeeper.presentation.screens.savedPlaces.SavedPlaceScreen

fun NavGraphBuilder.placeKeeperGraph(
    navController: NavHostController
) {
    // Home Screen
    composable(route = NavigationItem.Home.route) {
        HomeScreen()
    }

    // Categories Screen and Related Routes
    composable(route = NavigationItem.Categories.route) {
        CategoriesScreen(
            navigateToAddCategory = {
                navController.navigate(Routes.ADD_CATEGORY)
            },
            navigateToPlaces = { categoryId ->
                navController.navigate(Routes.placesRoute(categoryId))
            }
        )
    }

    composable(route = Routes.ADD_CATEGORY) {
        AddCategoryScreen(
            onNavigateBack = {
                navController.popBackStack()
            }
        )
    }

    composable(
        route = Routes.PLACES,
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
                navController.navigate(Routes.ADD_PLACE)
            }
        )
    }

    // Places List Screen
    composable(route = NavigationItem.PlacesList.route) {
        PlacesListScreen()
    }

    // Add Place Screen
    composable(route = Routes.ADD_PLACE) {
        AddPlaceScreen(
            onPlaceSaved = { placeId ->
                if (placeId > 0) {
                    Log.d("Navigation", "Place saved successfully with ID: $placeId")
                }
                navController.popBackStack()
            }
        )
    }

    // Saved Places Screen
    composable(route = NavigationItem.Saved.route) {
        SavedPlaceScreen()
    }
}

// Define route constants for better maintainability
object Routes {
    const val HOME = "home"
    const val CATEGORIES = "categories"
    const val PLACES_LIST = "places_list"
    const val SAVED = "saved"
    const val ADD_CATEGORY = "add_category"
    const val ADD_PLACE = "add_place"
    const val PLACES = "places/{categoryId}"

    fun placesRoute(categoryId: Long) = "places/$categoryId"
}