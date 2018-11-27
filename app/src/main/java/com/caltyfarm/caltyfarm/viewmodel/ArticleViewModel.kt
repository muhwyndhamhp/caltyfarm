package com.caltyfarm.caltyfarm.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.caltyfarm.caltyfarm.data.AppRepository
import com.caltyfarm.caltyfarm.data.model.Article
import com.caltyfarm.caltyfarm.data.model.User

class ArticleViewModel(val context: Context, val appRepository: AppRepository, val isActivity : Boolean = false, val articleId: String = ""): ViewModel(){

    val articleList : MutableLiveData<MutableList<Article>> = MutableLiveData()
    val singleArticle: MutableLiveData<Article> = MutableLiveData()
    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val updatePosition: MutableLiveData<Int> = MutableLiveData()
    val deletePosition: MutableLiveData<Int> = MutableLiveData()
    val addPosition: MutableLiveData<Int> = MutableLiveData()
    var category: Int = 0

    init {
        if(isActivity){
            appRepository.getSingleArticle(articleId, object : AppRepository.OnDataRetrieveCallback{
                override fun onDataRetrieved(user: User) {

                }

                override fun onDataRetrieved(article: Article) {
                    singleArticle.value = article
                }

                override fun onFailed(exception: Exception) {
                    errorMessage.value = exception.message
                }

            })
        }
        else{
            articleList.value = mutableListOf()
            getArticle(category)
        }
    }

    private fun getArticle(category: Int) {
        appRepository.getArticles(category, object : AppRepository.OnArticleDataCallback{
            override fun onChildAdded(article: Article?) {
                addChild(article!!)
            }

            override fun onChildChanged(article: Article?) {
                changeChild(article!!)
            }

            override fun onChildDeleted(article: Article?) {
                deleteChild(article!!)
            }

            override fun onFailed(exception: Exception) {
                errorMessage.value = exception.message
            }

        })
    }

    private fun deleteChild(article: Article) {
        val position = searchArticlePosition(article.id)
        if (position != -1){
            articleList.value!!.removeAt(position)
            deletePosition.value = position
        }
    }

    private fun changeChild(article: Article) {
        val position = searchArticlePosition(article.id)
        if(position != -1){
            articleList.value!![position] = article
            updatePosition.value = position
        }
    }

    private fun searchArticlePosition(id: String) : Int{
        for(i in articleList.value!!.indices){
            if(articleList.value!![i].id == id) return i
        }
        return -1
    }

    private fun addChild(article: Article) {
        articleList.value!!.add(article)
        addPosition.value = articleList.value!!.size-1
    }
}