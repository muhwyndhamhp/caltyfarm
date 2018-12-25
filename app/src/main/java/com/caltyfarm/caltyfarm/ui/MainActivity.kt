package com.caltyfarm.caltyfarm.ui

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.caltyfarm.caltyfarm.R
import com.caltyfarm.caltyfarm.utils.InjectorUtils
import com.caltyfarm.caltyfarm.utils.USER_DATA_KEY
import com.caltyfarm.caltyfarm.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var progressDialog: ProgressDialog

    companion object {
        const val TAG = "MAIN_ACTIVITY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showLoading("Memuat data pengguna", "Harap Tunggu...")
        val factory = InjectorUtils.provideMainViewModelFactory(this)
        viewModel = ViewModelProviders.of(this, factory).get(MainViewModel::class.java)
        viewModel.userData.observe(this, androidx.lifecycle.Observer {
            if (it != null && it.uid != "") {
                when (it.userType) {
                    0 -> showInvestorLayout()
                    1 -> showAnakKandangLayout()
                    2 -> showVetLayout()
                    else -> showAnakKandangLayout()
                }
                dismissLoading()
            }
        })
    }

    private fun showInvestorLayout() {
        ll_input_sapi.visibility = View.GONE
        ll_alarm.visibility = View.GONE
        ll_list_tindakan.visibility = View.GONE

        ll_chat.onClick {
            val intent = Intent(this@MainActivity, RoomListActivity::class.java)
            intent.putExtra(USER_DATA_KEY, viewModel.userData.value)
            startActivity(intent)
        }
    }

    private fun showVetLayout() {
        ll_input_sapi.visibility = View.VISIBLE
        ll_alarm.visibility = View.VISIBLE
        ll_list_tindakan.visibility = View.VISIBLE

        ll_chat.onClick {
            val intent = Intent(this@MainActivity, RoomListActivity::class.java)
            intent.putExtra(USER_DATA_KEY, viewModel.userData.value)
            startActivity(intent)
        }
    }

    private fun showAnakKandangLayout() {
        ll_input_sapi.visibility = View.VISIBLE
        ll_alarm.visibility = View.VISIBLE
        ll_list_tindakan.visibility = View.VISIBLE

        ll_chat.onClick {
            val intent = Intent(this@MainActivity, RoomListActivity::class.java)
            intent.putExtra(USER_DATA_KEY, viewModel.userData.value)
            startActivity(intent)
        }
    }


    fun showLoading(body: String, title: String) {
        progressDialog = indeterminateProgressDialog(message = body, title = title)
        progressDialog.setProgressStyle(R.style.MyAlertDialogStyle)
        progressDialog.show()
    }

    fun dismissLoading() {
        Timer().schedule(2000) {
            if (progressDialog.isShowing) progressDialog.dismiss()
        }

    }
}
