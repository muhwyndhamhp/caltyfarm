package com.caltyfarm.caltyfarm.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TwoLineListItem
import com.caltyfarm.caltyfarm.data.model.ShopItem
import java.text.NumberFormat
import java.util.*

class ShopOrderAdapter(val context: Context, val itemList: List<ShopItem>): BaseAdapter(){

    lateinit var twoLineListItem: TwoLineListItem

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        if(convertView == null){
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            twoLineListItem = inflater.inflate(android.R.layout.simple_list_item_2, null) as TwoLineListItem
        }
        else {
            twoLineListItem = convertView as TwoLineListItem
        }

        val tv1 = twoLineListItem.text1
        val tv2 = twoLineListItem.text2

        tv1.text = itemList[position].name
        tv2.text = NumberFormat
            .getCurrencyInstance(Locale("in", "ID"))
            .format(itemList[position].price * itemList[position].itemCount)

        return twoLineListItem
    }

    override fun getItem(position: Int) = itemList[position]

    override fun getItemId(position: Int) = 0.toLong()

    override fun getCount() = itemList.size

}