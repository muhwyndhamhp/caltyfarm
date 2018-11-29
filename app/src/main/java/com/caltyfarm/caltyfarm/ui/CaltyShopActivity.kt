package com.caltyfarm.caltyfarm.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.caltyfarm.caltyfarm.R
import com.caltyfarm.caltyfarm.ui.adapter.CaltyShopAdapter
import com.caltyfarm.caltyfarm.utils.InjectorUtils
import com.caltyfarm.caltyfarm.viewmodel.CaltyShopViewModel
import kotlinx.android.synthetic.main.activity_calty_shop.*
import kotlinx.android.synthetic.main.activity_halo_cows.*
import org.jetbrains.anko.toast

class CaltyShopActivity : AppCompatActivity() {

    lateinit var viewModel: CaltyShopViewModel
    lateinit var adapter: CaltyShopAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calty_shop)

        val factory = InjectorUtils.provideCaltyShopViewModelFactory(this)
        viewModel = ViewModelProviders.of(this, factory).get(CaltyShopViewModel::class.java)

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
    }

    private fun prepareRecyclerView() {
        rv_calty_shop.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        adapter = CaltyShopAdapter(this, this, viewModel)
        rv_calty_shop.adapter = adapter
        adapter.notifyDataSetChanged()
    }
}
