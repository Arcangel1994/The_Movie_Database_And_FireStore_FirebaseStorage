package com.example.themoviedbandfirebase.data.database.typeConnverters

import android.net.Uri
import androidx.room.TypeConverter
import java.util.*

class MyLocationTypeConverters {

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toDate(millisSinceEpoch: Long?): Date? {
        return millisSinceEpoch?.let {
            Date(it)
        }
    }

}