package com.caltyfarm.caltyfarm.data

import com.caltyfarm.caltyfarm.data.model.Article
import com.caltyfarm.caltyfarm.data.model.User
import com.caltyfarm.caltyfarm.utils.FirebaseUtils

class AppRepository(private val firebaseUtils: FirebaseUtils) {


    interface OnFirebaseUserDownload {

        fun onUserDownloaded(user: User?)
        fun onDownloadFailed(exception: Exception)

    }

    interface OnUserDataCallback {
        fun onUserDataRetrieved(user: User)
        fun onFailed(exception: Exception)
    }

    interface OnArticleDataCallback {
        fun onChildAdded(article: Article?)
        fun onChildChanged(article: Article?)
        fun onChildDeleted(article: Article?)
        fun onFailed(exception: Exception)
    }

    fun uploadUser(userData: User) {
        firebaseUtils.uploadUser(userData)
    }

    fun getUserData(callback: OnUserDataCallback) {
        firebaseUtils.getUserData(firebaseUtils.getFirebaseAuth().uid!!, object : OnFirebaseUserDownload {
            override fun onUserDownloaded(user: User?) {
                if (user != null)
                    callback.onUserDataRetrieved(user)
            }

            override fun onDownloadFailed(exception: Exception) {
                callback.onFailed(exception)
            }

        })
    }

    fun getArticles(category: Int, callback: OnArticleDataCallback) {
        firebaseUtils.getArticles(category, object: OnArticleDataCallback{
            override fun onChildAdded(article: Article?) {
                if(article!= null) callback.onChildAdded(article)
            }

            override fun onChildChanged(article: Article?) {
                if(article!= null) callback.onChildChanged(article)
            }

            override fun onChildDeleted(article: Article?) {
                if(article!= null) callback.onChildDeleted(article)
            }

            override fun onFailed(exception: Exception) {
                callback.onFailed(exception)
            }

        })
    }


    companion object {
        private const val TAG = "REPO"

        @Volatile
        private var instance: AppRepository? = null

        fun getInstance(firebaseUtils: FirebaseUtils) =
            instance ?: synchronized(this) { instance ?: AppRepository(firebaseUtils).also { instance = it } }
    }

}