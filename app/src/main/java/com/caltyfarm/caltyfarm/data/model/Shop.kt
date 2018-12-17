package com.caltyfarm.caltyfarm.data.model

import java.io.Serializable

class Shop(
    val id: String= "",
    var name: String="",
    var lati: Double = 0.0,
    var longi: Double= 0.0
): Serializable