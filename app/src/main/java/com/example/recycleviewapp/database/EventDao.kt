package com.example.recycleviewapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface EventDao {
    @Query("SELECT * FROM eventData")
    fun getAll() : Single<List<EventData>>

    @Query("SELECT * FROM eventData WHERE userId IN (:userIds)")
    fun loadAllByIds(userIds: IntArray) : Single<EventData>

    @Query("SELECT * FROM eventData WHERE category LIKE :category")
    fun findByCategory(category: String) : Single<EventData>

    @Insert
    fun insertUser(eventData: EventData): Completable

    @Insert
    fun insertAll(vararg eventData: EventData): Completable

    @Delete
    fun delete(eventData: EventData): Completable
}