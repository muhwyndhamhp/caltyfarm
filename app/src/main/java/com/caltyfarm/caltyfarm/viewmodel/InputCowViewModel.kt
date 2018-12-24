package com.caltyfarm.caltyfarm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.caltyfarm.caltyfarm.data.AppRepository
import java.util.*

class InputCowViewModel(val appRepository: AppRepository) : ViewModel() {
    val birthCalendar = MutableLiveData<Calendar>()
    val entryCalendar = MutableLiveData<Calendar>()
    val outCalendar = MutableLiveData<Calendar>()
    val pregnantCalendar = MutableLiveData<Calendar>()
    val wormCalendar = MutableLiveData<Calendar>()


    init {
        birthCalendar.value = Calendar.getInstance()
        entryCalendar.value = Calendar.getInstance()
        outCalendar.value = Calendar.getInstance()
        pregnantCalendar.value = Calendar.getInstance()
        wormCalendar.value = Calendar.getInstance()
    }

}