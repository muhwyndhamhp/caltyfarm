package com.caltyfarm.caltyfarm.utils

import androidx.lifecycle.MutableLiveData
import com.caltyfarm.caltyfarm.data.AppRepository
import com.caltyfarm.caltyfarm.data.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

object FirebaseUtils {

    fun getFirebaseAuth() = FirebaseAuth.getInstance()

    fun getFirebaseDatabase() =
        FirebaseDatabase.getInstance().reference.child("caltyManager")

    fun uploadUser(userData: User) {
        getFirebaseDatabase().child("users").child(userData.uid).setValue(userData)
    }

    fun getUserData(uid: String, callback: AppRepository.OnUserDataCallback) {
        getFirebaseDatabase().child("users").child(uid).addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                callback.onFailed(p0.toException())
            }

            override fun onDataChange(p0: DataSnapshot) {
                val user = p0.getValue(User::class.java)
                callback.onDataRetrieved(user)
            }

        })
    }
}