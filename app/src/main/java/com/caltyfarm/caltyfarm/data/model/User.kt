package com.caltyfarm.caltyfarm.data.model

class User(
    val uid: String = "",
    var name: String? = "",
    var userType: Int = 0,
    val phone: String = "",
    var profileUrl: String? = ""
)