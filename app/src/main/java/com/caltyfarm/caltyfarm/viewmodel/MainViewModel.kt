package com.caltyfarm.caltyfarm.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.caltyfarm.caltyfarm.data.AppRepository
import com.caltyfarm.caltyfarm.data.model.User
import com.caltyfarm.caltyfarm.ui.reminder.Reminder
import com.caltyfarm.caltyfarm.ui.reminder.ReminderDatabase
import com.caltyfarm.caltyfarm.utils.FirebaseUtils

class MainViewModel(val context: Context, val appRepository: AppRepository) : ViewModel() {

    val userData = MutableLiveData<User>()

    val reminderList = MutableLiveData<List<Reminder>>()

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

        initiateAlarmDatabase()
    }

    fun initiateAlarmDatabase() {
        val rb = ReminderDatabase(context)
        reminderList.value = rb.allReminders
    }


}