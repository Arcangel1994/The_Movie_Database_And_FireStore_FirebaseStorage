package com.example.themoviedbandfirebase.adapter

import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedbandfirebase.R
import com.example.themoviedbandfirebase.models.MyLocation
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import java.text.DateFormat
import java.text.SimpleDateFormat

class AdapterPlaces(private var places: MutableList<MyLocation>): RecyclerView.Adapter<AdapterPlaces.ViewHolder>() {

    //Listener
    lateinit var placesClicklistener: PlacesClicklistener

    var outputFormatDate: DateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.places_card_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return places.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mItem = places[position]

        if(!holder.mItem!!.foreground){
            holder.shapeImageIn!!.setImageResource(R.drawable.ic_in_front)
        }else{
            holder.shapeImageIn!!.setImageResource(R.drawable.ic_in_back)
        }

        holder.materialTextLat!!.text = "${holder.mItem!!.latitude}"
        holder.materialTextLong!!.text = "${holder.mItem!!.longitude}"
        holder.materialTextDate!!.text = "${outputFormatDate.format(holder.mItem!!.date)}"

        holder.itemView.setOnClickListener{
            placesClicklistener.onPlacesClick(holder.mItem!!, position, holder.itemView)
        }

    }

    fun setData(setPlaces: MutableList<MyLocation>){
        places = setPlaces
        notifyDataSetChanged()
    }

    inner class ViewHolder(mView: View): RecyclerView.ViewHolder(mView){

        var shapeImageIcon: ShapeableImageView? = null
        var materialTextLat: MaterialTextView? = null
        var materialTextLong: MaterialTextView? = null
        var materialTextDate: MaterialTextView? = null
        var shapeImageIn: ShapeableImageView? = null

        var mItem: MyLocation? = null

        init {
            shapeImageIcon = mView.findViewById(R.id.shapeImageIcon)
            materialTextLat = mView.findViewById(R.id.materialTextLat)
            materialTextLong = mView.findViewById(R.id.materialTextLong)
            materialTextDate = mView.findViewById(R.id.materialTextDate)
            shapeImageIn = mView.findViewById(R.id.shapeImageIn)
        }

    }

    interface PlacesClicklistener{
        fun onPlacesClick(myLocation: MyLocation, position: Int, view: View)
    }

}