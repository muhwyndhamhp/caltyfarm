package com.caltyfarm.caltyfarm.data.model

import java.io.Serializable

class Order(
    val id: String = "",
    val orderDate: Long = 0,
    val clientId: String = "",
    val workerType: Int = 0,
    val workerId: String = "",
    var itemIdList: MutableList<String> = mutableListOf(),
    var totalPrice: Long = 0,
    var totalDistance: Double = 0.0,
    var delivLat: String = "",
    var delivLong: String = "",
    var sourceLat: String = "",
    var sourceLong: String = "",
    var courierLat: String = "",
    var courierLong: String = "",
    var isFinished: Boolean = false,
    var paymentMethod: Boolean = false
): Serializable