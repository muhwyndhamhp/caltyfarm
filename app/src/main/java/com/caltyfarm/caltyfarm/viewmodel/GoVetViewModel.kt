package com.caltyfarm.caltyfarm.viewmodel

import android.app.Activity
import android.location.Location
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.caltyfarm.caltyfarm.data.AppRepository
import com.caltyfarm.caltyfarm.utils.DEFAULT_ZOOM
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng

class GoVetViewModel(val appRepository: AppRepository) : ViewModel() {

    var locationPermissionGranted = false

    val lastKnownLocation : MutableLiveData<Location> = MutableLiveData()
    val isAskingPermissionLocation: MutableLiveData<Boolean> = MutableLiveData()

    init {
        isAskingPermissionLocation.value = false
    }

    fun updateLocationUI(map: GoogleMap) {
        try {
            if (locationPermissionGranted) {
                map.isMyLocationEnabled = true
                map.uiSettings.isMyLocationButtonEnabled = true
            } else {
                map.isMyLocationEnabled = false
                map.uiSettings.isMyLocationButtonEnabled = false
                lastKnownLocation.value = null
                isAskingPermissionLocation.value = true
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message)
        }

    }

    fun getDeviceLocation(
        map: GoogleMap,
        fusedLocationProviderClient: FusedLocationProviderClient,
        activity: Activity
    ) {
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        val lastKnownLocation = task.result
                        map.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    lastKnownLocation!!.latitude,
                                    lastKnownLocation.longitude
                                ), DEFAULT_ZOOM
                            )
                        )
                    } else {
                        Log.d("GOVET", "Current location is null. Using defaults.")
                        Log.e("GOVET", "Exception: %s", task.exception)
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(-7.797068, 110.370529), DEFAULT_ZOOM))
                        map.uiSettings.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message)
        }

    }
}