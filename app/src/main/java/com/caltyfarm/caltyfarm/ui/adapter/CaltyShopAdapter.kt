package com.caltyfarm.caltyfarm.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.caltyfarm.caltyfarm.R
import com.caltyfarm.caltyfarm.data.model.Shop
import com.caltyfarm.caltyfarm.viewmodel.CaltyShopViewModel
import kotlinx.android.synthetic.main.item_calty_shop.view.*

class CaltyShopAdapter(val context: Context, val lifecycleOwner: LifecycleOwner, val viewModel: CaltyShopViewModel): RecyclerView.Adapter<CaltyShopAdapter.ViewHolder>(){
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindView(shop: Shop, context: Context) {
            itemView.tv_shop_name.text = shop.name
            itemView.tv_location_literal.text = shop.location
        }

    }
    
    lateinit var shopList: MutableList<Shop>

    init {
        viewModel.shopList.observe(lifecycleOwner, Observer { 
            shopList = it
        })
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CaltyShopAdapter.ViewHolder  =
        CaltyShopAdapter.ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(
                    R.layout.item_calty_shop,
                    parent,
                    false
                )
        )

    override fun getItemCount() = if (::shopList.isInitialized) shopList.size else 0

    override fun onBindViewHolder(holder: CaltyShopAdapter.ViewHolder, position: Int) {
        holder.bindView(shopList[position], context)
    }
}