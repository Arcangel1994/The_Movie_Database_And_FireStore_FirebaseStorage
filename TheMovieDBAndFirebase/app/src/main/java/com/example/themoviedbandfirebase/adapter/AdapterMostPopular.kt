package com.example.themoviedbandfirebase.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.example.themoviedbandfirebase.R
import com.example.themoviedbandfirebase.models.MostPopularResult
import com.example.themoviedbandfirebase.util.Constants
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textview.MaterialTextView
import java.text.DateFormat
import java.text.SimpleDateFormat

class AdapterMostPopular (private val context: Context, private var mValues: MutableList<MostPopularResult>): RecyclerView.Adapter<AdapterMostPopular.ViewHolder>() {

    lateinit var mostPopularClicklistener: MostPopularClicklistener

    var inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
    var outputFormatDate: DateFormat = SimpleDateFormat("dd-MM-yyyy")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movies_card_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.mItem = mValues[position]

        Glide.with(context)
            .load("${Constants.BASE_URL_IMAGES}${holder.mItem!!.poster_path}")
            .dontAnimate()
            .error(R.drawable.logomovie)
            .into(holder.imageViewMovieSrc!!)

        holder.mItem!!.popularity?.let { if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            {
                holder.circularProgressPopularity!!.setProgress( (it.toDouble() / 100).toInt() , true)
            }else{
                holder.circularProgressPopularity!!.progress = (it.toDouble() / 100).toInt()
            }
        }

        holder.textViewPopularity!!.text = ((holder.mItem!!.popularity?.toDouble() ?: 0.0) / 100).toInt().toString()

        holder.textViewName!!.text = holder.mItem!!.original_title

        holder.textViewDate!!.text = outputFormatDate.format(inputFormat.parse(holder.mItem!!.release_date.toString())!!)

        holder.itemView.setOnClickListener{
            mostPopularClicklistener.mostPopularNowClick(holder.mItem!!, holder.itemView)
        }

    }

    fun setData(result: MutableList<MostPopularResult>){
        mValues = result
        notifyDataSetChanged()
    }

    inner class ViewHolder(mView: View): RecyclerView.ViewHolder(mView){

        var imageViewMovieSrc: ImageView? = null
        var circularProgressPopularity: CircularProgressIndicator? = null
        var textViewPopularity: MaterialTextView? = null
        var textViewName: MaterialTextView? = null
        var textViewDate: MaterialTextView? = null

        var mItem: MostPopularResult? = null

        init {
            imageViewMovieSrc = mView.findViewById(R.id.imageViewMovieSrc)
            circularProgressPopularity = mView.findViewById(R.id.circularProgressPopularity)
            textViewPopularity = mView.findViewById(R.id.textViewPopularity)
            textViewName = mView.findViewById(R.id.textViewName)
            textViewDate = mView.findViewById(R.id.textViewDate)
        }

    }

    interface MostPopularClicklistener{
        fun mostPopularNowClick(movie: MostPopularResult, movieCard: View)
    }

}