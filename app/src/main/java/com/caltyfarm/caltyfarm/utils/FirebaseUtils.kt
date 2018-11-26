package com.caltyfarm.caltyfarm.utils

import android.content.Context
import com.caltyfarm.caltyfarm.data.AppRepository
import com.caltyfarm.caltyfarm.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception

class FirebaseUtils(val context: Context) {

    fun getFirebaseAuth() = FirebaseAuth.getInstance()

    fun getFirebaseDatabase() =
        FirebaseDatabase.getInstance().reference.child("caltyApp").child("users")

    fun uploadUser(userData: User) {
        getFirebaseDatabase().child(userData.uid).setValue(userData)
    }

    fun getUserData(uid: String, onFirebaseUserDownload: AppRepository.OnFirebaseUserDownload){
        getFirebaseDatabase().child(uid)
            .addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                onFirebaseUserDownload.onDownloadFailed(Exception("Failed to Download"))
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    val user = p0.getValue(User::class.java)
                    onFirebaseUserDownload.onUserDownloaded(user)
                }
            }

        })
    }
}