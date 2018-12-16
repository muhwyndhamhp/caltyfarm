package com.caltyfarm.caltyfarm.data.model

class Vet(
    val id: String = "",
    var profileUrl: String = "",
    val name: String = "",
    var exp: Int = 0,
    var price: Long = 0,
    var startTime: String = "",
    var endTime: String = "",
    var school : String = "",
    var hospital: String = "",
    var lati :Double = 0.0,
    var longi :Double = 0.0,
    var job: String = "Dokter Hewan"
)