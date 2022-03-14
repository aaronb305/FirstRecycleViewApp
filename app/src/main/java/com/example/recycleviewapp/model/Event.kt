package com.example.recycleviewapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Event(
    @PrimaryKey var title: String,
    var category: String,
    var date: String
)