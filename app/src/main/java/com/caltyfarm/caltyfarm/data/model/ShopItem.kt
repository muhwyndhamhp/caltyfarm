package com.caltyfarm.caltyfarm.data.model

import com.google.firebase.database.Exclude
import java.io.Serializable

class ShopItem(
    val id: String = "",
    var name: String = "",
    var description: String = "",
    var price: Long = 0,
    var itemStock: Int = 0,
    @Exclude
    var itemCount: Int = 0,
    var imageUrl: String = ""
): Serializable