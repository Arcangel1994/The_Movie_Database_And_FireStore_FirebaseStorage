package com.example.themoviedbandfirebase.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "MyLocation")
class MyLocation(

    var idfirebase: String = "",

    val latitude: Double = 0.0,

    val longitude: Double = 0.0,

    val foreground: Boolean = true,

    val date: Date = Date()

): Serializable {

    @PrimaryKey(autoGenerate = true)
    var id_my_location: Int = 0

}