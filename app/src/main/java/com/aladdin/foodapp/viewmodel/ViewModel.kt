package com.aladdin.foodapp.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aladdin.foodapp.models.BurgerRes
import com.aladdin.foodapp.models.Category
import com.aladdin.foodapp.models.FoodHome
import com.aladdin.foodapp.repository.Repository
import com.aladdin.foodapp.retrofit.ApiClient
import com.aladdin.foodapp.utils.NetworkHelper
import com.aladdin.foodapp.utils.Resource
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class ViewModel : ViewModel() {

    private var searchRepository = Repository(ApiClient.apiService)
    private var liveData = MutableLiveData<Resource<BurgerRes>>()
    private var liveDataCategory = MutableLiveData<Resource<List<Category>>>()
    private var liveDataHome  = MutableLiveData<Resource<List<FoodHome>>>()


    fun getFoods(
        context: Context,
        foodList: String,
        phoneNumber: String,
        count: Int,
        orderSize: String,
        price: Int
    ): LiveData<Resource<BurgerRes>> {


        viewModelScope.launch {
            if (NetworkHelper(context).isNetworkConnected()) {
                coroutineScope {
                    supervisorScope {
                        try {
                            liveData.postValue(Resource.loading(null))
                            val food = searchRepository.getFood(
                                foodList,
                                phoneNumber,
                                count,
                                orderSize,
                                price
                            )

                            if (food.isSuccessful) {
                                liveData.postValue(Resource.success(food.body()))

                            } else {
                                liveData.postValue(
                                    Resource.error(
                                        food.raw().toString(),
                                        null
                                    )
                                )


                            }


                        } catch (e: Exception) {
                            liveData.postValue(Resource.error(e.message ?: "Error", null))
                        }
                    }
                }
            } else {
                liveData.postValue(
                    Resource.error(
                        "Internet no connection! Please, connect internet and try again!",
                        null
                    )
                )

            }

        }

        return liveData

    }


    fun getCategory(
        context: Context
    ): MutableLiveData<Resource<List<Category>>> {


        viewModelScope.launch {
            if (NetworkHelper(context).isNetworkConnected()) {
                coroutineScope {
                    supervisorScope {
                        try {
                            liveDataCategory.postValue(Resource.loading(null))
                            val food = searchRepository.getCategory()

                            if (food.isSuccessful) {
                                val success = Resource.success(food.body())
                                liveDataCategory.postValue(success)
                            } else {
                                liveDataCategory.postValue(
                                    Resource.error(
                                        food.raw().toString(),
                                        null
                                    )
                                )


                            }


                        } catch (e: Exception) {
                            liveDataCategory.postValue(Resource.error(e.message ?: "Error", null))
                        }
                    }
                }
            } else {
                liveDataCategory.postValue(
                    Resource.error(
                        "Internet no connection! Please, connect internet and try again!",
                        null
                    )
                )

            }

        }

        return liveDataCategory

    }

    fun getHomeFood(
        context: Context
    ): MutableLiveData<Resource<List<FoodHome>>> {


        viewModelScope.launch {
            if (NetworkHelper(context).isNetworkConnected()) {
                coroutineScope {
                    supervisorScope {
                        try {
                            liveDataHome.postValue(Resource.loading(null))
                            val food = searchRepository.getHomeFood()

                            if (food.isSuccessful) {
                                val success = Resource.success(food.body())
                                liveDataHome.postValue(success)
                            } else {
                                liveDataHome.postValue(
                                    Resource.error(
                                        food.raw().toString(),
                                        null
                                    )
                                )


                            }


                        } catch (e: Exception) {
                            liveDataHome.postValue(Resource.error(e.message ?: "Error", null))
                        }
                    }
                }
            } else {
                liveDataHome.postValue(
                    Resource.error(
                        "Internet no connection! Please, connect internet and try again!",
                        null
                    )
                )

            }

        }

        return liveDataHome

    }


}

