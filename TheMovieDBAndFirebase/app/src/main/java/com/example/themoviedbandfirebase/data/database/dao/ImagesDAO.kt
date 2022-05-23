package com.example.themoviedbandfirebase.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.themoviedbandfirebase.models.Images
import kotlinx.coroutines.flow.Flow

@Dao
interface ImagesDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImages(myLocation: Images?)

    @Query("SELECT * FROM Images ORDER BY id_images DESC")
    fun getImages(): Flow<List<Images>?>

    @Query("SELECT * FROM Images ORDER BY id_images DESC")
    suspend fun getImagesSuspend(): List<Images>?

    @Query("SELECT * FROM Images WHERE id_images = :id")
    suspend fun getImagesById(id: Int): Images?

    @Query("DELETE FROM Images")
    suspend fun deleteImages()

}