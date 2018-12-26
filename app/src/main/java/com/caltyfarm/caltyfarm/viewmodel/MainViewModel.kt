package com.caltyfarm.caltyfarm.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.caltyfarm.caltyfarm.data.AppRepository
import com.caltyfarm.caltyfarm.data.model.User
import com.caltyfarm.caltyfarm.utils.FirebaseUtils

class MainViewModel(val context: Context, val appRepository: AppRepository) : ViewModel() {

    val userData = MutableLiveData<User>()

    var errorMessages: MutableLiveData<String> = MutableLiveData()

    init {
        appRepository.getUserData(
            FirebaseUtils.getFirebaseAuth().currentUser!!.uid,
            object : AppRepository.OnUserDataCallback {
                override fun onDataRetrieved(user: User?) {
                    if (user != null) {
                        userData.value = user
                        if (userData.value!!.isFirstTime) {
                        }
                    }
                }

                override fun onFailed(exception: Exception) {
                    errorMessages.value = exception.message!!
                }

            })
    }
}