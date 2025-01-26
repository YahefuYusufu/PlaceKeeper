
package com.example.placeKeeper.utils

import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings

object MapUtils {
    fun defaultProperties(
        isMyLocationEnabled: Boolean,
        mapType: MapType = MapType.NORMAL,
    ) = MapProperties(
        isMyLocationEnabled = isMyLocationEnabled,
        mapType = mapType,
        minZoomPreference = 3f,
        maxZoomPreference = 20f,
        isBuildingEnabled = true
    )

    fun defaultUiSettings(
        showZoomControls: Boolean = true,
        showCompass: Boolean = true
    ) = MapUiSettings(
        zoomControlsEnabled = showZoomControls,
        myLocationButtonEnabled = false,
        compassEnabled = showCompass,
        scrollGesturesEnabled = true,
        scrollGesturesEnabledDuringRotateOrZoom = true,
        rotationGesturesEnabled = true,
        tiltGesturesEnabled = true,
        zoomGesturesEnabled = true
    )
}