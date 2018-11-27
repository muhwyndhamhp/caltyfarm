package com.caltyfarm.caltyfarm.viewmodel

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.caltyfarm.caltyfarm.R
import com.caltyfarm.caltyfarm.data.AppRepository
import com.caltyfarm.caltyfarm.data.model.User
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class VerifViewModel(val context: Context, val appRepository: AppRepository, private val initialPhoneNumber: String) :
    ViewModel() {

    val isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val user: MutableLiveData<FirebaseUser> = MutableLiveData()
    private lateinit var callback: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var verifId: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    init {
        isLoading.value = true
        startPhoneAuth()
    }

    private fun startPhoneAuth() {
        callback = object: PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(p0)
            }

            override fun onVerificationFailed(p0: FirebaseException?) {
                isLoading.value = false
                errorMessage.value = context.getString(R.string.verif_failed)
                Log.e("ERROR", p0!!.message)
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                isLoading.value = false
                verifId = p0
                resendToken = p1
            }
        }

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            initialPhoneNumber,
            60,
            TimeUnit.SECONDS,
            context as Activity,
            callback
        )
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    user.value = it.result!!.user
                    initiateUser()

                } else {
                    errorMessage.value = context.getString(R.string.verif_failed)
                    Log.e("ERROR", it.exception!!.message)
                }
            }
    }

    private fun initiateUser() {
        val userData = User(
            user.value!!.uid,
            "",
            0,
            0,
            initialPhoneNumber,
            "",
            ""
        )

        appRepository.uploadUser(userData)
    }

    fun manualSignIn(text: String) {
        val credential = PhoneAuthProvider.getCredential(verifId, text)
        signInWithPhoneAuthCredential(credential)
    }
}
