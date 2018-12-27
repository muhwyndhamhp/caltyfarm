package com.caltyfarm.caltyfarm.data.model

import java.io.Serializable
import java.util.*

data class ActionHistory(
    var date: Date,
    var action: String,
    var condition: String,
    var diagnostic: String,
    var drugOrVaccine: String?
) : Serializable