package com.caltyfarm.caltyfarm.ui

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.caltyfarm.caltyfarm.MainActivity
import com.caltyfarm.caltyfarm.R
import com.caltyfarm.caltyfarm.utils.InjectorUtils
import com.caltyfarm.caltyfarm.utils.PHONE_NUMBER_CODE
import com.caltyfarm.caltyfarm.viewmodel.VerifViewModel
import kotlinx.android.synthetic.main.activity_verification.*
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast
import java.util.*
import kotlin.concurrent.schedule

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
            if (it){
                progressDialog.show()
            }
            else{
                if (progressDialog.isShowing) progressDialog.dismiss()
            }
        })
        viewModel.errorMessage.observe(this, Observer {
            if(it.isNotEmpty()){
                toast(it)
            }
        })
        viewModel.user.observe(this, Observer {
            if(it != null){
                navigateToMain()
            }
        })
        tv_burner_verif.text = intent.getStringExtra(PHONE_NUMBER_CODE)
        for(i in 29 downTo 1){
            Handler().postDelayed({
                tv_timer.text = "Jika dalam ${i} detik tidak menerima SMS,"
                if(i == 29) {
                    tv_resend.isClickable = true
                    tv_resend.setTextColor(resources.getColor(R.color.colorAccent))
                    tv_resend.onClick {
                        viewModel.resendCode()
                    }
                }
            }, 1000)
        }
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
    }
}
