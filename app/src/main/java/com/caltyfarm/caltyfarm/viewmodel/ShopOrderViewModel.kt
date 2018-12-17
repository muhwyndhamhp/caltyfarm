package com.caltyfarm.caltyfarm.viewmodel

import android.app.Activity
import android.content.Context
import android.location.Geocoder
import android.os.Build
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.akexorcist.googledirection.DirectionCallback
import com.akexorcist.googledirection.GoogleDirection
import com.akexorcist.googledirection.constant.AvoidType
import com.akexorcist.googledirection.model.Direction
import com.akexorcist.googledirection.util.DirectionConverter
import com.caltyfarm.caltyfarm.BuildConfig
import com.caltyfarm.caltyfarm.R
import com.caltyfarm.caltyfarm.data.AppRepository
import com.caltyfarm.caltyfarm.data.model.Order
import com.caltyfarm.caltyfarm.data.model.ShopItem
import com.caltyfarm.caltyfarm.utils.BASE_PRICE_PER_KM
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

    val distance: MutableLiveData<Long> = MutableLiveData()

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
                        updateDistance(LatLng(
                            lastKnownLocation.latitude,
                            lastKnownLocation.longitude
                        ))
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

                        updateDistance(LatLng(-7.797068, 110.370529))
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

        updateDistance(latLng)
    }

    private fun updateDistance(latLng: LatLng){
        orderData.value!!.delivLat = latLng.latitude
        orderData.value!!.delivLong = latLng.longitude
        GoogleDirection.withServerKey(BuildConfig.MapsApiKey)
            .from(LatLng(order.sourceLat, order.sourceLong))
            .to(latLng)
            .avoid(AvoidType.FERRIES)
            .execute(object : DirectionCallback {
                override fun onDirectionSuccess(direction: Direction?, rawBody: String?) {
                    if (direction!!.isOK) {
                        (context as Activity).runOnUiThread {
                            val route = direction.routeList[0]
                            distance.value = route.legList[0].distance.value.toLong()
                        }
                    }
                }

                override fun onDirectionFailure(t: Throwable?) {

                }

            })
    }

    fun countDelivFee(it: Long?): Long {
        return it!!/1000 * BASE_PRICE_PER_KM
    }

    fun postOrder() {
        val totalprice = orderData.value!!.totalPrice + countDelivFee(distance.value!!)
        orderData.value!!.totalPrice = totalprice
        orderData.value!!.totalDistance = distance.value!!
        appRepository.postOrder(orderData)
    }

    val orderData: MutableLiveData<Order> = MutableLiveData()
    val selectedItems: MutableLiveData<List<ShopItem>> = MutableLiveData()

    init {
        orderData.value = order
        selectedItems.value = list as List<ShopItem>
        distance.value = 0
    }
}