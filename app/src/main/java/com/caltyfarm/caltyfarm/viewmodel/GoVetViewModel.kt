package com.caltyfarm.caltyfarm.viewmodel

import android.app.Activity
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.akexorcist.googledirection.DirectionCallback
import com.akexorcist.googledirection.GoogleDirection
import com.akexorcist.googledirection.constant.AvoidType
import com.akexorcist.googledirection.model.Direction
import com.akexorcist.googledirection.model.Route
import com.akexorcist.googledirection.util.DirectionConverter
import com.caltyfarm.caltyfarm.BuildConfig
import com.caltyfarm.caltyfarm.R
import com.caltyfarm.caltyfarm.data.AppRepository
import com.caltyfarm.caltyfarm.data.model.Vet
import com.caltyfarm.caltyfarm.utils.DEFAULT_ZOOM
import com.caltyfarm.caltyfarm.utils.DistanceCounter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import java.util.*


class GoVetViewModel(val appRepository: AppRepository, val context: Context) : ViewModel() {

    private val lastKnownLocation: MutableLiveData<Location> = MutableLiveData()
    val isAskingPermissionLocation: MutableLiveData<Boolean> = MutableLiveData()
    val vetList: MutableLiveData<MutableList<Vet>> = MutableLiveData()
    private val markerList: MutableList<Marker> = mutableListOf()
    val map: MutableLiveData<GoogleMap> = MutableLiveData()
    var locationPermissionGranted = false
    lateinit var userPosition: LatLng
    lateinit var vetPosition: LatLng
    var polyDirection: Polyline? = null
    var polyDistance: MutableLiveData<String> = MutableLiveData()
    var polyDuration: String? = ""

    init {
        vetList.value = mutableListOf()
        polyDistance.value = ""
        isAskingPermissionLocation.value = false
    }

    fun updateLocationUI() {
        if (map.value != null)
            try {
                if (locationPermissionGranted) {
                    map.value!!.isMyLocationEnabled = true
                    map.value!!.uiSettings.isMyLocationButtonEnabled = true
                } else {
                    map.value!!.isMyLocationEnabled = false
                    map.value!!.uiSettings.isMyLocationButtonEnabled = false
                    lastKnownLocation.value = null
                    isAskingPermissionLocation.value = true
                }
            } catch (e: SecurityException) {
                Log.e("Exception: %s", e.message)
            }

    }

    fun getDeviceLocation(
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
                        userPosition =
                                LatLng(
                                    lastKnownLocation.latitude,
                                    lastKnownLocation.longitude
                                )
                        putVetDataOnMap(
                            LatLng(
                                lastKnownLocation.latitude,
                                lastKnownLocation.longitude
                            )
                        )
                    } else {
                        Log.d("GOVET", "Current location is null. Using defaults.")
                        Log.e("GOVET", "Exception: %s", task.exception)
                        map.value!!.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(-7.797068, 110.370529),
                                DEFAULT_ZOOM
                            )
                        )
                        userPosition = LatLng(-7.797068, 110.370529)
                        putVetDataOnMap(LatLng(-7.797068, 110.370529))
                        map.value!!.uiSettings.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message)
        }

    }

    private fun putVetDataOnMap(latLng: LatLng) {
        appRepository.getVetList(object : AppRepository.OnVetRetrievedCallback {
            override fun onChildAdded(article: Vet?) {
                if (DistanceCounter.distance(
                        latLng.latitude,
                        article!!.lati,
                        latLng.longitude,
                        article.longi,
                        0.0,
                        0.0
                    ) < 10000
                ) {
                    markerList.add(map.value!!.addMarker(
                        MarkerOptions().position(LatLng(article.lati, article.longi))
                            .title(article.name)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.vet_pin))
                    ).also { it.showInfoWindow() })
                    addArticle(article)
                }
            }

            override fun onChildChanged(article: Vet?) {

            }

            override fun onChildDeleted(article: Vet?) {
                deleteMarker(article!!)
            }

            override fun onFailed(exception: Exception) {

            }

        })
    }

    private fun deleteMarker(article: Vet) {
        val position = searchMarkerPosition(article.id)

        if (position != -1) {
            vetList.value!!.removeAt(position)
            markerList[position].remove()
        }
    }

    private fun searchMarkerPosition(id: String): Int {
        for (i in vetList.value!!.indices) {
            if (vetList.value!![i].id == id) return i
        }
        return -1
    }

    fun getMarkerVetData(name: String): Vet {
        val position = searchMarkerPosition(name, false)
        return vetList.value!![position]
    }

    private fun searchMarkerPosition(name: String, isMarker: Boolean): Int {
        for (i in vetList.value!!.indices) {
            if (vetList.value!![i].name == name) return i
        }
        return -1
    }

    private fun addArticle(article: Vet) {
        vetList.value!!.add(article)
    }

    fun drawRoute(location: LatLng) {
        vetPosition = location
        GoogleDirection.withServerKey(BuildConfig.MapsApiKey)
            .from(userPosition)
            .to(vetPosition)
            .avoid(AvoidType.FERRIES)
            .execute(object : DirectionCallback {
                override fun onDirectionSuccess(direction: Direction?, rawBody: String?) {
                    if (direction!!.isOK) {
                        (context as Activity).runOnUiThread {
                            val route = direction.routeList[0]
                            if (polyDirection != null) polyDirection!!.remove()
                            val directionPositionList = route.legList[0].directionPoint
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                polyDirection = map.value!!.addPolyline(
                                    DirectionConverter.createPolyline(
                                        context,
                                        directionPositionList,
                                        3,
                                        context.resources.getColor(R.color.colorAccent, context.theme)
                                    )
                                )
                            } else {
                                polyDirection = map.value!!.addPolyline(
                                    DirectionConverter.createPolyline(
                                        context,
                                        directionPositionList,
                                        5,
                                        context.resources.getColor(R.color.colorAccent)
                                    )
                                )
                            }
                            polyDistance.value = route.legList[0].distance.value
                            polyDuration = route.legList[0].duration.value
                            setCameraWithCoordinationBounds(route)
                        }
                    }
                }

                override fun onDirectionFailure(t: Throwable?) {

                }

            })
    }

    private fun setCameraWithCoordinationBounds(route: Route) {
        val southwest = route.bound.southwestCoordination.coordination
        val northeast = route.bound.northeastCoordination.coordination
        val bounds = LatLngBounds(southwest, northeast)
        map.value!!.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
    }

    fun removeRoute() {
        if (polyDirection != null) polyDirection!!.remove()
    }

    fun getVetAddress() =
        Geocoder(context, Locale.getDefault()).getFromLocation(
            vetPosition.latitude,
            vetPosition.longitude,
            1
        )[0].getAddressLine(0)!!

    fun getUserAddress() =
        Geocoder(context, Locale.getDefault()).getFromLocation(
            userPosition.latitude,
            userPosition.longitude,
            1
        )[0].getAddressLine(0)!!
}