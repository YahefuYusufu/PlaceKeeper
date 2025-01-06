package com.example.placeKeeper.utils


import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination

object NavigationUtils {
    // Animation constants
    val enterTransition = fadeIn(animationSpec = tween(300)) +
            slideInHorizontally(
                initialOffsetX = { 300 },
                animationSpec = tween(300)
            )

    val exitTransition = fadeOut(animationSpec = tween(300)) +
            slideOutHorizontally(
                targetOffsetX = { -300 },
                animationSpec = tween(300)
            )

    // Navigation routes
    object Routes {
        const val HOME = "home"
        const val CATEGORIES = "categories"
        const val PLACES_LIST = "places_list"
    }

    // Navigation helper functions
    fun NavController.navigateToTab(route: String) {
        navigate(route) {
            popUpTo(graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}