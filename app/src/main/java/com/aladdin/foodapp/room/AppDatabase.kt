package com.aladdin.foodapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aladdin.foodapp.models.FoodHome

@Database(entities = [FoodHome::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    abstract fun dao(): DatabaseService

    companion object {
        private var instens: AppDatabase? = null

        @Synchronized
        fun getInstants(context: Context): AppDatabase {
            if (instens == null) {
                instens = Room.databaseBuilder(context, AppDatabase::class.java, "room")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration().allowMainThreadQueries()
                    .build()
            }

            return instens!!

        }

    }



}