package com.caltyfarm.caltyfarm.data.model

import com.google.firebase.database.PropertyName

data class Cow(

    @set: PropertyName("i")
    @get: PropertyName("i")
    var id: Long,

    @set: PropertyName("r")
    @get: PropertyName("r")
    var birthDate: Long,

    @set: PropertyName("b")
    @get: PropertyName("b")
    var breed: String,

    @set: PropertyName("g")
    @get: PropertyName("g")
    var gender: String,

    @set: PropertyName("e")
    @get: PropertyName("e")
    var entryDate: Long,

    @set: PropertyName("o")
    @get: PropertyName("o")
    var outDate: Long?,

    @set: PropertyName("a")
    @get: PropertyName("a")
    var tag: String,

    @set: PropertyName("w")
    @get: PropertyName("w")
    var weight: Double,

    @set: PropertyName("p")
    @get: PropertyName("p")
    var pregnantDate: Long?,

    @set: PropertyName("s")
    @get: PropertyName("s")
    var estimatedBirthDate: Long?,

    @set: PropertyName("v")
    @get: PropertyName("v")
    var vaccineStatus: String,

    @set: PropertyName("m")
    @get: PropertyName("m")
    var parasiteWormDrug: String?,

    @set: PropertyName("c")
    @get: PropertyName("c")
    var caseHistory: String,

    @set: PropertyName("t")
    @get: PropertyName("t")
    var temperature :Double?,

    @set: PropertyName("n")
    @get: PropertyName("n")
    var tonusRumen: String?,

    @set: PropertyName("j")
    @get: PropertyName("j")
    var insemination: String,

    @set: PropertyName("k")
    @get: PropertyName("k")
    var treatment: String,

    @set: PropertyName("i")
    @get: PropertyName("i")
    var location: String?
)