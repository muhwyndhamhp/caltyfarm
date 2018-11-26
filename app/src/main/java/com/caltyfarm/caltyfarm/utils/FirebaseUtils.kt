package com.caltyfarm.caltyfarm.utils

import android.content.Context
import com.caltyfarm.caltyfarm.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class FirebaseUtils(val context: Context) {

    fun getFirebaseAuth() = FirebaseAuth.getInstance()

    fun getFirebaseDatabase() =
        FirebaseDatabase.getInstance().reference.child("caltyApp").child("users")

    fun uploadUser(userData: User) {
        getFirebaseDatabase().child(userData.uid).setValue(userData)
    }
}