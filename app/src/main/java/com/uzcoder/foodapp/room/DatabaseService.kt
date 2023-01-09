package com.uzcoder.foodapp.room

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.uzcoder.foodapp.models.Burger
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface DatabaseService {

    @Query("select * from Burger")
    fun getAll(): Flowable<List<Burger>>

    @Insert(onConflict = REPLACE)
    fun add(myClass: Burger): Single<Long>

    @Delete
    fun delete(myClass: Burger)

    @Query("DELETE FROM Burger")
    fun deleteAll()

    @Update
    fun update(myClass: Burger): Completable

}
