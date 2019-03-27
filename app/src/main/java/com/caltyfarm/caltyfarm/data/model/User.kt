package com.caltyfarm.caltyfarm.data.model

import com.google.firebase.database.PropertyName
import java.io.Serializable

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
    var profileUrl: String? = "",
    @set: PropertyName("c")
    @get: PropertyName("c")
    var companyId: String? ="",
    @set: PropertyName("s")
    @get: PropertyName("s")
    var isFirstTime : Boolean = true
):Serializable