package com.caltyfarm.caltyfarm.ui

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.caltyfarm.caltyfarm.R
import com.caltyfarm.caltyfarm.utils.InjectorUtils
import com.caltyfarm.caltyfarm.utils.PHONE_NUMBER_CODE
import com.caltyfarm.caltyfarm.viewmodel.VerifViewModel
import kotlinx.android.synthetic.main.activity_verification.*
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.toast

class VerificationActivity : AppCompatActivity() {


    lateinit var viewModel: VerifViewModel

    lateinit var progressDialog: ProgressDialog

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)
        progressDialog = indeterminateProgressDialog("Memverifikasi User", "Loading")
        val factory = InjectorUtils.provideVerifViewModelFactory(this, intent.getStringExtra(PHONE_NUMBER_CODE))
        viewModel = ViewModelProviders.of(this, factory).get(VerifViewModel::class.java)
        viewModel.isLoading.observe(this, Observer {
            if (it) {
                progressDialog.show()
            } else {
                if (progressDialog.isShowing) progressDialog.dismiss()
            }
        })
        viewModel.errorMessage.observe(this, Observer {
            if (it.isNotEmpty()) {
                toast(it)
            }
        })
        viewModel.user.observe(this, Observer {
            if (it != null) {
                navigateToMain()
            }
        })
        tv_burner_verif.text = intent.getStringExtra(PHONE_NUMBER_CODE)

        pin_entry.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                viewModel.manualSignIn(pin_entry.text.toString())
                true
            } else false
        }
    }

    private fun navigateToMain() {
        startActivity(Intent(this@VerificationActivity, MainActivity::class.java))
    }
}
