package com.example.themoviedbandfirebase.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MostPopular(

    @SerializedName("results")
    var results: List<MostPopularResult>? = ArrayList()

): Serializable