package com.caltyfarm.caltyfarm.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.caltyfarm.caltyfarm.R
import com.caltyfarm.caltyfarm.data.AppRepository
import com.caltyfarm.caltyfarm.data.model.ActionHistory
import com.caltyfarm.caltyfarm.data.model.User
import com.caltyfarm.caltyfarm.utils.FirebaseUtils
import com.caltyfarm.caltyfarm.utils.TableUtils
import com.caltyfarm.caltyfarm.utils.USER_DATA_KEY
import kotlinx.android.synthetic.main.activity_action_list.*
import kotlinx.android.synthetic.main.fragment_input_action_history_fragment.view.*
import org.jetbrains.anko.toast
import java.lang.Exception

class ActionListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_action_list)
        getActionList(intent.getSerializableExtra(USER_DATA_KEY) as User)
        actionList.observe(this, Observer { showActionHistoryTable(web_view_action, it) })
    }

    val actionList = MutableLiveData<MutableList<ActionHistory>>()

    private fun getActionList(user: User) {
        actionList.value = mutableListOf()
        AppRepository.getInstance(FirebaseUtils()).getActionList(user.companyId!!, object: AppRepository.OnActionListDataCallback{
            override fun onDataChanged(actionHistory: ActionHistory?) {
                val position  = searchUserPosition(actionHistory!!.date)
                if(position != -1){
                    val temp= actionList.value!!
                    temp.set(position, actionHistory)
                    actionList.value = temp
                }
            }

            override fun onDataAdded(actionHistory: ActionHistory?) {
                val temp = actionList.value!!
                temp.add(actionHistory!!)
                actionList.value = temp
            }

            override fun onDataDeleted(actionHistory: ActionHistory?) {
                val position  = searchUserPosition(actionHistory!!.date)
                if(position != -1){
                    val temp= actionList.value!!
                    temp.removeAt(position)
                    actionList.value = temp
                }
            }

            override fun onError(exception: Exception) {
                toast(exception.message!!)
            }

        })
    }

    private fun searchUserPosition(actionTime: Long): Int {
        for (i in actionList.value!!.indices) {
            if (actionList.value!![i].date== actionTime) return i
        }
        return -1
    }

    private fun showActionHistoryTable(view: WebView, actionHistoryList: List<ActionHistory>) {
        if (actionHistoryList.isEmpty()) {
            view.loadData("Tidak ada data", "text/html", "UTF-8")
        } else {
            view.loadData(
                TableUtils.listOfActionHistoryActivityHtml(actionHistoryList), "text/html", "UTF-8"
            )
        }
    }
}
