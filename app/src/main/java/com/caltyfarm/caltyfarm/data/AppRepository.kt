package com.caltyfarm.caltyfarm.data

import com.caltyfarm.caltyfarm.data.model.User
import com.caltyfarm.caltyfarm.utils.FirebaseUtils

class AppRepository(private val firebaseUtils: FirebaseUtils) {

    fun uploadUser(userData: User) {
        firebaseUtils.uploadUser(userData)
    }

    companion object {
        private const val TAG = "REPO"

        @Volatile
        private var instance: AppRepository? = null

        fun getInstance(firebaseUtils: FirebaseUtils) =
            instance ?: synchronized(this) { instance ?: AppRepository(firebaseUtils).also { instance = it } }
    }

}