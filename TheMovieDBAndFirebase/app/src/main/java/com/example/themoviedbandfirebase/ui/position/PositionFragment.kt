package com.example.themoviedbandfirebase.ui.position

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.themoviedbandfirebase.R
import com.example.themoviedbandfirebase.adapter.AdapterPlaces
import com.example.themoviedbandfirebase.databinding.FragmentPositionBinding
import com.example.themoviedbandfirebase.models.MyLocation
import com.example.themoviedbandfirebase.util.Features
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import javax.inject.Inject

@AndroidEntryPoint
class PositionFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentPositionBinding? = null
    private val binding get() = _binding!!

    //Features
    private val features by lazy { Features() }

    //ViewModel
    @Inject
    lateinit var positionViewModel: PositionViewModel

    //Adapter
    private val adapterPlaces by lazy { AdapterPlaces(arrayListOf()) }

    //Permission
    var mPermissionResultLauncherQ: ActivityResultLauncher<Array<String>>? = null
    var isLocationBackgroundPermissionGranted: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPositionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_DENIED){
            binding.floatingButtonSettings.show()
            binding.floatingButtonSettings.visibility = View.VISIBLE
        }else{
            binding.floatingButtonSettings.hide()
            binding.floatingButtonSettings.visibility = View.GONE
        }*/

        binding.floatingButtonSettings.setOnClickListener(this)

        mPermissionResultLauncherQ = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions(), ActivityResultCallback { result ->

            if(result[Manifest.permission.ACCESS_BACKGROUND_LOCATION] != null){

                isLocationBackgroundPermissionGranted = result[Manifest.permission.ACCESS_BACKGROUND_LOCATION]!!

            }

        })

        binding.recyclerViewPlaces.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewPlaces.hasFixedSize()
        binding.recyclerViewPlaces.adapter = adapterPlaces

        adapterPlaces.placesClicklistener = object : AdapterPlaces.PlacesClicklistener{
            override fun onPlacesClick(myLocation: MyLocation, position: Int, view: View) {

                if(features.isConnected(requireContext())){
                    val bundle = bundleOf("mylocation" to myLocation)
                    findNavController().navigate(R.id.action_navigation_position_to_detailsPositionActivity, bundle)
                }else{
                    Toast.makeText(requireContext(), "Necesitas internet para ver el mapa", Toast.LENGTH_LONG).show()
                }

            }
        }

        positionViewModel.locatios.observe(requireActivity()) {
            if(!it.isNullOrEmpty()){
                adapterPlaces.setData(it as MutableList<MyLocation>)
            }else{
                adapterPlaces.setData(arrayListOf())
            }
        }

    }

    override fun onClick(v: View?) {
        when(v?.id){

            R.id.floatingButtonSettings -> {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Toast.makeText(requireContext(), "Activa el permiso: \"permitir todo el tiempo\"", Toast.LENGTH_LONG).show()
                    requestPermission()
                }

            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun requestPermission(){

        isLocationBackgroundPermissionGranted = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED

        val permissionRequest: MutableList<String> = ArrayList<String>()

        if(!isLocationBackgroundPermissionGranted){
            permissionRequest.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }

        if(permissionRequest.isNotEmpty()){
            mPermissionResultLauncherQ!!.launch(permissionRequest.toTypedArray())
        }

    }

}