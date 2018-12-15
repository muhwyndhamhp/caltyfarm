package com.caltyfarm.caltyfarm.ui

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.caltyfarm.caltyfarm.R
import com.caltyfarm.caltyfarm.utils.InjectorUtils
import com.caltyfarm.caltyfarm.utils.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
import com.caltyfarm.caltyfarm.viewmodel.GoVetViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment


@Suppress("DEPRECATION")
class GoVetActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var map: GoogleMap

    lateinit var viewModel: GoVetViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_user_location)

        val factory = InjectorUtils.provideGovetViewModelFactory(this)
        viewModel = ViewModelProviders.of(this, factory).get(GoVetViewModel::class.java)

        viewModel.isAskingPermissionLocation.observe(this, Observer {
            if (it) getLocationPermission()
        })

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        viewModel.updateLocationUI(map)
        viewModel.getDeviceLocation(map, LocationServices.getFusedLocationProviderClient(this), this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        viewModel.locationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                @Suppress("DEPRECATED_IDENTITY_EQUALS")
                if (grantResults.isNotEmpty() && grantResults[0] === PackageManager.PERMISSION_GRANTED) {
                    viewModel.locationPermissionGranted = true
                }
            }
        }
        if (::map.isInitialized) viewModel.updateLocationUI(map)
    }

    private fun getLocationPermission() {

        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.locationPermissionGranted = true
            if (::map.isInitialized) viewModel.updateLocationUI(map)
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

}
