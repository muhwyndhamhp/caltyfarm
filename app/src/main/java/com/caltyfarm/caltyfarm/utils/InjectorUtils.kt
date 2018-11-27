package com.caltyfarm.caltyfarm.utils

import android.content.Context
import com.caltyfarm.caltyfarm.data.AppRepository
import com.caltyfarm.caltyfarm.viewmodel.VerifViewModelFactory
import com.caltyfarm.caltyfarm.viewmodel.factory.ArticleViewModelFactory
import com.caltyfarm.caltyfarm.viewmodel.factory.AuthViewModelFactory
import com.caltyfarm.caltyfarm.viewmodel.factory.MainViewModelFactory
import com.caltyfarm.caltyfarm.viewmodel.factory.ProfileViewModelFactory

object InjectorUtils {

    private fun getAppRepository(context: Context) = AppRepository.getInstance(FirebaseUtils(context))

    fun provideAuthViewModelFactory(context: Context): AuthViewModelFactory {
        return AuthViewModelFactory(context, getAppRepository(context))
    }

    fun provideVerifViewModelFactory(context: Context, phoneNumber: String): VerifViewModelFactory {
        return VerifViewModelFactory(context, getAppRepository(context), phoneNumber)
    }

    fun provideMainViewModelFactory(context: Context): MainViewModelFactory {
        return MainViewModelFactory(context, getAppRepository(context))
    }

    fun provideProfileViewModelFactory(context: Context): ProfileViewModelFactory {
        return ProfileViewModelFactory(context, getAppRepository(context))
    }

    fun provideArticleViewModelFactory(context: Context): ArticleViewModelFactory {
        return ArticleViewModelFactory(context, getAppRepository(context))
    }
}