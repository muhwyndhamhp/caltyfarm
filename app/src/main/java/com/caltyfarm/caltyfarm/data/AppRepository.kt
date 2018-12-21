package com.caltyfarm.caltyfarm.data

import com.caltyfarm.caltyfarm.data.model.User
import com.caltyfarm.caltyfarm.utils.FirebaseUtils
import java.lang.Exception

class AppRepository(private val firebaseUtils: FirebaseUtils) {

    interface OnUserDataCallback {
        fun onDataRetrieved(user: User?)
        fun onFailed(exception: Exception)
    }
    fun uploadUser(userData: User) {
        firebaseUtils.uploadUser(userData)
    }

    fun getUserData(uid: String, callback: OnUserDataCallback) {
        firebaseUtils.getUserData(uid, callback)
    }

    companion object {
        private const val TAG = "REPO"

        @Volatile
        private var instance: AppRepository? = null

        fun getInstance(firebaseUtils: FirebaseUtils) =
            instance ?: synchronized(this) { instance ?: AppRepository(firebaseUtils).also { instance = it } }
    }

}