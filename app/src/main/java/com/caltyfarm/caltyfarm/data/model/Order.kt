package com.caltyfarm.caltyfarm.data.model

class Order(
    val id: String = "",
    val orderDate: Long = 0,
    val clientId: String = "",
    val workerType: String = "",
    val workerId: String = "",
    var itemIdList: MutableList<String> = mutableListOf(),
    var totalPrice: Long = 0,
    var totalDistance: Double = 0.0,
    var delivLat: String = "",
    var delivLong: String = "",
    var delivAddress: String = "",
    var sourceLat: String = "",
    var sourceLong: String = "",
    var sourceAddress: String = "",
    var courierLat: String = "",
    var courierLong: String = ""
)