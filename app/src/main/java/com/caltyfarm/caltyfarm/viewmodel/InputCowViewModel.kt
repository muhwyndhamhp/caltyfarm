package com.caltyfarm.caltyfarm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.caltyfarm.caltyfarm.data.AppRepository
import com.caltyfarm.caltyfarm.data.model.ActionHistory
import com.caltyfarm.caltyfarm.data.model.Cow
import java.util.*
import kotlin.math.absoluteValue

class InputCowViewModel(val appRepository: AppRepository) : ViewModel() {


    val page = MutableLiveData<Int>()
    val pageTitle = MutableLiveData<String>()

    //fields
    val cowId = MutableLiveData<Long>()
    val ageIndex = MutableLiveData<Int>()
    val parentId = MutableLiveData<Long>()
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

    val actionHistoryList = MutableLiveData<List<ActionHistory>>()

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

        actionHistoryList.value = listOf()
    }

    fun setGenderIndex(value: Int) {
        genderIndex.value = value
        isPossiblePregnant.value = (value == 1 && ageIndex.value == 0) // betina dewasa
    }

    fun setAgeIndex(value: Int) {
        ageIndex.value = value
        isPossiblePregnant.value = (value == 0 && genderIndex.value == 1) // betina dewasa
    }

    fun addActionHistory(actionHistory: ActionHistory) {
        val oldList = actionHistoryList.value!!
        val newList = oldList + actionHistory
        actionHistoryList.value = newList
    }

    fun saveCowData() {
        val newId = cowId.value!!
        val newAgeIndex = ageIndex.value!!
        var newParentId: Long? = null
        if (ageIndex.value == 1) {
            newParentId = parentId.value
        }
        val newBirthDate = birthCalendar.value!!.timeInMillis / 1000
        val newBreedIndex = breedIndex.value!!
        val newGenderIndex = genderIndex.value!!
        val newEntryDate = entryCalendar.value!!.timeInMillis / 1000
        var newOutDate: Long? = null
        if (checkDateNotMin(outCalendar.value!!)) {
            newOutDate = outCalendar.value!!.timeInMillis / 1000
        }
        val newWeight = weight.value!!
        var newIsPregnant: Boolean? = null
        if (isPossiblePregnant.value == true) newIsPregnant = isPregnant.value ?: false
        var newPregnantNumber: Int? = null
        var newPregnantDate: Long? = null
        if (newIsPregnant == true) {
            newPregnantNumber = pregnantNumber.value
            newPregnantDate = pregnantCalendar.value!!.timeInMillis / 1000
        }
        var newActionHistoryList: List<ActionHistory>? = null
        if (actionHistoryList.value!!.isNotEmpty()) {
            newActionHistoryList = actionHistoryList.value
        }
        val newLocation: String? = null

        val cow = Cow(
            newId,
            newAgeIndex,
            newParentId,
            newBirthDate,
            newBreedIndex,
            newGenderIndex,
            newEntryDate,
            newOutDate,
            newWeight,
            newIsPregnant,
            newPregnantNumber,
            newPregnantDate,
            newActionHistoryList,
            newLocation
        )

        appRepository.uploadCowData(cow)
    }

    private fun checkDateNotMin(calendar: Calendar): Boolean {
        return calendar.time.compareTo(Date(Long.MIN_VALUE)) != 0
    }
}