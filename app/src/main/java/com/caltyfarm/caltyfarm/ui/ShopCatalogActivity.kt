package com.caltyfarm.caltyfarm.ui

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.caltyfarm.caltyfarm.R
import com.caltyfarm.caltyfarm.data.model.Shop
import com.caltyfarm.caltyfarm.ui.adapter.CaltyShopAdapter
import com.caltyfarm.caltyfarm.ui.adapter.ShopCatalogAdapter
import com.caltyfarm.caltyfarm.utils.*
import com.caltyfarm.caltyfarm.viewmodel.ShopCatalogViewModel
import kotlinx.android.synthetic.main.activity_calty_shop.*
import kotlinx.android.synthetic.main.activity_shop_catalog.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class ShopCatalogActivity : AppCompatActivity() {


    lateinit var viewModel: ShopCatalogViewModel
    lateinit var adapter: ShopCatalogAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_catalog)
        val shop = intent.getSerializableExtra(SHOP_CODE) as Shop

        collapsingToolbar.title = shop.name
        val factory = InjectorUtils.provideShopCatalogViewModelFactory(this, shop.id)
        viewModel = ViewModelProviders.of(this, factory).get(ShopCatalogViewModel::class.java)

        viewModel.errorMessage.observe(this, Observer {
            if (it != "") toast(it)
        })

        viewModel.updatePosition.observe(this, Observer {
            if (it != null) adapter.notifyItemChanged(it)

        })

        viewModel.addPosition.observe(this, Observer {
            if (it != null) adapter.notifyItemInserted(it)
        })

        viewModel.deletePosition.observe(this, Observer {
            if (it != null) adapter.notifyItemRemoved(it)
        })
        prepareRecyclerView()

        viewModel.currentOrder.observe(this, Observer {
            totalPrice.text = NumberFormat
                .getCurrencyInstance(Locale("in", "ID"))
                .format(it.totalPrice)
        })

        viewModel.selectedItemList.observe(this, Observer {
            itemCount.text = "Jumlah item: ${it.size}"
        })

        layout_shop_order.onClick {
            if(viewModel.selectedItemList.value!!.size != 0){
                if (setupPermission()) {
                    navigateToOrderCheck()
                } else {
                    ActivityCompat.requestPermissions(
                        this@ShopCatalogActivity,
                        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_FINE_LOCATION
                    )
                }
            } else{
                toast("Keranjang tidak boleh kosong!")
            }
        }
    }

    private fun prepareRecyclerView() {
        rv_shop_item.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        adapter = ShopCatalogAdapter(this, this, viewModel)
        rv_shop_item.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            REQUEST_FINE_LOCATION -> {
                if(grantResults.isNotEmpty()){
                    if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                        toast("Izinkan lokasi untuk melanjutkan!")
                    } else {
                        navigateToOrderCheck()
                    }
                }
            }
        }
    }

    private fun navigateToOrderCheck() {
        val intent = Intent(this@ShopCatalogActivity, ShopOrderActivity::class.java)
        intent.putExtra(ORDER_CODE, viewModel.currentOrder.value!!)
        intent.putExtra(SELECTED_ITEM_CODE, viewModel.selectedItemList.value!! as ArrayList)
        startActivity(intent)
    }

    private fun setupPermission(): Boolean {

        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)

        return permission == PackageManager.PERMISSION_GRANTED
    }
}
