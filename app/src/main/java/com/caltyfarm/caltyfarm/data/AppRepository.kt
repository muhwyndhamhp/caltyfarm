package com.caltyfarm.caltyfarm.data

import com.caltyfarm.caltyfarm.data.model.ActionHistory
import com.caltyfarm.caltyfarm.data.model.Cow
import com.caltyfarm.caltyfarm.data.model.User
import com.caltyfarm.caltyfarm.utils.FirebaseUtils
import java.lang.Exception

class AppRepository(private val firebaseUtils: FirebaseUtils) {

    interface OnUserDataCallback {
        fun onDataRetrieved(user: User?)
        fun onFailed(exception: Exception)
    }

    interface OnUserListDataCallback {
        fun onDataChanged(user: User?)
        fun onDataAdded(user: User?)
        fun onDataDeleted(user: User?)
        fun onError(exception: Exception)
    }

    interface OnCowListDataCallback {
        fun onDataChaned(cow: Cow?)
        fun onDataAdded(cow: Cow?)
        fun onDataDeleted(cow: Cow?)
        fun onError(exception: Exception)
    }
    interface OnActionListDataCallback {
        fun onDataChanged(actionHistory: ActionHistory?)
        fun onDataAdded(actionHistory: ActionHistory?)
        fun onDataDeleted(actionHistory: ActionHistory?)
        fun onError(exception: Exception)
    }
    fun uploadUser(userData: User) {
        firebaseUtils.uploadUser(userData)
    }

    fun getUserData(uid: String, callback: OnUserDataCallback) {
        firebaseUtils.getUserData(uid, callback)
    }

    fun getFriendsData(companyId: String, callback: OnUserListDataCallback) {
        firebaseUtils.getFriendsData(companyId, callback)
    }

    fun uploadCowData(cowData: Cow) {
        firebaseUtils.uploadCow(cowData)
    }

    fun getCowList(companyId: String, callback : OnCowListDataCallback){
        firebaseUtils.getCowList(companyId, callback)
    }

    fun getActionList(companyId: String, callback : OnActionListDataCallback){
        firebaseUtils.getActionList(companyId, callback)
    }

    companion object {
        private const val TAG = "REPO"

        @Volatile
        private var instance: AppRepository? = null

        fun getInstance(firebaseUtils: FirebaseUtils) =
            instance ?: synchronized(this) { instance ?: AppRepository(firebaseUtils).also { instance = it } }
    }

}