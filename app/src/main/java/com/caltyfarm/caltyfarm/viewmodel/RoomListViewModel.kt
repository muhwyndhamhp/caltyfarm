package com.caltyfarm.caltyfarm.viewmodel

import android.content.Intent
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.caltyfarm.caltyfarm.data.AppRepository
import com.caltyfarm.caltyfarm.data.model.User
import com.caltyfarm.caltyfarm.ui.RoomListActivity
import com.qiscus.sdk.Qiscus


class RoomListViewModel(
    appRepository: AppRepository,
    val userData: User,
    private val roomListActivity: RoomListActivity
) : ViewModel() {

    val friendList = MutableLiveData<MutableList<User>>()
    val errorMessage = MutableLiveData<String>()
    val addPosition = MutableLiveData<Int>()
    val removePosition = MutableLiveData<Int>()
    val changePosition = MutableLiveData<Int>()
    val chatIntent = MutableLiveData<Intent>()

    init {
        friendList.value = mutableListOf()
        appRepository.getFriendsData(userData.companyId!!, object : AppRepository.OnUserListDataCallback {
            override fun onDataChanged(user: User?) {
                val position = searchUserPosition(user!!.uid)
                if (position != -1) {
                    friendList.value!![position] = user
                    changePosition.value = position
                }
            }

            override fun onDataAdded(user: User?) {
                if (user!!.uid != userData.uid) {
                    when (userData.userType) {
                        0, 1 -> if (user.userType == 2) {
                            friendList.value!!.add(user)
                            addPosition.value = friendList.value!!.size - 1
                        }
                        2 -> {
                            friendList.value!!.add(user)
                            addPosition.value = friendList.value!!.size - 1
                        }
                    }
                }
            }

            override fun onDataDeleted(user: User?) {
                val position = searchUserPosition(user!!.uid)
                if (position != -1) {
                    friendList.value!!.removeAt(position)
                    removePosition.value = position
                }
            }

            override fun onError(exception: Exception) {
                errorMessage.value = exception.message
            }

        })
    }

    private fun searchUserPosition(uid: String): Int {
        for (i in friendList.value!!.indices) {
            if (friendList.value!![i].uid == uid) return i
        }
        return -1
    }

    fun createQiscusChatRoom(adapterPosition: Int) {
        Qiscus.buildChatWith(friendList.value!![adapterPosition].uid)
            .build(roomListActivity, object : Qiscus.ChatActivityBuilderListener {
                override fun onSuccess(intent: Intent?) {
                    chatIntent.value = intent
                }

                override fun onError(throwable: Throwable?) {
                    errorMessage.value = throwable!!.message
                    Log.e("MAIN", throwable.message)
                }

            })
    }
}