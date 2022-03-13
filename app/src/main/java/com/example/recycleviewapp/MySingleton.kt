package com.example.recycleviewapp

import android.content.Context
import androidx.room.Room
import com.example.recycleviewapp.database.EventDatabase
import com.example.recycleviewapp.model.Event
import java.util.*

/**
 * Variables should go at the top of the class
 */
object MySingleton {

    init {}
    //    var title = mutableListOf<String>()
//    var category = mutableListOf<String>()
//    var date = mutableListOf<Long>()
    var event = mutableListOf<Event>()

    fun addEvent(event: Event) {
        MySingleton.event.add(event)
    }

    fun removeEvent(event: Event) {
        MySingleton.event.remove(event)
    }
}

/**
 * With this singleton we ensure the database is crated only once and everytime we need it, will be the same
 */
class EventDatabaseSingle {
    companion object {
        private var db: EventDatabase? = null

        // Here we create the DB
        // Synchronized to allow only one thread to access to it at a time
        @Synchronized fun getDatabase(context: Context): EventDatabase {
            if (db == null) {
                db = Room.databaseBuilder(
                    context,
                    EventDatabase::class.java,
                    "event-db")
                    .build()
            }

            return db as EventDatabase
        }
    }
}