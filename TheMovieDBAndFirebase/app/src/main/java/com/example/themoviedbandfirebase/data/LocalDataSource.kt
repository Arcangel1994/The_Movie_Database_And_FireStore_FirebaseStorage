package com.example.themoviedbandfirebase.data

import com.example.themoviedbandfirebase.data.database.dao.ImagesDAO
import com.example.themoviedbandfirebase.data.database.dao.MostPopularDAO
import com.example.themoviedbandfirebase.data.database.dao.MyLocationDAO
import com.example.themoviedbandfirebase.models.Images
import com.example.themoviedbandfirebase.models.MostPopularResult
import com.example.themoviedbandfirebase.models.MyLocation
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val mostPopularDAO: MostPopularDAO, private val myLocationDAO: MyLocationDAO, private val imagesDAO: ImagesDAO) {

    //Most Popular
    suspend fun insertMostPopular(mostPopular: List<MostPopularResult>?) {
        mostPopularDAO.insertMostPopular(mostPopular)
    }

    suspend fun getMostPopular(): List<MostPopularResult>? {
        return mostPopularDAO.getMostPopular()
    }

    suspend fun getMostPopularById(id: String): MostPopularResult? {
        return mostPopularDAO.getMostPopularById(id)
    }

    suspend fun deleteMostPopular() {
        mostPopularDAO.deleteMostPopular()
    }

    //Location
    suspend fun insertMyLocation(myLocation: List<MyLocation>?) {
        myLocationDAO.insertMyLocation(myLocation)
    }

    fun getMyLocation(): Flow<List<MyLocation>?> {
        return myLocationDAO.getMyLocation()
    }

    suspend fun getMyLocationSuspend(): List<MyLocation>? {
        return myLocationDAO.getMyLocationSuspend()
    }

    suspend fun getMyLocationById(id: Int): MyLocation? {
        return myLocationDAO.getMyLocationById(id)
    }

    suspend fun deleteMyLocation() {
        myLocationDAO.deleteMyLocation()
    }

    //Images
    suspend fun insertImages(images: Images?) {
        imagesDAO.insertImages(images)
    }

    fun getImages(): Flow<List<Images>?> {
        return imagesDAO.getImages()
    }

    suspend fun getImagesSuspend(): List<Images>? {
        return imagesDAO.getImagesSuspend()
    }

    suspend fun getImagesById(id: Int): Images? {
        return imagesDAO.getImagesById(id)
    }

    suspend fun deleteImages() {
        imagesDAO.deleteImages()
    }

}