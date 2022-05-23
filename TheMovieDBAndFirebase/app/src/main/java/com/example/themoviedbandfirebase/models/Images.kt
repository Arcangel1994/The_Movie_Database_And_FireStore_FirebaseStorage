package com.example.themoviedbandfirebase.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "Images")
class Images(

    var idfirebase: String = "",

    var describe: String?,

    var uri: String?,

    val date: Date = Date()

): Serializable {

    @PrimaryKey(autoGenerate = true)
    var id_images: Int = 0

}