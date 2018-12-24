package com.caltyfarm.caltyfarm.ui

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.caltyfarm.caltyfarm.R
import com.google.firebase.auth.FirebaseAuth
import com.qiscus.sdk.Qiscus
import com.qiscus.sdk.chat.core.QiscusCore
import com.qiscus.sdk.chat.core.data.model.QiscusAccount
import org.jetbrains.anko.toast
import java.util.*
import kotlin.concurrent.schedule

class Splash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Qiscus.init(applicationContext as Application?, resources.getString(R.string.APP_ID))
        Timer().schedule(2000){
            checkUserLogin()
        }
    }

    private fun checkUserLogin() {
        if(FirebaseAuth.getInstance().currentUser == null){
            navigateToRegister()
        } else {
            navigateToMain()
        }
    }

    private fun navigateToMain() {
        Qiscus.setUser(FirebaseAuth.getInstance().currentUser!!.uid, "19081997")
            .withUsername("Muhammad Wyndham Haryata Permana")
            .save(object : QiscusCore.SetUserListener{
                override fun onSuccess(qiscusAccount: QiscusAccount?) {
                    startActivity(Intent(this@Splash, MainActivity::class.java))
                }

                override fun onError(throwable: Throwable?) {
                    toast(throwable!!.message!!)
                }

            })
    }

    private fun navigateToRegister() {
        startActivity(Intent(this, AuthActivity::class.java))
    }
}
