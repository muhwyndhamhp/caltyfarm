package com.caltyfarm.caltyfarm.data.model

import com.google.firebase.database.PropertyName

data class Cow(

    @set: PropertyName("i")
    @get: PropertyName("i")
    var id: Long = 0,

    @set: PropertyName("a")
    @get: PropertyName("a")
    var ageIndex: Int = 0,

    @set: PropertyName("p")
    @get: PropertyName("p")
    var parentId: Long? = 0,

    @set: PropertyName("b")
    @get: PropertyName("b")
    var birthDate: Long = 0,

    @set: PropertyName("r")
    @get: PropertyName("r")
    var breedIndex: Int = 0,

    @set: PropertyName("g")
    @get: PropertyName("g")
    var genderIndex: Int = 0,

    @set: PropertyName("e")
    @get: PropertyName("e")
    var entryDate: Long = 0,

    @set: PropertyName("o")
    @get: PropertyName("o")
    var outDate: Long? = 0,

    @set: PropertyName("w")
    @get: PropertyName("w")
    var weight: Double = 0.0,

    @set: PropertyName("s")
    @get: PropertyName("s")
    var isPregnant: Boolean? = false,

    @set: PropertyName("n")
    @get: PropertyName("n")
    var pregnantNumber: Int? = 0,

    @set: PropertyName("d")
    @get: PropertyName("d")
    var pregnantDate: Long? = 0,

    @set: PropertyName("c")
    @get: PropertyName("c")
    var actionHistoryList: List<ActionHistory>? = null,

    @set: PropertyName("l")
    @get: PropertyName("l")
    var location: String? = "",

    @set: PropertyName("h")
    @get: PropertyName("h")
    var isHealthy: Boolean = true,

    @set: PropertyName("y")
    @get: PropertyName("y")
    var companyId: String = ""
)