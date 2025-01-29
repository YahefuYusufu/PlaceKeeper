package com.example.placeKeeper.utils

import com.example.placeKeeper.domain.model.Place
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.CameraPositionState

object CameraUtils {
    // Default animation duration
    const val ANIMATION_DURATION = 1000

    // Zoom levels
    const val MIN_ZOOM = 2f
    const val MAX_ZOOM = 21f
    const val DEFAULT_ZOOM = 15f
    const val ZOOM_STEP = 1f

    // Bounds padding
    const val BOUNDS_PADDING = 100

    suspend fun CameraPositionState.animateToPosition(
        latLng: LatLng,
        zoom: Float = DEFAULT_ZOOM
    ) {
        animate(
            update = CameraUpdateFactory.newLatLngZoom(latLng, zoom),
            durationMs = ANIMATION_DURATION
        )
    }

    suspend fun CameraPositionState.animateToBounds(
        bounds: LatLngBounds,
        padding: Int = BOUNDS_PADDING
    ) {
        animate(
            update = CameraUpdateFactory.newLatLngBounds(bounds, padding),
            durationMs = ANIMATION_DURATION
        )
    }

    fun CameraPositionState.zoomIn(): Boolean {
        if (position.zoom < MAX_ZOOM) {
            position = CameraPosition.Builder()
                .target(position.target)
                .zoom(position.zoom + ZOOM_STEP)
                .bearing(position.bearing)
                .tilt(position.tilt)
                .build()
            return true
        }
        return false
    }

    fun CameraPositionState.zoomOut(): Boolean {
        if (position.zoom > MIN_ZOOM) {
            position = CameraPosition.Builder()
                .target(position.target)
                .zoom(position.zoom - ZOOM_STEP)
                .bearing(position.bearing)
                .tilt(position.tilt)
                .build()
            return true
        }
        return false
    }

    fun buildBoundsFromPlaces(places: List<Place>): LatLngBounds {
        return LatLngBounds.Builder().apply {
            places.forEach { place ->
                include(LatLng(place.latitude, place.longitude))
            }
        }.build()
    }
}