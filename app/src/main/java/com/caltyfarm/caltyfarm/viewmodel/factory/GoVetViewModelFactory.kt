package com.caltyfarm.caltyfarm.viewmodel.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.caltyfarm.caltyfarm.data.AppRepository
import com.caltyfarm.caltyfarm.viewmodel.GoVetViewModel

class GoVetViewModelFactory(val appRepository: AppRepository, val context: Context): ViewModelProvider.NewInstanceFactory(){

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GoVetViewModel(appRepository, context) as T
    }
}