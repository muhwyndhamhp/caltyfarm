package com.caltyfarm.caltyfarm.utils

import android.content.Context
import com.caltyfarm.caltyfarm.data.AppRepository
import com.caltyfarm.caltyfarm.viewmodel.VerifViewModelFactory
import com.caltyfarm.caltyfarm.viewmodel.factory.*

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

    fun provideArticleViewModelFactory(context: Context, isActivity: Boolean, articleId: String): ArticleViewModelFactory {
        return ArticleViewModelFactory(context, getAppRepository(context), isActivity, articleId)
    }

    fun provideHaloCowsViewModelFactory(context: Context): HaloCowsViewModelFactory{
        return HaloCowsViewModelFactory(context, getAppRepository(context))
    }
}