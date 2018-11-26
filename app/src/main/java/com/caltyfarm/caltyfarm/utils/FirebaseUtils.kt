package com.caltyfarm.caltyfarm.utils

import android.content.Context
import com.google.firebase.auth.FirebaseAuth

class FirebaseUtils(val context: Context){

    fun getFirebaseAuth() = FirebaseAuth.getInstance()
}