package com.caltyfarm.caltyfarm.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.caltyfarm.caltyfarm.data.AppRepository

class VerifViewModelFactory(val context: Context, val appRepository: AppRepository, val initalPhonenumber: String) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return VerifViewModel(context, appRepository, initalPhonenumber) as T
    }
}