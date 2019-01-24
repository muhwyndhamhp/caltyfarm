package com.caltyfarm.caltyfarm.utils

import com.caltyfarm.caltyfarm.data.AppRepository
import com.caltyfarm.caltyfarm.data.model.ActionHistory
import com.caltyfarm.caltyfarm.data.model.Cow
import com.caltyfarm.caltyfarm.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FirebaseUtils {

    companion object {

        fun newInstance() = FirebaseUtils()

        var firebaseDatabase: FirebaseDatabase? = null

        fun getFirebaseAuth() = FirebaseAuth.getInstance()

        fun getFirebaseDatabase(): DatabaseReference {
            if (firebaseDatabase == null) {
                firebaseDatabase = FirebaseDatabase.getInstance()
                firebaseDatabase!!.setPersistenceEnabled(true)

            }
            return firebaseDatabase!!.reference.child("caltyManager")
        }

        fun setPersistenceEnabled() {
            if (firebaseDatabase == null) {
                firebaseDatabase = FirebaseDatabase.getInstance()
                firebaseDatabase!!.setPersistenceEnabled(true)
            }
        }
    }


    fun uploadUser(userData: User) {
        getFirebaseDatabase().child("users").child(userData.uid).setValue(userData)
    }

    fun uploadCow(cowData: Cow) {
        getFirebaseDatabase().child("cows").child(cowData.companyId).child(cowData.id.toString()).setValue(cowData)
        if(cowData.actionHistoryList != null && cowData.actionHistoryList!!.isNotEmpty()){
            for (i in cowData.actionHistoryList!!.indices) {
                getFirebaseDatabase().child("actions").child(cowData.companyId)
                    .child(cowData.actionHistoryList!![i].date.toString() + "-" + cowData.id).setValue(
                        cowData.actionHistoryList!![i]
                    )
            }
        }
    }

    fun getUserData(uid: String, callback: AppRepository.OnUserDataCallback) {
        getFirebaseDatabase().child("users").child(uid).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                callback.onFailed(p0.toException())
            }

            override fun onDataChange(p0: DataSnapshot) {
                val user = p0.getValue(User::class.java)
                callback.onDataRetrieved(user)
            }

        })
    }

    fun getFriendsData(companyId: String, callback: AppRepository.OnUserListDataCallback) {
        getFirebaseDatabase().child("users").orderByChild("c").equalTo(companyId)
            .addChildEventListener(object : ChildEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    callback.onError(p0.toException())
                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {

                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                    val user = p0.getValue(User::class.java)
                    if (user != null)
                        callback.onDataChanged(user)
                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    val user = p0.getValue(User::class.java)
                    if (user != null)
                        callback.onDataAdded(user)
                }

                override fun onChildRemoved(p0: DataSnapshot) {
                    val user = p0.getValue(User::class.java)
                    if (user != null)
                        callback.onDataDeleted(user)
                }

            })
    }

    fun getCowList(companyId: String, callback: AppRepository.OnCowListDataCallback) {
        getFirebaseDatabase().child("cows").child(companyId).addChildEventListener(object: ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {
                callback.onError(p0.toException())
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val cow = p0.getValue(Cow::class.java)
                if(cow != null) callback.onDataChaned(cow)
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val cow = p0.getValue(Cow::class.java)
                if(cow != null) callback.onDataAdded(cow)
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                val cow = p0.getValue(Cow::class.java)
                if(cow != null) callback.onDataDeleted(cow)
            }

        })
    }

    fun getActionList(companyId: String, callback: AppRepository.OnActionListDataCallback) {
        getFirebaseDatabase().child("actions").child(companyId).addChildEventListener(object: ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {
                callback.onError(p0.toException())
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val cow = p0.getValue(ActionHistory::class.java)
                if(cow != null) {
                    cow.cowId = p0.key!!.split("-")[1].toInt()
                    callback.onDataChanged(cow)
                }
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val cow = p0.getValue(ActionHistory::class.java)
                if(cow != null) {
                    cow.cowId = p0.key!!.split("-")[1].toInt()
                    callback.onDataAdded(cow)
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                val cow = p0.getValue(ActionHistory::class.java)
                if(cow != null) {
                    cow.cowId = p0.key!!.split("-")[1].toInt()
                    callback.onDataDeleted(cow)
                }
            }

        })
    }
}