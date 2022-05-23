package com.example.themoviedbandfirebase

import android.app.ActivityManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.themoviedbandfirebase.models.MyLocation
import com.example.themoviedbandfirebase.ui.MainActivity
import com.example.themoviedbandfirebase.ui.MainActivityViewModel
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationResult
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import java.util.concurrent.Executors
import javax.inject.Inject

class LocationUpdatesBroadcastReceiver : BroadcastReceiver() {

    private val TAG = "BroadcastReceiver"

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "onReceive() context:$context, intent:$intent")

        if (intent.action == ACTION_PROCESS_UPDATES) {

            // Checks for location availability changes.
            LocationAvailability.extractLocationAvailability(intent)?.let { locationAvailability ->
                if (!locationAvailability.isLocationAvailable) {
                    Log.d(TAG, "¡Los servicios de ubicación ya no están disponibles!")
                }
            }

            LocationResult.extractResult(intent)?.let { locationResult ->
                val locations = locationResult.locations.map { location ->
                    Log.i(TAG, location.latitude.toString() + location.longitude.toString())
                    MyLocation(
                        latitude = location.latitude,
                        longitude = location.longitude,
                        foreground = isAppInForeground(context),
                        date = Date(location.time)
                    )
                }
                if (locations.isNotEmpty()) {
                    showNotification(context)
                    //MainActivity().getLocation(locations)
                }
            }
        }
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

    companion object {
        const val ACTION_PROCESS_UPDATES =
            "com.example.themoviedbandfirebase.action." +
                    "PROCESS_UPDATES"
    }
}
