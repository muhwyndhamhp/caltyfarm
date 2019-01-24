package com.caltyfarm.caltyfarm.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.caltyfarm.caltyfarm.data.AppRepository
import com.caltyfarm.caltyfarm.data.model.User
import com.caltyfarm.caltyfarm.ui.CowListActivity
import com.caltyfarm.caltyfarm.viewmodel.CowListViewModel

class CowListViewModelFactory(
    val appRepository: AppRepository,
    val user: User,
    val cowListActivity: CowListActivity
): ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CowListViewModel(appRepository, user, cowListActivity) as T
    }
}