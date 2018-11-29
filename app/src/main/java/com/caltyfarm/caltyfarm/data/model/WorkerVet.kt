package com.caltyfarm.caltyfarm.data.model

class WorkerVet(
    val id: String = "",
    var profileUrl: String = "",
    val name: String = "",
    var exp: Int = 0,
    var price: Long = 0,
    var startTime: String = "",
    var endTime: String = "",
    var school : String = "",
    var hospital: String = "",
    var lati :String = "",
    var longi :String = "",
    var job: String = "Dokter Hewan"
)