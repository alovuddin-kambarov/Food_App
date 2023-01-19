package com.aladdin.foodapp.room

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.aladdin.foodapp.models.FoodHome
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface DatabaseService {

    @Query("select * from FOODHOME")
    fun getAll(): Flowable<List<FoodHome>>

    @Insert(onConflict = REPLACE)
    fun add(myClass: FoodHome): Single<Long>

    @Delete
    fun delete(myClass: FoodHome)

    @Query("DELETE FROM FoodHome")
    fun deleteAll()

    @Update
    fun update(myClass: FoodHome): Completable

}
