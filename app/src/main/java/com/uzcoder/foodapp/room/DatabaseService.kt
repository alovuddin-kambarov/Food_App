package com.uzcoder.foodapp.room

import androidx.room.*
import com.uzcoder.foodapp.models.Burger
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface DatabaseService {

    @Query("select * from Burger")
    fun getAll(): Flowable<List<Burger>>

    @Insert
    fun add(myClass: Burger): Single<Long>

    @Delete
    fun delete(myClass: Burger)

    @Update
    fun update(myClass: Burger): Completable

}
