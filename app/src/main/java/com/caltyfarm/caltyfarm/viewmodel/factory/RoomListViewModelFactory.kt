package com.caltyfarm.caltyfarm.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.caltyfarm.caltyfarm.data.AppRepository
import com.caltyfarm.caltyfarm.data.model.User
import com.caltyfarm.caltyfarm.ui.RoomListActivity
import com.caltyfarm.caltyfarm.viewmodel.RoomListViewModel

class RoomListViewModelFactory(
    private val appRepository: AppRepository,
    val userData: User,
    val roomListActivity: RoomListActivity
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RoomListViewModel(appRepository, userData, roomListActivity) as T
    }
}