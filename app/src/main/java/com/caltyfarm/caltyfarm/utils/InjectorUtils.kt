package com.caltyfarm.caltyfarm.utils

import android.content.Context
import com.caltyfarm.caltyfarm.data.AppRepository
import com.caltyfarm.caltyfarm.data.model.Cow
import com.caltyfarm.caltyfarm.data.model.User
import com.caltyfarm.caltyfarm.ui.CowListActivity
import com.caltyfarm.caltyfarm.ui.RoomListActivity
import com.caltyfarm.caltyfarm.viewmodel.VerifViewModelFactory
import com.caltyfarm.caltyfarm.viewmodel.factory.*

object InjectorUtils {

    private fun getAppRepository() = AppRepository.getInstance(FirebaseUtils.newInstance())

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

    fun provideInputCowViewModelFactory(cow: Cow? = null): InputCowViewModelFactory {
        return InputCowViewModelFactory(getAppRepository(), cow)
    }

    fun provideCowListViewModelFactory(
        user: User,
        cowListActivity: CowListActivity
    ): CowListViewModelFactory {
        return CowListViewModelFactory(getAppRepository(), user, cowListActivity)
    }
}