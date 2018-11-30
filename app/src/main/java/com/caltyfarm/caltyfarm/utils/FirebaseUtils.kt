package com.caltyfarm.caltyfarm.utils

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

    fun getUserData(uid: String, onFirebaseUserDownload: AppRepository.OnFirebaseUserDownload) {
        getFirebaseDatabase().child("users").child(uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    onFirebaseUserDownload.onDownloadFailed(Exception("Failed to Download"))
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        val user = p0.getValue(User::class.java)
                        onFirebaseUserDownload.onUserDownloaded(user)
                    }
                }

            })
    }

    fun getArticles(onArticleDataCallback: AppRepository.OnArticleDataCallback) {
        getFirebaseDatabase().child("articles")
            .addChildEventListener(object : ChildEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    onArticleDataCallback.onFailed(p0.toException())
                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {

                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                    if (p0.exists()) {
                        val article = p0.getValue(Article::class.java)
                        onArticleDataCallback.onChildChanged(article)
                    }
                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    if (p0.exists()) {
                        val article = p0.getValue(Article::class.java)
                        onArticleDataCallback.onChildAdded(article)
                    }
                }

                override fun onChildRemoved(p0: DataSnapshot) {
                    if (p0.exists()) {
                        val article = p0.getValue(Article::class.java)
                        onArticleDataCallback.onChildDeleted(article)
                    }
                }

            })
    }

    fun getArticle(articleId: String, onDataRetrieveCallback: AppRepository.OnDataRetrieveCallback) {
        getFirebaseDatabase().child("articles").child(articleId).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                onDataRetrieveCallback.onFailed(p0.toException())
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    val article = p0.getValue(Article::class.java)
                    onDataRetrieveCallback.onDataRetrieved(article!!)
                }
            }

        })
    }

    fun postVet(vet: Vet) {
        getFirebaseDatabase().child("vet").child(vet.id).setValue(vet)
    }

    fun getVetList(onVetRetrievedCallback: AppRepository.OnVetRetrievedCallback) {
        getFirebaseDatabase().child("workerVet")
            .addChildEventListener(object : ChildEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    onVetRetrievedCallback.onFailed(p0.toException())
                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {

                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                    if (p0.exists()) {
                        val article = p0.getValue(Vet::class.java)
                        onVetRetrievedCallback.onChildChanged(article)
                    }
                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    if (p0.exists()) {
                        val article = p0.getValue(Vet::class.java)
                        onVetRetrievedCallback.onChildAdded(article)
                    }
                }

                override fun onChildRemoved(p0: DataSnapshot) {
                    if (p0.exists()) {
                        val article = p0.getValue(Vet::class.java)
                        onVetRetrievedCallback.onChildDeleted(article)
                    }
                }

            })
    }

    fun getShopList(onShopRetireveCallback: AppRepository.OnShopRetireveCallback) {
        getFirebaseDatabase().child("workerShop")
            .addChildEventListener(object : ChildEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    onShopRetireveCallback.onFailed(p0.toException())
                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                    if (p0.exists()) {
                        val article = p0.getValue(Shop::class.java)
                        onShopRetireveCallback.onChildChanged(article)
                    }
                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    if (p0.exists()) {
                        val article = p0.getValue(Shop::class.java)
                        onShopRetireveCallback.onChildAdded(article)
                    }
                }

                override fun onChildRemoved(p0: DataSnapshot) {
                    if (p0.exists()) {
                        val article = p0.getValue(Shop::class.java)
                        onShopRetireveCallback.onChildDeleted(article)
                    }
                }

            })
    }

    fun getItems(shopId: String, callback: AppRepository.OnItemRetreiveCallback) {
        getFirebaseDatabase().child("itemShop").child(shopId)
            .addChildEventListener(object: ChildEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    callback.onFailed(p0.toException())
                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {

                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                    val item = p0.getValue(ShopItem::class.java)
                    callback.onChildChanged(item!!)
                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    val item = p0.getValue(ShopItem::class.java)
                    callback.onChildAdded(item!!)
                }

                override fun onChildRemoved(p0: DataSnapshot) {
                    val item = p0.getValue(ShopItem::class.java)
                    callback.onChildDeleted(item!!)
                }

            })
    }
}