package com.caltyfarm.caltyfarm.ui.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.caltyfarm.caltyfarm.R
import com.caltyfarm.caltyfarm.data.model.ShopItem
import com.caltyfarm.caltyfarm.utils.LoggingListener
import com.caltyfarm.caltyfarm.viewmodel.ShopCatalogViewModel
import kotlinx.android.synthetic.main.item_shop_item.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.text.NumberFormat
import java.util.*

class ShopCatalogAdapter(
    val context: Context,
    val lifecycleOwner: LifecycleOwner,
    val viewModel: ShopCatalogViewModel
) :
    RecyclerView.Adapter<ShopCatalogAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(shopItem: ShopItem, context: Context, viewModel: ShopCatalogViewModel) {
            itemView.apply {
                tv_item_name.text = shopItem.name
                tv_item_count.text = shopItem.itemCount.toString()
                tv_item_desc.text = shopItem.description
                tv_item_priceeeee.text = NumberFormat
                    .getCurrencyInstance(Locale("in", "ID"))
                    .format(shopItem.price)
                if (shopItem.imageUrl != "") Glide.with(context).load(shopItem.imageUrl).listener(LoggingListener<Drawable>())
                    .into(itemView.iv_item_photo)
                bt_add.onClick { viewModel.addItemToSelectedList(shopItem) }
                ib_minus.onClick { viewModel.removeItemFromSelectedList(shopItem)}
                ib_plus.onClick { viewModel.addItemCount(shopItem) }
                if(shopItem.itemCount != 0){
                    bt_add.visibility = View.GONE
                    ib_minus.visibility = View.VISIBLE
                    ib_plus.visibility = View.VISIBLE
                    tv_item_count.visibility = View.VISIBLE
                } else{
                    bt_add.visibility = View.VISIBLE
                    ib_minus.visibility = View.GONE
                    ib_plus.visibility = View.GONE
                    tv_item_count.visibility = View.GONE
                }
            }
        }

    }

    lateinit var shopList: MutableList<ShopItem>

    init {
        viewModel.itemList.observe(lifecycleOwner, Observer {
            shopList = it
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopCatalogAdapter.ViewHolder =
        ShopCatalogAdapter.ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(
                    R.layout.item_shop_item,
                    parent,
                    false
                )
        )

    override fun getItemCount() = if (::shopList.isInitialized) shopList.size else 0

    override fun onBindViewHolder(holder: ShopCatalogAdapter.ViewHolder, position: Int) {
        holder.bindView(shopList[position], context, viewModel)
    }

}