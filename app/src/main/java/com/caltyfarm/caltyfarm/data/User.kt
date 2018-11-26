package com.caltyfarm.caltyfarm.data

class User(
    val uid: String,
    var name: String?,
    var birthdate: Long?,
    var gender: Int?,
    val phone: String,
    var email: String?,
    var profileUrl: String?
)