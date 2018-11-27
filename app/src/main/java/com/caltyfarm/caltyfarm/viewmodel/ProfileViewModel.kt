package com.caltyfarm.caltyfarm.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.caltyfarm.caltyfarm.data.AppRepository
import com.caltyfarm.caltyfarm.data.model.User

class ProfileViewModel(val context: Context, val appRepository: AppRepository): ViewModel(){
    fun updateUser() {
        appRepository.uploadUser(user.value!!)
    }


    val user: MutableLiveData<User> = MutableLiveData()
    val errorMessage: MutableLiveData<String> = MutableLiveData()

    init {
        appRepository.getUserData(object : AppRepository.OnUserDataCallback{
            override fun onUserDataRetrieved(p0: User) {
                user.value = p0
            }

            override fun onFailed(exception: Exception) {
                errorMessage.value = exception.message
            }

        })
    }
}