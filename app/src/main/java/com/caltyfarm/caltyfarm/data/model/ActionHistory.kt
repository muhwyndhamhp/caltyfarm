package com.caltyfarm.caltyfarm.data.model

import com.google.firebase.database.PropertyName
import java.io.Serializable
import java.util.*

data class ActionHistory(

    @set: PropertyName("a")
    @get: PropertyName("a")
    var date: Long,

    @set: PropertyName("b")
    @get: PropertyName("b")
    var action: String,

    @set: PropertyName("c")
    @get: PropertyName("c")
    var condition: String,

    @set: PropertyName("d")
    @get: PropertyName("d")
    var diagnostic: String,

    @set: PropertyName("e")
    @get: PropertyName("e")
    var drugOrVaccine: String?

) : Serializable