
package com.example.mytracker.utils

object MapProperties {
    // Default camera position (you can modify these coordinates)
    const val DEFAULT_LATITUDE = 0.0
    const val DEFAULT_LONGITUDE = 0.0
    const val DEFAULT_ZOOM = 15f

    // Map UI settings
    val defaultMapUiSettings = mapOf(
        "isZoomControlsEnabled" to false,      // We'll add custom controls
        "isCompassEnabled" to true,
        "isMyLocationButtonEnabled" to false,  // We'll add custom location button
        "isMapToolbarEnabled" to false
    )

    // Map types
    enum class MapType {
        NORMAL,
        SATELLITE,
        TERRAIN,
        HYBRID
    }
}