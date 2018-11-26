package com.caltyfarm.caltyfarm.data

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


    companion object {
        private const val TAG = "REPO"

        @Volatile
        private var instance: AppRepository? = null

        fun getInstance(firebaseUtils: FirebaseUtils) =
            instance ?: synchronized(this) { instance ?: AppRepository(firebaseUtils).also { instance = it } }
    }

}