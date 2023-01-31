package com.aladdin.foodapp.repository

import com.aladdin.foodapp.retrofit.ApiService
import okhttp3.RequestBody

class Repository(var apiService: ApiService) {

    suspend fun getFood(
        foodBody: RequestBody,
    ) = apiService.getFoods( foodBody )

    suspend fun getCategory() = apiService.getCategory()
    suspend fun getHomeFood() = apiService.getHomeFood()
    suspend fun getOrders(number: String) = apiService.getOrders(number = number)

}