package com.caltyfarm.caltyfarm.viewmodel

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.caltyfarm.caltyfarm.data.AppRepository
import com.caltyfarm.caltyfarm.data.model.Cow
import com.caltyfarm.caltyfarm.data.model.User
import com.caltyfarm.caltyfarm.ui.cowdetail.CowDetailActivity
import com.caltyfarm.caltyfarm.ui.CowListActivity
import com.caltyfarm.caltyfarm.utils.COW_DATA_KEY

class CowListViewModel(
    val appRepository: AppRepository,
    private val userData: User,
    private val cowListActivity: CowListActivity
) : ViewModel(){
    
    val cowList = MutableLiveData<MutableList<Cow>>()
    val errorMessage = MutableLiveData<String>()
    val addPosition = MutableLiveData<Int>()
    val removePosition = MutableLiveData<Int>()
    val changePosition = MutableLiveData<Int>()
    val cowIntent = MutableLiveData<Intent>()
    
    init {
        cowList.value  = mutableListOf()

        cowList.value = mutableListOf()
        appRepository.getCowList(userData.companyId!!, object : AppRepository.OnCowListDataCallback {
            override fun onDataChaned(cow: Cow?) {
                val position = searchUserPosition(cow!!.id)
                if (position != -1) {
                    cowList.value!![position] = cow
                    changePosition.value = position
                }
            }

            override fun onDataAdded(cow: Cow?) {
                cowList.value!!.add(cow!!)
                addPosition.value = cowList.value!!.size - 1
            }

            override fun onDataDeleted(cow: Cow?) {
                val position = searchUserPosition(cow!!.id)
                if (position != -1) {
                    cowList.value!!.removeAt(position)
                    removePosition.value = position
                }
            }

            override fun onError(exception: Exception) {
                errorMessage.value = exception.message
            }

        })
    }

    private fun searchUserPosition(cowId: Long): Int {
        for (i in cowList.value!!.indices) {
            if (cowList.value!![i].id == cowId) return i
        }
        return -1
    }
    fun showCowData(adapterPosition: Int) {
        val intent =Intent(cowListActivity, CowDetailActivity::class.java)
        intent.putExtra(COW_DATA_KEY, cowList.value!![adapterPosition])
        cowIntent.value = intent
    }

}