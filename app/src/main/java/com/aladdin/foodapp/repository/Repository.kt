package com.aladdin.foodapp.repository

import com.aladdin.foodapp.retrofit.ApiService

class Repository(var apiService: ApiService) {

    suspend fun getFood(
        foodName: String,
        phoneNumber: String,
        count: Int,
        orderSize: String,
        price: Int
    ) = apiService.getFoods(foodName, phoneNumber, count, orderSize, price)

    suspend fun getCategory() = apiService.getCategory()
    suspend fun getHomeFood() = apiService.getHomeFood()
    suspend fun getOrders(number: String) = apiService.getOrders(number = number)

}