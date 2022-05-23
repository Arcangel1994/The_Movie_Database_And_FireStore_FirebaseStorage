package com.example.themoviedbandfirebase.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.themoviedbandfirebase.models.MostPopularResult

@Dao
interface MostPopularDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMostPopular(mostPopular: List<MostPopularResult>?)

    @Query("SELECT * FROM MostPopularResult")
    suspend fun getMostPopular(): List<MostPopularResult>?

    @Query("SELECT * FROM MostPopularResult WHERE id = :id")
    suspend fun getMostPopularById(id: String): MostPopularResult?

    @Query("DELETE FROM MostPopularResult")
    suspend fun deleteMostPopular()

}
