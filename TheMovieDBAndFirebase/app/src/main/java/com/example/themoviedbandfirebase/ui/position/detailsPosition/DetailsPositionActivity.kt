package com.example.themoviedbandfirebase.ui.position.detailsPosition

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.Window
import com.example.themoviedbandfirebase.R
import com.example.themoviedbandfirebase.databinding.ActivityDetailsPositionBinding
import com.example.themoviedbandfirebase.models.MyLocation
import com.example.themoviedbandfirebase.util.Features

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.text.DateFormat
import java.text.SimpleDateFormat

class DetailsPositionActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityDetailsPositionBinding

    var mylocation: MyLocation? = null

    //Features
    private val features by lazy { Features() }

    var outputFormatDate: DateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsPositionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.details_position)
        }

        val intent = intent
        mylocation = intent.getSerializableExtra("mylocation") as MyLocation

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.isMyLocationEnabled = true
        mMap.setMinZoomPreference(15.0f)
        mMap.setMaxZoomPreference(20.0f)
        // Add a marker in Sydney and move the camera
        val place = LatLng(mylocation!!.latitude, mylocation!!.longitude)
        mMap.addMarker(MarkerOptions().position(place).title("Estabas aqui: ${outputFormatDate.format(mylocation!!.date)}"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(place))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

}