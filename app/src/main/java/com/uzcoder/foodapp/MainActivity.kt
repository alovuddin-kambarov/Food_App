package com.uzcoder.foodapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.uzcoder.foodapp.utils.MySharedPreference


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MySharedPreference.getInstance(this)

    }


}
