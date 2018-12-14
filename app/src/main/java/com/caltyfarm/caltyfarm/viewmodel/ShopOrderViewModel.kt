package com.caltyfarm.caltyfarm.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.caltyfarm.caltyfarm.data.AppRepository
import com.caltyfarm.caltyfarm.data.model.Order
import com.caltyfarm.caltyfarm.data.model.ShopItem

class ShopOrderViewModel(
    val context: Context,
    val appRepository: AppRepository,
    val order: Order,
    val list: List<*>
): ViewModel(){

    val orderData: MutableLiveData<Order> = MutableLiveData()
    val selectedItems: MutableLiveData<List<ShopItem>> = MutableLiveData()

    init {
        orderData.value = order
        selectedItems.value = list as List<ShopItem>
    }
}