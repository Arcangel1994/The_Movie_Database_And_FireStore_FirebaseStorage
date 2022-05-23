package com.example.themoviedbandfirebase.data

import com.example.themoviedbandfirebase.data.network.TheMovieDBAPI
import com.example.themoviedbandfirebase.models.MostPopular
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val theMovieDBAPI: TheMovieDBAPI) {

    //Most Popular
    suspend fun getMostPopular(): Response<MostPopular?> {
        return theMovieDBAPI.getMostPopular()
    }

}