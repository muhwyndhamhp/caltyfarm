package com.caltyfarm.caltyfarm.data

class User(
    val uid: String = "",
    var name: String? = "",
    var birthdate: Long? = 0,
    var gender: Int? = 0,
    val phone: String = "",
    var email: String? = "",
    var profileUrl: String? = ""
)