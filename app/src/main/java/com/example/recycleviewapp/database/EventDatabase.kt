package com.example.recycleviewapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.recycleviewapp.model.Event

@Database(entities = [Event::class], version = 1)
abstract class EventDatabase : RoomDatabase() {
    abstract fun eventDao() : EventDao
}