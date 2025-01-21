package com.example.placeKeeper.domain.location

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.example.placeKeeper.utils.PermissionUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume


@Singleton
class LocationManager @Inject constructor(
    private val context: Application,
    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
        context
    )
) {
    private val _currentLocation = MutableStateFlow<Location?>(null)
    val currentLocation = _currentLocation.asStateFlow()

    private val _locationPermissionGranted = MutableStateFlow(
        PermissionUtils.hasLocationPermissions(context)
    )
    val locationPermissionGranted = _locationPermissionGranted.asStateFlow()


    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): Result<Location> {
        return suspendCancellableCoroutine { continuation ->
            try {
                // Explicit permission check for both required permissions
                val hasFineLocation = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

                val hasCoarseLocation = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

                if (!hasFineLocation || !hasCoarseLocation) {
                    continuation.resume(Result.failure(SecurityException("Location permission not granted")))
                    return@suspendCancellableCoroutine
                }

                // If we have permissions, proceed with location request
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location ->
                        location?.let {
                            _currentLocation.value = it
                            continuation.resume(Result.success(it))
                        }
                            ?: continuation.resume(Result.failure(Exception("Location not available")))
                    }
                    .addOnFailureListener { exception ->
                        continuation.resume(Result.failure(exception))
                    }

            } catch (e: Exception) {
                continuation.resume(Result.failure(e))
            }
        }
    }

    fun updatePermissionStatus(granted: Boolean) {
        _locationPermissionGranted.value = granted
    }

    companion object {
        fun isValidLatitude(latitude: Double) = latitude in -90.0..90.0
        fun isValidLongitude(longitude: Double) = longitude in -180.0..180.0
    }


}
