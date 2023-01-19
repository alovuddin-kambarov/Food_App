package com.aladdin.foodapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class Burger : Serializable {


    @PrimaryKey(autoGenerate = false)
    var id: Int? = null
    @ColumnInfo(name = "image")
    var image: String? = null
    @ColumnInfo(name = "name")
    var name: String? = null
    @ColumnInfo(name = "subtitle")
    var subTitle: String? = null
    @ColumnInfo(name = "price")
    var price: String? = null
    @ColumnInfo(name = "star")
    var star: String? = null
    @ColumnInfo(name = "type")
    var type: Int? = null
    @ColumnInfo(name = "size")
    var size: String? = null
    @ColumnInfo(name = "count")
    var count: Int?  = 1


    constructor()
    constructor(
        image: String?,
        name: String?,
        subTitle: String?,
        price: String?,
        star: String?,
        type: Int?,
        size: String?,
        count: Int?
    ) {
        this.image = image
        this.name = name
        this.subTitle = subTitle
        this.price = price
        this.star = star
        this.type = type
        this.size = size
        this.count = count
    }


}