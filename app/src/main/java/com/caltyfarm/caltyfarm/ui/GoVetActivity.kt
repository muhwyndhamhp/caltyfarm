package com.caltyfarm.caltyfarm.ui

import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.caltyfarm.caltyfarm.R
import com.caltyfarm.caltyfarm.data.model.Vet
import com.caltyfarm.caltyfarm.ui.fragments.GovetDataVetFragment
import com.caltyfarm.caltyfarm.utils.InjectorUtils
import com.caltyfarm.caltyfarm.utils.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
import com.caltyfarm.caltyfarm.viewmodel.GoVetViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_govet.*
import org.jetbrains.anko.indeterminateProgressDialog


@Suppress("DEPRECATION")
class GoVetActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var viewModel: GoVetViewModel

    lateinit var progressDialog : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_govet)

        val factory = InjectorUtils.provideGovetViewModelFactory(this)
        viewModel = ViewModelProviders.of(this, factory).get(GoVetViewModel::class.java)

        viewModel.isAskingPermissionLocation.observe(this, Observer {
            if (it) getLocationPermission()
        })

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        viewModel.map.value = googleMap
        viewModel.map.value!!.setOnMarkerClickListener { marker ->
            setMarkerClickListener(marker)
            false
        }
        viewModel.updateLocationUI()
        viewModel.getDeviceLocation(LocationServices.getFusedLocationProviderClient(this), this)
    }

    private fun setMarkerClickListener(marker: Marker?) {
        val vetData = viewModel.getMarkerVetData(marker!!.title)
        viewModel.drawRoute(LatLng(vetData.lati, vetData.longi))

        showLoadingDialog()
        viewModel.polyDistance.observe(this, Observer {
            if(it != "") inflateFragment(vetData, it, viewModel.polyDuration)
            dismissProgressDialog()
        })
    }

    private fun showLoadingDialog() {
        progressDialog = indeterminateProgressDialog("Memuat data dokter hewan", "Harap Tunggu")

        progressDialog.show()
    }

    fun dismissProgressDialog(){
        if(::progressDialog.isInitialized && progressDialog.isShowing) progressDialog.dismiss()
    }

    private fun inflateFragment(
        vetData: Vet,
        it: String,
        polyDuration: String?
    ) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.frame_govet, GovetDataVetFragment.newInstance(vetData, it, polyDuration))
        ft.commit()
        tv_title.visibility = View.GONE
        BottomSheetBehavior.from(bottom_sheet).state = BottomSheetBehavior.STATE_EXPANDED
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
        viewModel.updateLocationUI()
    }

    private fun getLocationPermission() {

        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.locationPermissionGranted = true
            viewModel.updateLocationUI()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    override fun onBackPressed() {
        if (tv_title.visibility == View.GONE) {
            tv_title.visibility = View.VISIBLE
            BottomSheetBehavior.from(bottom_sheet).state = BottomSheetBehavior.STATE_COLLAPSED
            viewModel.removeRoute()
        } else super.onBackPressed()
    }
}
