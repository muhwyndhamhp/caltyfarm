package com.caltyfarm.caltyfarm.data

import androidx.lifecycle.MutableLiveData
import com.caltyfarm.caltyfarm.data.model.*
import com.caltyfarm.caltyfarm.utils.FirebaseUtils

class AppRepository(private val firebaseUtils: FirebaseUtils) {


    interface OnFirebaseUserDownload {
        fun onUserDownloaded(user: User?)
        fun onDownloadFailed(exception: Exception)
    }

    interface OnDataRetrieveCallback {
        fun onDataRetrieved(user: User)
        fun onDataRetrieved(article: Article)
        fun onFailed(exception: Exception)
    }

    interface OnVetRetrievedCallback {
        fun onChildAdded(article: Vet?)
        fun onChildChanged(article: Vet?)
        fun onChildDeleted(article: Vet?)
        fun onFailed(exception: Exception)
    }

    interface OnArticleDataCallback {
        fun onChildAdded(article: Article?)
        fun onChildChanged(article: Article?)
        fun onChildDeleted(article: Article?)
        fun onFailed(exception: Exception)
    }

    interface OnShopRetireveCallback {
        fun onChildAdded(shop: Shop?)
        fun onChildChanged(shop: Shop?)
        fun onChildDeleted(shop: Shop?)
        fun onFailed(exception: Exception)
    }

    interface OnItemRetreiveCallback{
        fun onChildAdded(shopItem: ShopItem?)
        fun onChildChanged(shopItem: ShopItem?)
        fun onChildDeleted(shopItem: ShopItem?)
        fun onFailed(exception: Exception)
    }

    interface OnOrderCountRetrieveCallback{
        fun dataDownloaded(itemCount: Long)
        fun onFailed(exception: Exception)
    }

    fun uploadUser(userData: User) {
        firebaseUtils.uploadUser(userData)
    }

    fun getUserData(retrieveCallback: OnDataRetrieveCallback) {
        firebaseUtils.getUserData(firebaseUtils.getFirebaseAuth().uid!!, object : OnFirebaseUserDownload {
            override fun onUserDownloaded(user: User?) {
                if (user != null)
                    retrieveCallback.onDataRetrieved(user)
            }

            override fun onDownloadFailed(exception: Exception) {
                retrieveCallback.onFailed(exception)
            }

        })
    }

    fun getArticles(category: Int, callback: OnArticleDataCallback) {
        firebaseUtils.getArticles(object : OnArticleDataCallback {
            override fun onChildAdded(article: Article?) {
                if (article != null) callback.onChildAdded(article)
            }

            override fun onChildChanged(article: Article?) {
                if (article != null) callback.onChildChanged(article)
            }

            override fun onChildDeleted(article: Article?) {
                if (article != null) callback.onChildDeleted(article)
            }

            override fun onFailed(exception: Exception) {
                callback.onFailed(exception)
            }

        })
    }

    fun getSingleArticle(articleId: String, callback: OnDataRetrieveCallback) {
        firebaseUtils.getArticle(articleId, object : OnDataRetrieveCallback {
            override fun onDataRetrieved(user: User) {
            }

            override fun onDataRetrieved(article: Article) {
                callback.onDataRetrieved(article)
            }

            override fun onFailed(exception: Exception) {
                callback.onFailed(exception)
            }

        })
    }

    fun postVet(value: MutableList<Vet>) {
        for (i in value.indices) {
            firebaseUtils.postVet(value[i])
        }
    }

    fun getVetList(callback: OnVetRetrievedCallback) {
        firebaseUtils.getVetList(object : OnVetRetrievedCallback {
            override fun onChildAdded(article: Vet?) {
                if (article != null) callback.onChildAdded(article)
            }

            override fun onChildChanged(article: Vet?) {
                if (article != null) callback.onChildChanged(article)
            }

            override fun onChildDeleted(article: Vet?) {
                if (article != null) callback.onChildDeleted(article)
            }

            override fun onFailed(exception: Exception) {
                callback.onFailed(exception)
            }

        })
    }

    fun getShops(onShopRetireveCallback: OnShopRetireveCallback) {
        firebaseUtils.getShopList(object : OnShopRetireveCallback{
            override fun onChildAdded(shop: Shop?) {
                if(shop != null) onShopRetireveCallback.onChildAdded(shop)
            }

            override fun onChildChanged(shop: Shop?) {
                if(shop != null) onShopRetireveCallback.onChildChanged(shop)
            }

            override fun onChildDeleted(shop: Shop?) {
                if(shop != null) onShopRetireveCallback.onChildDeleted(shop)
            }

            override fun onFailed(exception: Exception) {
                onShopRetireveCallback.onFailed(exception)
            }

        })
    }

    fun getItems(shopId: String, callback: OnItemRetreiveCallback) {
        firebaseUtils.getItems(shopId, callback)
    }

    fun getOrderListCount(uid: String, onOrderCountRetrieveCallback: OnOrderCountRetrieveCallback) {
        firebaseUtils.getOrderListCount(uid, onOrderCountRetrieveCallback)
    }

    fun postOrder(orderData: MutableLiveData<Order>) {
        firebaseUtils.postOrder(orderData)
    }


    companion object {
        private const val TAG = "REPO"

        @Volatile
        private var instance: AppRepository? = null

        fun getInstance(firebaseUtils: FirebaseUtils) =
            instance ?: synchronized(this) { instance ?: AppRepository(firebaseUtils).also { instance = it } }
    }

}