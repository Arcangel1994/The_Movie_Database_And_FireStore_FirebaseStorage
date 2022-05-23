package com.example.themoviedbandfirebase.data.network

import com.example.themoviedbandfirebase.models.MostPopular
import com.example.themoviedbandfirebase.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET

interface TheMovieDBAPI {

    @GET("popular?api_key=${API_KEY}&language=es-MX&page=1")
    suspend fun getMostPopular(): Response<MostPopular?>

}