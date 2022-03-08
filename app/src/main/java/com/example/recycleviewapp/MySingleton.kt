package com.example.recycleviewapp

import com.example.recycleviewapp.model.Event
import java.util.*

object MySingleton {
    fun addEvent(event: Event) {
        MySingleton.event.add(event)
    }

    init {}
//    var title = mutableListOf<String>()
//    var category = mutableListOf<String>()
//    var date = mutableListOf<Long>()
    var event = mutableListOf<Event>()
}