package com.caltyfarm.caltyfarm.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.caltyfarm.caltyfarm.BuildConfig
import com.caltyfarm.caltyfarm.R
import com.caltyfarm.caltyfarm.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        Timer().schedule(2000) {
            checkUserLogin()
        }
    }

    private fun checkUserLogin() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            navigateToRegister()
        } else {
            navigateToMain(currentUser.uid)
        }
    }

    private fun navigateToMain(uid: String) {
        FirebaseDatabase.getInstance().reference
            .child("caltyManager")
            .child("users")
            .child(uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    toast(p0.message)
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val user = p0.getValue(User::class.java)
                    Qiscus.setUser(
                        FirebaseAuth.getInstance().currentUser!!.uid,
                        BuildConfig.MasterPassword
//                "19081997"
                    )
                        .withUsername(user!!.name)
                        .save(object : QiscusCore.SetUserListener {
                            override fun onSuccess(qiscusAccount: QiscusAccount?) {
                                startActivity(Intent(this@Splash, MainActivity::class.java))
                            }

                            override fun onError(throwable: Throwable?) {
                                toast(throwable!!.message!!)
                            }

                        })
                }

            })
    }

    private fun navigateToRegister() {
        startActivity(Intent(this, AuthActivity::class.java))
    }
}
