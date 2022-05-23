package com.example.themoviedbandfirebase.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.themoviedbandfirebase.data.database.dao.ImagesDAO
import com.example.themoviedbandfirebase.data.database.dao.MostPopularDAO
import com.example.themoviedbandfirebase.data.database.dao.MyLocationDAO
import com.example.themoviedbandfirebase.data.database.typeConnverters.MyLocationTypeConverters
import com.example.themoviedbandfirebase.models.Images
import com.example.themoviedbandfirebase.models.MostPopularResult
import com.example.themoviedbandfirebase.models.MyLocation

@Database(entities = [MostPopularResult::class, MyLocation::class, Images::class], version = 1, exportSchema = false)
@TypeConverters(MyLocationTypeConverters::class)
abstract class TheMovieDBDatabase: RoomDatabase() {

    abstract fun mostPopularDAO(): MostPopularDAO

    abstract fun myLocationDAO(): MyLocationDAO

    abstract fun imagesDAO(): ImagesDAO

}