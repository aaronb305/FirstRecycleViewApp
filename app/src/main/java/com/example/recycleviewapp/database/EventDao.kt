package com.example.recycleviewapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface EventDao {
    @Query("SELECT * FROM eventData")
    fun getAll() : List<EventData>

    @Query("SELECT * FROM eventData WHERE userId IN (:userIds)")
    fun loadAllByIds(userIds: IntArray) : List<EventData>

    @Query("SELECT * FROM eventData WHERE category LIKE :category")
    fun findByCategory(category: String) : List<EventData>

    @Insert
    fun insertUser(eventData: EventData)

    @Insert
    fun insertAll(vararg eventData: EventData)

    @Delete
    fun delete(eventData: EventData)
}