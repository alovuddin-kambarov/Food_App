package com.aladdin.foodapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class FoodHome(
    @PrimaryKey
    val id: Int,
    val category: String,
    val image: String,
    val name: String,
    val price: String,
    var count: Int,
    var size:String
):Serializable
