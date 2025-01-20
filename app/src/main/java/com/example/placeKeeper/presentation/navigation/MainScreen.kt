package com.example.placeKeeper.presentation.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.compose.*
import com.example.placeKeeper.presentation.navigation.components.PlaceKeeperBottomNavigation
import com.example.placeKeeper.presentation.screens.addplace.components.AddPlaceButton

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navigationItems = listOf(
        NavigationItem.Home,
        NavigationItem.Categories,
        NavigationItem.PlacesList,
        NavigationItem.Saved
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { paddingValues ->
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                NavHost(
                    navController = navController,
                    startDestination = Routes.HOME,
                    modifier = Modifier.padding(paddingValues)
                ) {
                    placeKeeperGraph(navController)
                }

                // Bottom Navigation
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                ) {
                    PlaceKeeperBottomNavigation(
                        navController = navController,
                        items = navigationItems
                    )
                }

                // Add Button
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .offset(y = (-48).dp)
                        .zIndex(1f)
                ) {
                    AddPlaceButton(
                        onClick = {
                            navController.navigate(Routes.ADD_PLACE)
                        }
                    )
                }
            }
        }
    }
}