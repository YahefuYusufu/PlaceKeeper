package com.example.placeKeeper.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Museum
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Constants {
    // Map related
    const val DEFAULT_ZOOM = 15f
    const val DEFAULT_LATITUDE = 0.0
    const val DEFAULT_LONGITUDE = 0.0

    // Permission related
    const val LOCATION_PERMISSION_REQUEST = 1

    // Database related (if we add later)
    const val DATABASE_NAME = "placekeeper_db"
}

object CategoryConstants {
    val AVAILABLE_COLORS = listOf(
        ColorOption("#FF4081", "Pink"),
        ColorOption("#00C853", "Green"),
        ColorOption("#FF6E40", "Orange"),
        ColorOption("#6200EA", "Purple"),
        ColorOption("#2962FF", "Blue"),
        ColorOption("#FFD600", "Yellow"),
        ColorOption("#FF1744", "Red"),
        ColorOption("#00B8D4", "Cyan"),
        ColorOption("#FF9100", "Amber"),
        ColorOption("#3D5AFE", "Indigo")
    )

    val AVAILABLE_ICONS = listOf(
        IconOption(Icons.Default.Place, "place", "Location"),
        IconOption(Icons.Default.Restaurant, "restaurant", "Restaurant"),
        IconOption(Icons.Default.Park, "park", "Park"),
        IconOption(Icons.Default.Museum, "museum", "Museum"),
        IconOption(Icons.Default.ShoppingCart, "shopping", "Shopping"),
        IconOption(Icons.Default.LocalCafe, "cafe", "Cafe"),
        IconOption(Icons.Default.School, "school", "Education"),
        IconOption(Icons.Default.Movie, "movie", "Entertainment"),
        IconOption(Icons.Default.FitnessCenter, "fitness", "Fitness"),
        IconOption(Icons.Default.LocalHospital, "hospital", "Healthcare")
    )

    fun getIconForName(iconName: String): ImageVector {
        return AVAILABLE_ICONS.find { it.iconName == iconName }?.imageVector
            ?: Icons.Default.Place
    }

}

data class ColorOption(
    val hex: String,
    val name: String
)

data class IconOption(
    val imageVector: ImageVector,
    val iconName: String,
    val displayName: String
)

fun formatDate(timestamp: Long): String {
    return SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(timestamp))
}