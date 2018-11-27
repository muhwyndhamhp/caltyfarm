package com.caltyfarm.caltyfarm.viewmodel.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.caltyfarm.caltyfarm.data.AppRepository
import com.caltyfarm.caltyfarm.viewmodel.ArticleViewModel

class ArticleViewModelFactory(val context: Context, val appRepository: AppRepository, val isActivity : Boolean = false, val articleId: String = ""): ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ArticleViewModel(context, appRepository, isActivity, articleId) as T
    }
}