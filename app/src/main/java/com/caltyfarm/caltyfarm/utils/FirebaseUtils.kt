package com.caltyfarm.caltyfarm.utils

import android.content.Context
import com.caltyfarm.caltyfarm.data.AppRepository
import com.caltyfarm.caltyfarm.data.model.Article
import com.caltyfarm.caltyfarm.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.lang.Exception

class FirebaseUtils(val context: Context) {

    fun getFirebaseAuth() = FirebaseAuth.getInstance()

    fun getFirebaseDatabase() =
        FirebaseDatabase.getInstance().reference.child("caltyApp")

    fun uploadUser(userData: User) {
        getFirebaseDatabase().child("users").child(userData.uid).setValue(userData)
    }

    fun getUserData(uid: String, onFirebaseUserDownload: AppRepository.OnFirebaseUserDownload){
        getFirebaseDatabase().child("users").child(uid)
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

    fun getArticles(category: Int, onArticleDataCallback: AppRepository.OnArticleDataCallback) {
        getFirebaseDatabase().child("articles")
            .addChildEventListener(object : ChildEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    onArticleDataCallback.onFailed(p0.toException())
                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {

                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                    if(p0.exists()){
                        val article = p0.getValue(Article::class.java)
                        onArticleDataCallback.onChildChanged(article)
                    }
                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    if(p0.exists()){
                        val article = p0.getValue(Article::class.java)
                        onArticleDataCallback.onChildAdded(article)
                    }
                }

                override fun onChildRemoved(p0: DataSnapshot) {
                    if(p0.exists()){
                        val article = p0.getValue(Article::class.java)
                        onArticleDataCallback.onChildDeleted(article)
                    }
                }

            })
    }
}