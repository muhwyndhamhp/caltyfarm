package com.caltyfarm.caltyfarm.viewmodel

import android.content.Context
import android.location.Geocoder
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.caltyfarm.caltyfarm.data.AppRepository
import com.caltyfarm.caltyfarm.data.model.Order
import com.caltyfarm.caltyfarm.data.model.Shop
import com.caltyfarm.caltyfarm.data.model.ShopItem
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class ShopCatalogViewModel(val context: Context, val appRepository: AppRepository, val shopId: Shop) : ViewModel() {

    val itemList: MutableLiveData<MutableList<ShopItem>> = MutableLiveData()
    val selectedItemList: MutableLiveData<MutableList<ShopItem>> = MutableLiveData()
    val currentOrder: MutableLiveData<Order> = MutableLiveData()
    val deletePosition: MutableLiveData<Int> = MutableLiveData()
    val updatePosition: MutableLiveData<Int> = MutableLiveData()
    val addPosition: MutableLiveData<Int> = MutableLiveData()
    val errorMessage: MutableLiveData<String> = MutableLiveData()

    init {
        itemList.value = mutableListOf()
        selectedItemList.value = mutableListOf()
        appRepository.getOrderListCount(FirebaseAuth.getInstance().currentUser!!.uid, object : AppRepository.OnOrderCountRetrieveCallback{
            override fun dataDownloaded(itemCount: Long) {
                if(itemCount != 0.toLong()){
                    currentOrder.value = Order(
                        "${FirebaseAuth.getInstance().currentUser!!.uid}-cs-${itemCount + 1}",
                        System.currentTimeMillis()/1000,
                        FirebaseAuth.getInstance().currentUser!!.uid,
                        1,
                        shopId.id,
                        mutableListOf(),
                        0,
                        sourceLat = shopId.lati,
                        sourceLong = shopId.longi,
                        courierLat = shopId.lati,
                        courierLong = shopId.longi
                    )
                } else {
                    currentOrder.value = Order(
                        "${FirebaseAuth.getInstance().currentUser!!.uid}-cs-1",
                        System.currentTimeMillis()/1000,
                        FirebaseAuth.getInstance().currentUser!!.uid,
                        1,
                        shopId.id,
                        mutableListOf(),
                        0,
                        sourceLat = shopId.lati,
                        sourceLong = shopId.longi,
                        courierLat = shopId.lati,
                        courierLong = shopId.longi
                    )
                }
            }

            override fun onFailed(exception: Exception) {
                errorMessage.value = exception.message
            }

        })

        appRepository.getItems(shopId.id, object : AppRepository.OnItemRetreiveCallback {
            override fun onChildAdded(shopItem: ShopItem?) {
                addChild(shopItem!!)
            }

            override fun onChildChanged(shopItem: ShopItem?) {
                changeChild(shopItem!!)
            }

            override fun onChildDeleted(shopItem: ShopItem?) {
                deleteChild(shopItem!!)
            }

            override fun onFailed(exception: Exception) {
                errorMessage.value = exception.message
            }

        })
    }

    private fun deleteChild(item: ShopItem) {
        val position = searchArticlePosition(item.id)
        if (position != -1) {
            itemList.value!!.removeAt(position)
            deletePosition.value = position
        }
    }

    private fun changeChild(item: ShopItem) {
        val position = searchArticlePosition(item.id)
        if (position != -1) {
            itemList.value!![position] = item
            updatePosition.value = position
        }
    }

    private fun searchArticlePosition(id: String): Int {
        for (i in itemList.value!!.indices) {
            if (itemList.value!![i].id == id) return i
        }
        return -1
    }

    private fun addChild(item: ShopItem) {
        itemList.value!!.add(item)
        addPosition.value = itemList.value!!.size - 1
    }

    fun addItemToSelectedList(shopItem: ShopItem) {
        val position = searchArticlePosition(shopItem.id)
        if(position != -1){
            val item = itemList.value!![position]
            item.itemCount++
            val tempList = selectedItemList.value!!
            tempList.add(item)

            selectedItemList.value = tempList
            val order = currentOrder.value!!
            order.totalPrice = countTotalPrice()
            currentOrder.value = order
            updatePosition.value = position
        }
    }

    private fun countTotalPrice(): Long {
        var totalPrice = 0.toLong()
        for (i in selectedItemList.value!!.indices){
            totalPrice += (selectedItemList.value!![i].price * selectedItemList.value!![i].itemCount)
        }
        return totalPrice
    }

    fun removeItemFromSelectedList(shopItem: ShopItem) {
        var position = searchSelectedItemPosition(shopItem.id)
        if(position != -1 && selectedItemList.value!![position].itemCount != 1){
            val tempList = selectedItemList.value!!
            tempList[position].itemCount -= 1
            selectedItemList.value = tempList

            val order = currentOrder.value!!
            order.totalPrice = countTotalPrice()
            currentOrder.value = order
            updatePosition.value = position
        } else if(position != -1 && selectedItemList.value!![position].itemCount == 1){
            val tempList = selectedItemList.value!!
            val item = tempList[position]
            item.itemCount -= 1
            tempList.removeAt(position)
            selectedItemList.value = tempList

            val order = currentOrder.value!!
            order.totalPrice = countTotalPrice()
            currentOrder.value = order
            changeChild(item)
        }
    }

    private fun searchSelectedItemPosition(id: String): Int {
        for (i in selectedItemList.value!!.indices) {
            if (selectedItemList.value!![i].id == id) return i
        }
        return -1
    }

    fun addItemCount(shopItem: ShopItem) {
        val position = searchArticlePosition(shopItem.id)
        if(position != -1){
            val tempList = selectedItemList.value!!
            tempList[position].itemCount++
            selectedItemList.value = tempList

            val order = currentOrder.value!!
            order.totalPrice = countTotalPrice()
            currentOrder.value = order
            updatePosition.value = position
        }

    }

    fun getShopAddress(): String {
        return Geocoder(context, Locale.getDefault()).getFromLocation(
            shopId.lati,
            shopId.longi,
            1
        )[0].getAddressLine(0)!!
    }
}