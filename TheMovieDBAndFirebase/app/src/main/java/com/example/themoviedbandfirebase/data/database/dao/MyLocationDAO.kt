package com.example.themoviedbandfirebase.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.themoviedbandfirebase.models.MyLocation
import kotlinx.coroutines.flow.Flow

@Dao
interface MyLocationDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMyLocation(myLocation: List<MyLocation>?)

    @Query("SELECT * FROM MyLocation ORDER BY id_my_location DESC")
    fun getMyLocation(): Flow<List<MyLocation>?>

    @Query("SELECT * FROM MyLocation ORDER BY id_my_location DESC")
    suspend fun getMyLocationSuspend(): List<MyLocation>?

    @Query("SELECT * FROM MyLocation WHERE id_my_location = :id")
    suspend fun getMyLocationById(id: Int): MyLocation?

    @Query("DELETE FROM MyLocation")
    suspend fun deleteMyLocation()

}