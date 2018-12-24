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
        birthCalendar.value = Calendar.getInstance().also {
            it.time = Date(Long.MIN_VALUE)
        }
        entryCalendar.value = Calendar.getInstance().also {
            it.time = Date(Long.MIN_VALUE)
        }
        outCalendar.value = Calendar.getInstance().also {
            it.time = Date(Long.MIN_VALUE)
        }
        pregnantCalendar.value = Calendar.getInstance().also {
            it.time = Date(Long.MIN_VALUE)
        }
        wormCalendar.value = Calendar.getInstance().also {
            it.time = Date(Long.MIN_VALUE)
        }
    }

}