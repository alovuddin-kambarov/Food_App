package com.aladdin.foodapp

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.aladdin.foodapp.room.AppDatabase

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AppDatabase.getInstants(this)

        // disable night mode settings
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

    }
}