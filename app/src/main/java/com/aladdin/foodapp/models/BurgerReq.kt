package com.aladdin.foodapp.models

class BurgerReq {

    var food_name:List<String>? = null
    var phone_number:String? = null
    var count:Int? = null
    var order_size:String? = null
    var price:Int? = null



    constructor(
        food_name: List<String>?,
        phone_number: String?,
        count: Int?,
        order_size: String?,
        price: Int?
    ) {
        this.food_name = food_name
        this.phone_number = phone_number
        this.count = count
        this.order_size = order_size
        this.price = price
    }

    constructor()
}