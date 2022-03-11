package com.example.recycleviewapp.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [EventData::class], version = 1)
abstract class EventDatabase : RoomDatabase() {
    abstract fun eventDao() : EventDao
}