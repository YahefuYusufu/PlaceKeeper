package com.example.placeKeeper.presentation


import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import com.example.placeKeeper.presentation.navigation.MainScreen
import com.example.placeKeeper.presentation.screens.permissions.PermissionsScreen
import com.example.placeKeeper.presentation.theme.PlaceKeeperTheme
import com.example.placeKeeper.utils.PermissionUtils
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        Log.d("MainActivity", "Activity created")
        setContent {
            PlaceKeeperTheme {
                var hasPermissions by remember {
                    mutableStateOf(PermissionUtils.hasLocationPermissions(this))
                }

                Surface(modifier = Modifier.fillMaxSize()) {
                    if (hasPermissions) {
                        MainScreen()
                    } else {
                        PermissionsScreen(
                            onPermissionsGranted = {
                                hasPermissions = true
                            }
                        )
                    }
                }
            }
        }
    }
}

