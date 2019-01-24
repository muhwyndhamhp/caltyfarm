package com.caltyfarm.caltyfarm.data.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.PropertyName
import java.io.Serializable
import java.util.*

data class ActionHistory(

    @set: PropertyName("a")
    @get: PropertyName("a")
    var date: Long = 0,

    @set: PropertyName("b")
    @get: PropertyName("b")
    var action: String = "",

    @set: PropertyName("c")
    @get: PropertyName("c")
    var condition: String = "",

    @set: PropertyName("d")
    @get: PropertyName("d")
    var diagnostic: String = "",

    @set: PropertyName("e")
    @get: PropertyName("e")
    var drugOrVaccine: String? = "",

    @Exclude
    var cowId: Int? = 0

) : Serializable