package com.uzcoder.foodapp.repository

import com.uzcoder.foodapp.retrofit.ApiService

class Repository(var apiService: ApiService) {

    suspend fun getFood(foodName:String,phoneNumber:String,count:Int,orderSize:String,price:Int) = apiService.getFoods(foodName,phoneNumber,count,orderSize,price)

}