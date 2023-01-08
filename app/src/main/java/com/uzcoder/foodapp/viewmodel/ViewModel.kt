package com.uzcoder.foodapp.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uzcoder.foodapp.models.BurgerRes
import com.uzcoder.foodapp.repository.Repository
import com.uzcoder.foodapp.retrofit.ApiClient
import com.uzcoder.foodapp.utils.NetworkHelper
import com.uzcoder.foodapp.utils.Resource
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class ViewModel : ViewModel() {

    private var searchRepository = Repository(ApiClient.apiService)
    private var liveData = MutableLiveData<Resource<BurgerRes>>()


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


}

