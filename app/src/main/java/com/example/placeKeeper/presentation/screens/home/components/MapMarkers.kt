package com.example.placeKeeper.presentation.screens.home.components

 import com.google.android.gms.maps.model.BitmapDescriptorFactory


object MapMarkers {
    fun getMarkerIcon(categoryId: Long): Float {
        return when (categoryId) {
            1L -> BitmapDescriptorFactory.HUE_RED
            2L -> BitmapDescriptorFactory.HUE_BLUE       // Example: Hotel
            3L -> BitmapDescriptorFactory.HUE_GREEN      // Example: Park
            4L -> BitmapDescriptorFactory.HUE_VIOLET     // Example: Shopping
            5L -> BitmapDescriptorFactory.HUE_ORANGE     // Example: Gym
            6L -> BitmapDescriptorFactory.HUE_YELLOW     // Example: School
            else -> BitmapDescriptorFactory.HUE_AZURE
        }
    }
}