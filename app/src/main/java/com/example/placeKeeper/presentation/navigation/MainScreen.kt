package com.example.placeKeeper.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.placeKeeper.presentation.navigation.components.PlaceKeeperBottomNavigation
import com.example.placeKeeper.presentation.screens.addplace.AddPlaceBottomSheet
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
    var showAddPlaceSheet by remember { mutableStateOf(false) }


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
                        .offset(y = (-58).dp)
                        .zIndex(1f)
                ) {
                    AddPlaceButton(
                        onClick = { showAddPlaceSheet = true }
                    )
                }
            }
        }

        // Bottom Sheet
        if (showAddPlaceSheet) {
            AddPlaceBottomSheet(
                onDismiss = { showAddPlaceSheet = false }
            )
        }
    }
}

