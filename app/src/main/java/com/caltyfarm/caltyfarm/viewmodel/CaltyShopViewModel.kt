package com.caltyfarm.caltyfarm.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.caltyfarm.caltyfarm.data.AppRepository
import com.caltyfarm.caltyfarm.data.model.Shop

class CaltyShopViewModel(val context: Context, val appRepository: AppRepository) : ViewModel() {

    val shopList: MutableLiveData<MutableList<Shop>> = MutableLiveData()
    val deletePosition: MutableLiveData<Int> = MutableLiveData()
    val updatePosition: MutableLiveData<Int> = MutableLiveData()
    val addPosition: MutableLiveData<Int> = MutableLiveData()
    val errorMessage: MutableLiveData<String> = MutableLiveData()

    init {
        shopList.value = mutableListOf()
        appRepository.getShops(object : AppRepository.OnShopRetireveCallback {
            override fun onChildAdded(shop: Shop?) {
                addChild(shop!!)
            }

            override fun onChildChanged(shop: Shop?) {
                changeChild(shop!!)
            }

            override fun onChildDeleted(shop: Shop?) {
                deleteChild(shop!!)
            }

            override fun onFailed(exception: Exception) {
                errorMessage.value = exception.message
            }

        })
    }

    private fun deleteChild(shop: Shop) {
        val position = searchArticlePosition(shop.id)
        if (position != -1) {
            shopList.value!!.removeAt(position)
            deletePosition.value = position
        }
    }

    private fun changeChild(shop: Shop) {
        val position = searchArticlePosition(shop.id)
        if (position != -1) {
            shopList.value!![position] = shop
            updatePosition.value = position
        }
    }

    private fun searchArticlePosition(id: String): Int {
        for (i in shopList.value!!.indices) {
            if (shopList.value!![i].id == id) return i
        }
        return -1
    }

    private fun addChild(shop: Shop) {
        shopList.value!!.add(shop)
        addPosition.value = shopList.value!!.size - 1
    }
}