package com.caltyfarm.caltyfarm.ui

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.caltyfarm.caltyfarm.R
import com.caltyfarm.caltyfarm.data.model.Order
import com.caltyfarm.caltyfarm.ui.adapter.ShopOrderAdapter
import com.caltyfarm.caltyfarm.utils.InjectorUtils
import com.caltyfarm.caltyfarm.utils.KalmanLatLong
import com.caltyfarm.caltyfarm.utils.ORDER_CODE
import com.caltyfarm.caltyfarm.utils.SELECTED_ITEM_CODE
import com.caltyfarm.caltyfarm.viewmodel.ShopOrderViewModel
import kotlinx.android.synthetic.main.activity_shop_order.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast
import java.text.NumberFormat
import java.util.*

class ShopOrderActivity : AppCompatActivity() {

    lateinit var viewModel: ShopOrderViewModel

    lateinit var locationManager: LocationManager

    lateinit var locationListener: LocationListener

    lateinit var kalmanFilter: KalmanLatLong

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_order)

        val factory = InjectorUtils.provideShopOrderViewModelFactory(this, intent.getSerializableExtra(ORDER_CODE) as Order, intent.getSerializableExtra(SELECTED_ITEM_CODE) as List<*>)
        viewModel = ViewModelProviders.of(this, factory).get(ShopOrderViewModel::class.java)

        lv_order_item_list.adapter = ShopOrderAdapter(this, viewModel.selectedItems.value!!)

        if(!setupPermission()) {
            toast("Membutuhkan izin lokasi")
            finish()
        }else {
            val criteria = Criteria()
            criteria.apply {
                accuracy = Criteria.ACCURACY_FINE
                powerRequirement = Criteria.POWER_HIGH
                isAltitudeRequired = false
                isSpeedRequired = false
                isCostAllowed = true
                isBearingRequired = false
                horizontalAccuracy = Criteria.ACCURACY_HIGH
                verticalAccuracy = Criteria.ACCURACY_HIGH
            }
            kalmanFilter = KalmanLatLong(3F)
            locationListener = object: LocationListener{
                override fun onLocationChanged(location: Location?) {
                    if(location!= null) filterAndSetLocation(location)
                }

                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

                override fun onProviderEnabled(provider: String?) {}

                override fun onProviderDisabled(provider: String?) {}

            }
            locationManager = getSystemService(Service.LOCATION_SERVICE) as LocationManager
            locationManager.requestLocationUpdates(300, 30F, criteria, locationListener, null)
        }
        viewModel.orderData.observe(this, androidx.lifecycle.Observer {
            tv_total_item_price.text = NumberFormat
                .getCurrencyInstance(Locale("in", "ID"))
                .format(it.totalPrice)

            tv_total_price.text = NumberFormat
                .getCurrencyInstance(Locale("in", "ID"))
                .format(it.totalPrice + tv_deliv_fee.text.toString().toLong())
        })

        bt_order_now.onClick {
            longToast("Order berhasil dikirim!")
            startActivity(Intent(this@ShopOrderActivity, MainActivity::class.java))
            finish()
        }
    }

    private fun filterAndSetLocation(location: Location): Boolean {
        
        if (location.accuracy <= 0) {
            Log.d("TAG", "Latitidue and longitude values are invalid.")
            return false
        }

        //setAccuracy(newLocation.getAccuracy());
        val horizontalAccuracy = location.accuracy
        if (horizontalAccuracy > 30) {
            Log.d("TAG", "Accuracy is too low.")
            return false
        }


        /* Kalman Filter */
        val qValue = 3F

        val elapsedTimeInMillis = 100.toLong()

        kalmanFilter.Process(location.latitude, location.longitude, location.accuracy, elapsedTimeInMillis, qValue)
        val predictedLat = kalmanFilter._lat
        val predictedLng = kalmanFilter._lng

        val predictedLocation = Location("")//provider name is unecessary
        predictedLocation.latitude = predictedLat//your coords of course
        predictedLocation.longitude = predictedLng
        val predictedDeltaInMeters = predictedLocation.distanceTo(location)

        if (predictedDeltaInMeters > 60) {
            Log.d("TAG", "Kalman Filter detects mal GPS, we should probably remove this from track")
            kalmanFilter.consecutiveRejectCount += 1

            if (kalmanFilter.consecutiveRejectCount > 3) {
                kalmanFilter = KalmanLatLong(3F) //reset Kalman Filter if it rejects more than 3 times in raw.
            }

            return false
        } else {
            kalmanFilter.consecutiveRejectCount = 0
        }

        Log.d("TAG", "Location quality is good enough.")
        updateDistance(location)


        return true
    }

    private fun updateDistance(location: Location) {
        tv_location.text = "Latitude: ${location.latitude}; \nLongitude: ${location.longitude}"
    }

    private fun setupPermission(): Boolean {

        val permission1 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
        val permission2 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED
    }
}
