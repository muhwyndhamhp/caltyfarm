package com.caltyfarm.caltyfarm.utils

import android.content.Context
import com.caltyfarm.caltyfarm.data.AppRepository
import com.caltyfarm.caltyfarm.data.model.User
import com.caltyfarm.caltyfarm.ui.RoomListActivity
import com.caltyfarm.caltyfarm.viewmodel.VerifViewModelFactory
import com.caltyfarm.caltyfarm.viewmodel.factory.AuthViewModelFactory
import com.caltyfarm.caltyfarm.viewmodel.factory.MainViewModelFactory
import com.caltyfarm.caltyfarm.viewmodel.factory.RoomListViewModelFactory

object InjectorUtils {

    private fun getAppRepository() = AppRepository.getInstance(FirebaseUtils)

    fun provideAuthViewModelFactory(context: Context): AuthViewModelFactory {
        return AuthViewModelFactory(context, getAppRepository())
    }

    fun provideVerifViewModelFactory(context: Context, phoneNumber: String): VerifViewModelFactory {
        return VerifViewModelFactory(context, getAppRepository(), phoneNumber)
    }

    fun provideMainViewModelFactory(context: Context): MainViewModelFactory {
        return MainViewModelFactory(context, getAppRepository())
    }

    fun provideRoomListVieModelFactory(
        userData: User,
        roomListActivity: RoomListActivity
    ): RoomListViewModelFactory {
        return RoomListViewModelFactory(getAppRepository(), userData, roomListActivity)
    }
}