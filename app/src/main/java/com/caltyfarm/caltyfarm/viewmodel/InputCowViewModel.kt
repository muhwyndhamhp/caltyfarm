package com.caltyfarm.caltyfarm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.caltyfarm.caltyfarm.data.AppRepository
import java.util.*

class InputCowViewModel(val appRepository: AppRepository) : ViewModel() {

    //fields
    val cowId = MutableLiveData<Int>()
    val ageIndex = MutableLiveData<Int>()
    val parentId = MutableLiveData<Int>()
    val birthCalendar = MutableLiveData<Calendar>()
    val breedIndex = MutableLiveData<Int>()
    val genderIndex = MutableLiveData<Int>()
    val entryCalendar = MutableLiveData<Calendar>()
    val outCalendar = MutableLiveData<Calendar>()
    val weight = MutableLiveData<Double>()
    val isPregnant = MutableLiveData<Boolean>()
    val pregnantNumber = MutableLiveData<Int>()
    val pregnantCalendar = MutableLiveData<Calendar>()
    val isPossiblePregnant = MutableLiveData<Boolean>()

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

    fun setGenderIndex(value: Int) {
        genderIndex.value = value
        isPossiblePregnant.value = (value == 1 && ageIndex.value == 0) // betina dewasa
    }

    fun setAgeIndex(value: Int) {
        ageIndex.value = value
        isPossiblePregnant.value = (value == 0 && genderIndex.value == 1) // betina dewasa
    }

}