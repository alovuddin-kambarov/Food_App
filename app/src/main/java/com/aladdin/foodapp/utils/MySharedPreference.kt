package com.aladdin.foodapp.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object MySharedPreference {

    lateinit var sharedPreferences: SharedPreferences

    fun getInstance(context: Context) {
        sharedPreferences = context.getSharedPreferences(
            "" +
                    "", Context.MODE_PRIVATE
        )
    }

    var typeData: Int?
        get() = sharedPreferences.getInt("typeData", 0)
        set(value) = sharedPreferences.edit {
            if (value != null) {
                this.putInt("typeData", value)
            }
        }

    var isCategory: Boolean?
        get() = sharedPreferences.getBoolean("isCategory", false)
        set(value) = sharedPreferences.edit {
            if (value != null) {
                this.putBoolean("isCategory", value)
            }
        }

    var phoneNumber: String?
        get() = sharedPreferences.getString("phoneNumber", "")
        set(value) = sharedPreferences.edit {
            if (value != null) {
                this.putString("phoneNumber", value)
            }

        }

    var languageId: Int?
        get() = sharedPreferences.getInt("languageId", 0)
        set(value) = sharedPreferences.edit {
            if (value != null) {
                this.putInt("languageId", value)
            }

        }
    var language: String?
        get() = sharedPreferences.getString("language", "uz")
        set(value) = sharedPreferences.edit {
            if (value != null) {
                this.putString("language", value)
            }

        }

}