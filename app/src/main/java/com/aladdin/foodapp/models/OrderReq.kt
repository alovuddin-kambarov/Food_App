package com.aladdin.foodapp.models

data class OrderReq(
    var count: String,
    val foods:   String ,
    val id: String,
    val phone_number: String,
    val price: String,
    val size: String,
    val vaqt: String,
    val image: String
)