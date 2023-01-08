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
    var type: Int? = null
    var count: Int?  = 1


    constructor()
    constructor(
        image: String?,
        name: String?,
        subTitle: String?,
        price: String?,
        star: String?,
        type: Int?,
        count:Int?
    ) {
        this.image = image
        this.name = name
        this.subTitle = subTitle
        this.price = price
        this.star = star
        this.type = type
        this.count = count
    }


}