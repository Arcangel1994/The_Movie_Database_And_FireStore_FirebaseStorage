package com.example.themoviedbandfirebase.ui.movies.detailsMovie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.example.themoviedbandfirebase.R
import com.example.themoviedbandfirebase.databinding.ActivityMovieDetailsBinding
import com.example.themoviedbandfirebase.models.MostPopularResult
import com.example.themoviedbandfirebase.util.Constants
import com.example.themoviedbandfirebase.util.Features

class MovieDetailsActivity : AppCompatActivity() {

    var movie: MostPopularResult? = null

    private lateinit var binding: ActivityMovieDetailsBinding

    //Features
    private val features by lazy { Features() }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)

        val intent = intent
        movie = intent.getSerializableExtra("movie") as MostPopularResult

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = movie!!.original_title
        }

        Glide.with(this@MovieDetailsActivity)
            .load("${Constants.BASE_URL_IMAGES}${movie!!.poster_path}")
            .dontAnimate()
            .error(R.drawable.logomovie)
            .into(binding.imageViewSrc)

        binding.ratingBarRatingVoteAverage.rating = (movie!!.vote_average?.toFloat() ?: 0.0) as Float

        binding.materialTextViewOriginalTitle.text = "${movie!!.original_title}"

        binding.materialTextViewDate.text = "${movie!!.release_date}"

        binding.materialTextViewOverView.text = "${movie!!.overview}"

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

}