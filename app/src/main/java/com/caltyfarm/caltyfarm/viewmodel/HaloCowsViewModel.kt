package com.caltyfarm.caltyfarm.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.caltyfarm.caltyfarm.data.AppRepository
import com.caltyfarm.caltyfarm.data.model.Vet

class HaloCowsViewModel(val context: Context, val appRepository: AppRepository): ViewModel(){

    val vetList: MutableLiveData<MutableList<Vet>> = MutableLiveData()
    val updatePosition: MutableLiveData<Int> = MutableLiveData()
    val deletePosition: MutableLiveData<Int> = MutableLiveData()
    val addPosition: MutableLiveData<Int> = MutableLiveData()
    val errorMessage: MutableLiveData<String> = MutableLiveData()

    init {
        vetList.value = mutableListOf()
        initiateVetList()
    }

    private fun initiateVetList() {
        appRepository.getVetList(object: AppRepository.OnVetRetrievedCallback{
            override fun onChildAdded(article: Vet?) {
                addChild(article!!)
            }

            override fun onChildChanged(article: Vet?) {
                changeChild(article!!)
            }

            override fun onChildDeleted(article: Vet?) {
                deleteChild(article!!)
            }

            override fun onFailed(exception: Exception) {
                errorMessage.value = exception.message
            }

        })
    }

    private fun deleteChild(vet: Vet) {
        val position = searchArticlePosition(vet.id)
        if (position != -1){
            vetList.value!!.removeAt(position)
            deletePosition.value = position
        }
    }

    private fun changeChild(vet: Vet) {
        val position = searchArticlePosition(vet.id)
        if(position != -1){
            vetList.value!![position] = vet
            updatePosition.value = position
        }
    }

    private fun searchArticlePosition(id: String) : Int{
        for(i in vetList.value!!.indices){
            if(vetList.value!![i].id == id) return i
        }
        return -1
    }

    private fun addChild(vet: Vet) {
        vetList.value!!.add(vet)
        addPosition.value = vetList.value!!.size-1
    }
}