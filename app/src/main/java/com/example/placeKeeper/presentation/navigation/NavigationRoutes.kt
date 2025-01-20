package com.example.placeKeeper.presentation.navigation

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
import com.example.placeKeeper.presentation.screens.savedPlaces.SavedScreen

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
                navController.navigate("add_category")
            },
            navigateToPlaces = { categoryId ->
                navController.navigate("places/$categoryId")
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
                navController.navigate("add_place")
            }
        )
    }

    // Places List Screen
    composable(route = NavigationItem.PlacesList.route) {
        PlacesListScreen()
    }

    // Add Place Screen
    composable(route = "add_place") {
        AddPlaceScreen(
            onNavigateBack = {
                navController.popBackStack()
            },

        )
    }

    // Saved Places Screen
    composable(route = NavigationItem.Saved.route) {
        SavedScreen()
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

    // Helper function to create places route with categoryId
    fun placesRoute(categoryId: Long) = "places/$categoryId"
}