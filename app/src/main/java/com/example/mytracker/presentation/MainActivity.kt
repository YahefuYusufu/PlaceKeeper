package com.example.mytracker.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.view.WindowCompat
import com.example.mytracker.presentation.screens.permissions.PermissionsScreen
import com.example.mytracker.presentation.screens.tracking.TrackingScreen
import com.example.mytracker.presentation.theme.MyTrackerTheme
import com.example.mytracker.utils.PermissionUtils

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)



        setContent {
            MyTrackerTheme {
                var hasPermissions by remember {
                    mutableStateOf(PermissionUtils.hasLocationPermissions(this))
                }

                Surface(modifier = Modifier.fillMaxSize()) {
                    if (hasPermissions) {
                        TrackingScreen()
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
