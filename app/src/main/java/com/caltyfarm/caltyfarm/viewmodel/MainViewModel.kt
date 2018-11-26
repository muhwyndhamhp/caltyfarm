package com.caltyfarm.caltyfarm.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.caltyfarm.caltyfarm.data.AppRepository

class MainViewModel(val context: Context, val appRepository: AppRepository): ViewModel(){

    val fragmentPosition = MutableLiveData<Int>()

    fun setFragmentPosition(position: Int) {
        fragmentPosition.value = position
    }
}