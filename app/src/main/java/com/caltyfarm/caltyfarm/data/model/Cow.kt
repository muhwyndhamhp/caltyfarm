package com.caltyfarm.caltyfarm.data.model

import com.google.firebase.database.PropertyName

data class Cow(

    @set: PropertyName("i")
    @get: PropertyName("i")
    var id: Long,

    @set: PropertyName("a")
    @get: PropertyName("a")
    var ageIndex: Int,

    @set: PropertyName("p")
    @get: PropertyName("p")
    var parentId: Long?,

    @set: PropertyName("b")
    @get: PropertyName("b")
    var birthDate: Long,

    @set: PropertyName("r")
    @get: PropertyName("r")
    var breedIndex: Int,

    @set: PropertyName("g")
    @get: PropertyName("g")
    var genderIndex: Int,

    @set: PropertyName("e")
    @get: PropertyName("e")
    var entryDate: Long,

    @set: PropertyName("o")
    @get: PropertyName("o")
    var outDate: Long?,

    @set: PropertyName("w")
    @get: PropertyName("w")
    var weight: Double,

    @set: PropertyName("s")
    @get: PropertyName("s")
    var isPregnant: Boolean?,

    @set: PropertyName("n")
    @get: PropertyName("n")
    var pregnantNumber: Int?,

    @set: PropertyName("d")
    @get: PropertyName("d")
    var pregnantDate: Long?,

    @set: PropertyName("c")
    @get: PropertyName("c")
    var actionHistoryList: List<ActionHistory>?,

    @set: PropertyName("l")
    @get: PropertyName("l")
    var location: String?
)