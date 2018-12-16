package com.caltyfarm.caltyfarm.viewmodel

import android.app.Activity
import android.content.Context
import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.caltyfarm.caltyfarm.data.AppRepository
import com.caltyfarm.caltyfarm.data.model.Order
import com.caltyfarm.caltyfarm.data.model.ShopItem
import com.caltyfarm.caltyfarm.utils.DEFAULT_ZOOM
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class ShopOrderViewModel(
    val context: Context,
    val appRepository: AppRepository,
    val order: Order,
    val list: List<*>
) : ViewModel() {

    var locationPermissionGranted = false

    val map: MutableLiveData<GoogleMap> = MutableLiveData()

    var positionMarker: Marker? = null

    val address: MutableLiveData<String> = MutableLiveData()

    fun getCurrentLocation(
        fusedLocationProviderClient: FusedLocationProviderClient,
        activity: Activity
    ) {

        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        // Set the map.value's camera position to the current location of the device.
                        val lastKnownLocation = task.result
                        map.value!!.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    lastKnownLocation!!.latitude,
                                    lastKnownLocation.longitude
                                ), DEFAULT_ZOOM
                            )
                        )
                        positionMarker = map.value!!.addMarker(
                            MarkerOptions().position(
                                LatLng(
                                    lastKnownLocation.latitude,
                                    lastKnownLocation.longitude
                                )
                            )
                        )

                        address.value = Geocoder(context, Locale.getDefault()).getFromLocation(
                            lastKnownLocation.latitude,
                            lastKnownLocation.longitude,
                            1
                        )[0].getAddressLine(0)!!
                    } else {
                        Log.d("GOVET", "Current location is null. Using defaults.")
                        Log.e("GOVET", "Exception: %s", task.exception)
                        map.value!!.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(-7.797068, 110.370529),
                                DEFAULT_ZOOM
                            )
                        )

                        positionMarker = map.value!!.addMarker(
                            MarkerOptions().position(
                                LatLng(-7.797068, 110.370529)
                            )
                        )
                        address.value = Geocoder(context, Locale.getDefault()).getFromLocation(
                            -7.797068, 110.370529,
                            1
                        )[0].getAddressLine(0)!!

                        map.value!!.uiSettings.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message)
        }
    }

    fun moveCameraAndPin(latLng: LatLng) {
        if (positionMarker != null) positionMarker!!.remove()
        map.value!!.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    latLng.latitude,
                    latLng.longitude
                ), DEFAULT_ZOOM
            )
        )
        positionMarker = map.value!!.addMarker(
            MarkerOptions().position(
                LatLng(
                    latLng.latitude,
                    latLng.longitude
                )
            )
        )
    }

    val orderData: MutableLiveData<Order> = MutableLiveData()
    val selectedItems: MutableLiveData<List<ShopItem>> = MutableLiveData()

    init {
        orderData.value = order
        selectedItems.value = list as List<ShopItem>
    }
}