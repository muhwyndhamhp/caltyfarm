package com.caltyfarm.caltyfarm.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.caltyfarm.caltyfarm.R
import com.google.firebase.auth.FirebaseAuth
import java.util.*
import kotlin.concurrent.schedule

class Splash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
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
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun navigateToRegister() {
        startActivity(Intent(this, AuthActivity::class.java))
    }
}
