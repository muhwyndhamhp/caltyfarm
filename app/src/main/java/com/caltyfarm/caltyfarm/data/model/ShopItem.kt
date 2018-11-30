package com.caltyfarm.caltyfarm.data.model

class ShopItem(
    val id: String = "",
    var name: String = "",
    var description: String = "",
    var price: Long = 0,
    var itemStock: Int = 0,
    var itemCount: Int = 0,
    var imageUrl: String = ""
)