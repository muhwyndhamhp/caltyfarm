package com.caltyfarm.caltyfarm.utils

import android.content.Context
import com.caltyfarm.caltyfarm.data.AppRepository
import com.caltyfarm.caltyfarm.data.model.Order
import com.caltyfarm.caltyfarm.viewmodel.VerifViewModelFactory
import com.caltyfarm.caltyfarm.viewmodel.factory.*

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

    fun provideProfileViewModelFactory(context: Context): ProfileViewModelFactory {
        return ProfileViewModelFactory(context, getAppRepository())
    }

    fun provideArticleViewModelFactory(context: Context): ArticleViewModelFactory {
        return ArticleViewModelFactory(context, getAppRepository())
    }

    fun provideArticleViewModelFactory(
        context: Context,
        isActivity: Boolean,
        articleId: String
    ): ArticleViewModelFactory {
        return ArticleViewModelFactory(context, getAppRepository(), isActivity, articleId)
    }

    fun provideHaloCowsViewModelFactory(context: Context): HaloCowsViewModelFactory {
        return HaloCowsViewModelFactory(context, getAppRepository())
    }

    fun provideCaltyShopViewModelFactory(context: Context): CaltyShopViewModelFactory {
        return CaltyShopViewModelFactory(context, getAppRepository())
    }

    fun provideShopCatalogViewModelFactory(context: Context, shopId: String): ShopCatalogViewModelFactory {
        return ShopCatalogViewModelFactory(context, getAppRepository(), shopId)
    }

    fun provideShopOrderViewModelFactory(
        context: Context,
        order: Order,
        list: List<*>
    ): ShopOrderViewModelFactory {
        return ShopOrderViewModelFactory(context, getAppRepository(), order, list)
    }
}