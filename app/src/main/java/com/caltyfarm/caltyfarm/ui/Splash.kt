package com.caltyfarm.caltyfarm.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.caltyfarm.caltyfarm.BuildConfig
import com.caltyfarm.caltyfarm.R
import com.caltyfarm.caltyfarm.data.model.User
import com.caltyfarm.caltyfarm.utils.FirebaseUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
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

        FirebaseUtils.setPersistenceEnabled()
        FirebaseMessaging.getInstance().isAutoInitEnabled = true
        Timer().schedule(2000) {
            checkUserLogin()
        }
    }

    private fun checkUserLogin() {
        val currentUser = FirebaseUtils.getFirebaseAuth().currentUser
        if (currentUser == null) {
            navigateToRegister()
        } else {
            navigateToMain(currentUser.uid)
        }
    }

    private fun navigateToMain(uid: String) {
        FirebaseUtils.getFirebaseDatabase()
            .child("users")
            .child(uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    toast(p0.message)
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val user = p0.getValue(User::class.java)
                    if (user != null) {
                        Qiscus.setUser(
                            FirebaseUtils.getFirebaseAuth().currentUser!!.uid,
                            BuildConfig.MasterPassword
                        )
                            .withUsername(user.name)
                            .save(object : QiscusCore.SetUserListener {
                                override fun onSuccess(qiscusAccount: QiscusAccount?) {
                                    startActivity(Intent(this@Splash, MainActivity::class.java))
                                }

                                override fun onError(throwable: Throwable?) {
                                    toast(throwable!!.message!!)
                                }

                            })
                    } else {
                        startActivity(
                            Intent(
                                this@Splash,
                                Splash::class.java
                            ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        )
                    }

                }

            })
    }

    private fun navigateToRegister() {
        startActivity(Intent(this, AuthActivity::class.java))
    }
}
