package com.caltyfarm.caltyfarm.ui

import android.app.ProgressDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.caltyfarm.caltyfarm.R
import com.caltyfarm.caltyfarm.utils.InjectorUtils
import com.caltyfarm.caltyfarm.viewmodel.MainViewModel
import org.jetbrains.anko.indeterminateProgressDialog
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val factory = InjectorUtils.provideMainViewModelFactory(this)
        viewModel = ViewModelProviders.of(this, factory).get(MainViewModel::class.java)

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
