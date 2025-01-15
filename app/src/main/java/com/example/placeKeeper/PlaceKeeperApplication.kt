package com.example.placeKeeper

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PlaceKeeperApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d("PlaceKeeperApplication", "Application created")
    }
}