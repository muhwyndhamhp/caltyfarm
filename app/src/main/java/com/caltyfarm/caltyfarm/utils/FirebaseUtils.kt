package com.caltyfarm.caltyfarm.utils

import androidx.lifecycle.MutableLiveData
import com.caltyfarm.caltyfarm.data.AppRepository
import com.caltyfarm.caltyfarm.data.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

object FirebaseUtils {

    fun getFirebaseAuth() = FirebaseAuth.getInstance()

    fun getFirebaseDatabase() =
        FirebaseDatabase.getInstance().reference.child("caltyApp")

    fun uploadUser(userData: User) {
        getFirebaseDatabase().child("users").child(userData.uid).setValue(userData)
    }
}