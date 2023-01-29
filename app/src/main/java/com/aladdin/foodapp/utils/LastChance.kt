package com.aladdin.foodapp.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class LastChance  {

    private var liveData = MutableLiveData<Boolean>()

    fun set(v:Boolean){
        liveData.value = v
    }

    fun get():LiveData<Boolean>{
        return liveData
    }

    fun setClear(v:Boolean){
        liveData.value = v
    }

    fun getClear():LiveData<Boolean>{
        return liveData
    }

}