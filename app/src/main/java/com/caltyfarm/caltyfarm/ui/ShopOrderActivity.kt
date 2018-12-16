package com.caltyfarm.caltyfarm.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.caltyfarm.caltyfarm.R
import com.caltyfarm.caltyfarm.data.model.Order
import com.caltyfarm.caltyfarm.ui.adapter.ShopOrderAdapter
import com.caltyfarm.caltyfarm.utils.InjectorUtils
import com.caltyfarm.caltyfarm.utils.ORDER_CODE
import com.caltyfarm.caltyfarm.utils.PLACE_PICKER_REQUEST
import com.caltyfarm.caltyfarm.utils.SELECTED_ITEM_CODE
import com.caltyfarm.caltyfarm.viewmodel.ShopOrderViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.android.synthetic.main.activity_shop_order.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast
import java.text.NumberFormat
import java.util.*

class ShopOrderActivity : AppCompatActivity(), OnMapReadyCallback {
    override fun onMapReady(p0: GoogleMap?) {
        viewModel.map.value = p0
        viewModel.getCurrentLocation(LocationServices.getFusedLocationProviderClient(this), this)
        viewModel.address.observe(this, androidx.lifecycle.Observer {
            tv_location.text = it
        })
    }

    lateinit var viewModel: ShopOrderViewModel

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
            viewModel.locationPermissionGranted = true
            val mapFragment = supportFragmentManager.findFragmentById(R.id.map_shop_order) as SupportMapFragment
            mapFragment.getMapAsync(this)
        }
        viewModel.orderData.observe(this, androidx.lifecycle.Observer {
            tv_total_item_price.text = NumberFormat
                .getCurrencyInstance(Locale("in", "ID"))
                .format(it.totalPrice)

            tv_total_price.text = NumberFormat
                .getCurrencyInstance(Locale("in", "ID"))
                .format(it.totalPrice + tv_deliv_fee.text.toString().toLong())
        })

        tv_location.onClick {
            startPlacePicker()
        }

        bt_order_now.onClick {
            longToast("Order berhasil dikirim!")
            startActivity(Intent(this@ShopOrderActivity, MainActivity::class.java))
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PLACE_PICKER_REQUEST ){
            if(resultCode == Activity.RESULT_OK) {
                val place = PlacePicker.getPlace(data, this)
                tv_location.text = place.address
                viewModel.moveCameraAndPin(place.latLng)
            }
        }
    }

    private fun startPlacePicker() {
        val builder = PlacePicker.IntentBuilder()

        startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST)
    }

    private fun setupPermission(): Boolean {

        val permission1 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
        val permission2 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED
    }
}
