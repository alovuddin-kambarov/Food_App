package com.aladdin.foodapp.retrofit

import com.aladdin.foodapp.models.*
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("write_order.php")
    suspend fun getFoods(
        @Body items: RequestBody
    ): Response<BurgerRes>

    @FormUrlEncoded
    @POST("read_category.php")
    suspend fun getCategory(@Field("code") string: String = "!:GzxWR(34f"): Response<List<Category>>

    @FormUrlEncoded
    @POST("read_foods_list.php")
    suspend fun getHomeFood(@Field("code") string: String = "!:GzxWR(34f"): Response<List<FoodHome>>

    @FormUrlEncoded
    @POST("read_phone.php")
    suspend fun getOrders(@Field("code") string: String = "!:GzxWR(34f",@Field("phone_number") number: String ): Response<List<OrderReq>>

}




