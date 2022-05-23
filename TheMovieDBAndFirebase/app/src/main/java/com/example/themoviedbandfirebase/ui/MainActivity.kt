package com.example.themoviedbandfirebase.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.themoviedbandfirebase.LocationUpdatesBroadcastReceiver
import com.example.themoviedbandfirebase.R
import com.example.themoviedbandfirebase.databinding.ActivityMainBinding
import com.example.themoviedbandfirebase.models.MyLocation
import com.example.themoviedbandfirebase.util.Features
import com.google.android.gms.location.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    private lateinit var binding: ActivityMainBinding

    //Permission
    var mPermissionResultLauncher: ActivityResultLauncher<Array<String>>? = null
    var isLocationCoarsePermissionGranted: Boolean = false
    var isLocationFinePermissionGranted: Boolean = false

    //Location
    private val fusedLocationClient: FusedLocationProviderClient by lazy { LocationServices.getFusedLocationProviderClient(applicationContext) }
    private val locationRequest: LocationRequest = LocationRequest.create().apply {
        interval = TimeUnit.SECONDS.toMillis(300)
        fastestInterval = TimeUnit.SECONDS.toMillis(250)
        maxWaitTime = TimeUnit.MINUTES.toMillis(6)
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    //ViewModel
    @Inject
    lateinit var mainActivityViewModel: MainActivityViewModel

    //Firebase Firestore
    val db = Firebase.firestore

    //Features
    private val features by lazy { Features() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_movies, R.id.navigation_position, R.id.navigation_images
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        mPermissionResultLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions(), ActivityResultCallback { result ->

            if(result[Manifest.permission.ACCESS_COARSE_LOCATION] != null){

                isLocationCoarsePermissionGranted = result[Manifest.permission.ACCESS_COARSE_LOCATION]!!

            }

            if(result[Manifest.permission.ACCESS_FINE_LOCATION] != null){

                isLocationFinePermissionGranted = result[Manifest.permission.ACCESS_FINE_LOCATION]!!

            }

            if(isLocationCoarsePermissionGranted && isLocationFinePermissionGranted){
                //startLocationUpdates()
                setLocationListner()
            }

        })

        requestPermission()

    }

    private fun requestPermission(){

        isLocationCoarsePermissionGranted = ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

        isLocationFinePermissionGranted = ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

        val permissionRequest: MutableList<String> = ArrayList<String>()

        if(!isLocationCoarsePermissionGranted){
            permissionRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }

        if(!isLocationFinePermissionGranted){
            permissionRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        if(permissionRequest.isNotEmpty()){
            mPermissionResultLauncher!!.launch(permissionRequest.toTypedArray())
        }else{
            //startLocationUpdates()
            setLocationListner()
        }

    }

    @SuppressLint("MissingPermission")
    private fun setLocationListner() {

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    val locations = locationResult.locations.map { location ->
                        Log.i("Hola", location.latitude.toString() + location.longitude.toString())
                        MyLocation(
                            idfirebase = "",
                            latitude = location.latitude,
                            longitude = location.longitude,
                            foreground = isAppInForeground(this@MainActivity),
                            date = Date(location.time)
                        )
                    }
                    if (locations.isNotEmpty()) {
                        saveMyLocation(locations)
                    }
                }
            },
            Looper.getMainLooper()
        )

    }

    fun insertMyLocation(myLocation: List<MyLocation>?){
        Log.i(TAG, myLocation?.get(0)?.latitude.toString() + myLocation?.get(0)?.longitude.toString())
        if (myLocation != null) {
            mainActivityViewModel.insertMyLocation(myLocation)
        }
    }


    private fun saveMyLocation(locations: List<MyLocation>) {

        if(features.isConnected(this@MainActivity)){

          for(location in locations){

              val data = hashMapOf(
                  "latitude" to location.latitude,
                  "longitude" to location.longitude,
                  "foreground" to location.foreground,
                  "date" to location.date
              )

              db.collection("locations")
                  .add(data)
                  .addOnSuccessListener { documentReference ->
                      Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                      showNotification(this@MainActivity)
                      location.idfirebase = documentReference.id
                      insertMyLocation(locations)
                  }
                  .addOnFailureListener { e ->
                      Log.w(TAG, "Error adding document", e)
                  }

          }

        }else{

            //Toast.makeText(this@MainActivity, "No tienes conexion de internet, se guardo local", Toast.LENGTH_LONG).show()
            showNotification(this@MainActivity)
            insertMyLocation(locations)

        }

    }

    private fun startLocationUpdates() {

        try {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationUpdatePendingIntent)
        } catch (permissionRevoked: SecurityException) {
            Log.d("MainActivityLocation", "Location permission revoked; details: $permissionRevoked")
            throw permissionRevoked
        }

    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationUpdatePendingIntent)
    }

    private val locationUpdatePendingIntent: PendingIntent by lazy {
        val intent = Intent(this@MainActivity, LocationUpdatesBroadcastReceiver::class.java)
        intent.action = LocationUpdatesBroadcastReceiver.ACTION_PROCESS_UPDATES
        PendingIntent.getBroadcast(this@MainActivity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun isAppInForeground(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val appProcesses = activityManager.runningAppProcesses ?: return false

        appProcesses.forEach { appProcess ->
            if (appProcess.importance ==
                ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND &&
                appProcess.processName == context.packageName) {
                return true
            }
        }
        return false
    }

    private fun showNotification(context: Context){

        val id = "mychannel"
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var channel = manager!!.getNotificationChannel(id)
            if(channel ==null)
            {
                channel = NotificationChannel(id, "Location", NotificationManager.IMPORTANCE_HIGH)
                channel.description = "Update"
                channel.enableVibration(true)
                channel.vibrationPattern = longArrayOf(100, 1000, 200, 340)
                channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                manager.createNotificationChannel(channel)
            }
        }

        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, id)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher))
            .setStyle(NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher)).bigLargeIcon(null))
            .setContentTitle("Update Location")
            .setContentText("Save Location")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(longArrayOf(100, 1000, 200, 340))
            .setAutoCancel(false)
            .setTicker("Nofiication")

        val m = NotificationManagerCompat.from(context)
        m.notify(1, builder.build())

    }

}