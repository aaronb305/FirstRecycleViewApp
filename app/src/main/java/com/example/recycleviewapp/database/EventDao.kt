package com.example.recycleviewapp.database

import androidx.room.*
import com.example.recycleviewapp.model.Event
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface EventDao {
    @Query("SELECT * FROM event")
    fun getAll() : Single<List<Event>>

//    @Query("SELECT * FROM event WHERE userId IN (:userIds)")
//    fun loadAllByIds(userIds: IntArray) : Single<Event>

    @Query("SELECT * FROM event WHERE category LIKE :category")
    fun findByCategory(category: String) : Single<Event>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEvent(eventData: Event): Completable

    @Insert
    fun insertAll(vararg eventData: Event): Completable

    @Delete
    fun delete(eventData: Event): Completable
}