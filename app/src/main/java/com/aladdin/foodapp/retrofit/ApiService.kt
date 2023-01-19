package com.aladdin.foodapp.retrofit

import com.aladdin.foodapp.models.BurgerRes
import com.aladdin.foodapp.models.Category
import com.aladdin.foodapp.models.FoodHome
import retrofit2.Response
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

    @FormUrlEncoded
    @POST("read_category.php")
    suspend fun getCategory(@Field("code") string: String = "!:GzxWR(34f"): Response<List<Category>>

    @FormUrlEncoded
    @POST("read_foods_list.php")
    suspend fun getHomeFood(@Field("code") string: String = "!:GzxWR(34f"): Response<List<FoodHome>>

}




