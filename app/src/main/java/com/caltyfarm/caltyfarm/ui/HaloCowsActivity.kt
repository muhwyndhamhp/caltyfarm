package com.caltyfarm.caltyfarm.ui

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.caltyfarm.caltyfarm.R
import com.caltyfarm.caltyfarm.ui.adapter.ArticleAdapter
import com.caltyfarm.caltyfarm.ui.adapter.HaloCowAdapter
import com.caltyfarm.caltyfarm.utils.InjectorUtils
import com.caltyfarm.caltyfarm.utils.REQUEST_PHONE_CODE
import com.caltyfarm.caltyfarm.viewmodel.HaloCowsViewModel
import kotlinx.android.synthetic.main.activity_halo_cows.*
import kotlinx.android.synthetic.main.fragment_article.*
import kotlinx.android.synthetic.main.item_halo_cow.view.*
import org.jetbrains.anko.toast

class HaloCowsActivity : AppCompatActivity() {


    lateinit var viewModel: HaloCowsViewModel
    lateinit var adapter: HaloCowAdapter
    lateinit var phoneNumber: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_halo_cows)

        val factory = InjectorUtils.provideHaloCowsViewModelFactory(this)
        viewModel = ViewModelProviders.of(this, factory).get(HaloCowsViewModel::class.java)

        viewModel.errorMessage.observe(this, Observer {
            if(it != "") toast(it)
        })

        viewModel.updatePosition.observe(this, Observer {
            if(it != null) adapter.notifyItemChanged(it)

        })

        viewModel.addPosition.observe(this, Observer {
            if(it != null) adapter.notifyItemInserted(it)
        })

        viewModel.deletePosition.observe(this, Observer {
            if(it != null) adapter.notifyItemRemoved(it)
        })
        prepareRecyclerView()
    }

    private fun prepareRecyclerView() {
        rv_halo_cow.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        adapter = HaloCowAdapter(this, this, viewModel)
        rv_halo_cow.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            REQUEST_PHONE_CODE -> {
                if(grantResults.isNotEmpty()){
                    if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                        toast("Permintaan membaca SMS ditolak!")
                    } else {
                        startActivity(
                            Intent(
                                Intent.ACTION_CALL,
                                Uri.parse("tel:$phoneNumber")
                            )
                        )
                    }
                }
            }
        }
    }
}
