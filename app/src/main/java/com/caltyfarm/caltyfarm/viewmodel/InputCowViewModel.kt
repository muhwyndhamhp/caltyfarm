package com.caltyfarm.caltyfarm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.caltyfarm.caltyfarm.data.AppRepository
import java.util.*

class InputCowViewModel(val appRepository: AppRepository) : ViewModel() {
    val idSapi = MutableLiveData<Int>()
    val birthCalendar = MutableLiveData<Calendar>()
    val breedIndex = MutableLiveData<Int>()
    val genderIndex = MutableLiveData<Int>()
    val entryCalendar = MutableLiveData<Calendar>()
    val outCalendar = MutableLiveData<Calendar>()
    val weight = MutableLiveData<Double>()
    val pregnantCalendar = MutableLiveData<Calendar>()
    val page = MutableLiveData<Int>()
    val pageTitle = MutableLiveData<String>()

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
        page.value = 0
    }

}