package com.caltyfarm.caltyfarm.viewmodel.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.caltyfarm.caltyfarm.data.AppRepository
import com.caltyfarm.caltyfarm.viewmodel.ProfileViewModel

class ProfileViewModelFactory(val context: Context, val appRepository: AppRepository): ViewModelProvider.NewInstanceFactory(){

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProfileViewModel(context, appRepository) as T
    }
}