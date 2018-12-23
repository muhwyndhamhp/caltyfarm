package com.caltyfarm.caltyfarm.data.model

import com.google.firebase.database.PropertyName

class User(
    @set: PropertyName("i")
    @get: PropertyName("i")
    var uid: String = "",
    @set: PropertyName("n")
    @get: PropertyName("n")
    var name: String? = "",
    @set: PropertyName("t")
    @get: PropertyName("t")
    var userType: Int = 0,
    @set: PropertyName("p")
    @get: PropertyName("p")
    var phone: String = "",
    @set: PropertyName("f")
    @get: PropertyName("f")
    var profileUrl: String? = ""
)