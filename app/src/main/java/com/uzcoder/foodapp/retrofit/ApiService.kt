package com.uzcoder.foodapp.retrofit

import com.uzcoder.foodapp.models.BurgerReq
import com.uzcoder.foodapp.models.BurgerRes
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("write_order.php")
    suspend fun getFoods(
        @Field("food_name") food_name: String,
        @Field("phone_number") phone_number: String,
        @Field("count") count: Int,
        @Field("size") order_size: String,
        @Field("price") price: Int,
    ): Response<BurgerRes>


}


