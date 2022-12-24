package com.uzcoder.foodapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class Burger : Serializable {


    @PrimaryKey
    var id: Int? = null
    var image: String? = null
    var name: String? = null
    var subTitle: String? = null
    var price: String? = null
    var star: String? = null
    var type: Short? = null

    constructor(
        image: String?,
        name: String?,
        subTitle: String?,
        price: String?,
        star: String?,
        type: Short?
    ) {
        this.image = image
        this.name = name
        this.subTitle = subTitle
        this.price = price
        this.star = star
        this.type = type
    }

    constructor()

}