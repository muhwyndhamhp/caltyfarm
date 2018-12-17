package com.caltyfarm.caltyfarm.data.model

import java.io.Serializable

class Order(
    val id: String = "",
    val orderDate: Long = 0,
    val clientId: String = "",
    val orderType: Int = 0,
    val workerId: String = "",
    var itemIdList: MutableList<String> = mutableListOf(),
    var totalPrice: Long = 0,
    var totalDistance: Long = 0,
    var delivLat: Double = 0.0,
    var delivLong: Double = 0.0,
    var sourceLat: Double = 0.0,
    var sourceLong: Double = 0.0,
    var courierLat: Double = 0.0,
    var courierLong: Double = 0.0,
    var isFinished: Boolean = false,
    var paymentMethod: Boolean = false
): Serializable