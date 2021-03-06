package com.caltyfarm.caltyfarm.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.caltyfarm.caltyfarm.data.AppRepository
import com.caltyfarm.caltyfarm.data.model.Cow
import com.caltyfarm.caltyfarm.viewmodel.InputCowViewModel

class InputCowViewModelFactory(
    val appRepository: AppRepository,
    val cow: Cow? = null
): ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return InputCowViewModel(appRepository, cow) as T
    }
}