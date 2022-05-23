package com.example.themoviedbandfirebase.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.themoviedbandfirebase.R
import com.example.themoviedbandfirebase.models.Images
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import java.text.DateFormat
import java.text.SimpleDateFormat

class AdapterImages(private val context: Context, private var images: MutableList<Images>): RecyclerView.Adapter<AdapterImages.ViewHolder>() {

    var outputFormatDate: DateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.images_card_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mItem = images[position]

        Glide.with(context)
            .load(holder.mItem!!.uri)
            .dontAnimate()
            .error(R.drawable.logomovie)
            .into(holder.shapeImage!!)

        holder.materialTextDescripción!!.text = holder.mItem!!.describe

        holder.materialTextDate!!.text = "${outputFormatDate.format(holder.mItem!!.date)}"

    }

    fun setData(setImages: MutableList<Images>){
        images = setImages
        notifyDataSetChanged()
    }

    inner class ViewHolder(mView: View): RecyclerView.ViewHolder(mView){

        var shapeImage: ShapeableImageView? = null
        var materialTextDescripción: MaterialTextView? = null
        var materialTextDate: MaterialTextView? = null

        var mItem: Images? = null

        init {
            shapeImage = mView.findViewById(R.id.shapeImage)
            materialTextDescripción = mView.findViewById(R.id.materialTextDescripción)
            materialTextDate = mView.findViewById(R.id.materialTextDate)
        }

    }

}