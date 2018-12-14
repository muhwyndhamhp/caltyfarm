package com.caltyfarm.caltyfarm.viewmodel.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.caltyfarm.caltyfarm.data.AppRepository
import com.caltyfarm.caltyfarm.data.model.Order
import com.caltyfarm.caltyfarm.viewmodel.ShopOrderViewModel

class ShopOrderViewModelFactory(
    val context: Context,
    val appRepository: AppRepository,
    val order: Order,
    val list: List<*>
): ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ShopOrderViewModel(context, appRepository, order, list) as T
    }
}