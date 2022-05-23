package com.example.themoviedbandfirebase.ui.images

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.themoviedbandfirebase.R
import com.example.themoviedbandfirebase.adapter.AdapterImages
import com.example.themoviedbandfirebase.databinding.FragmentImagesBinding
import com.example.themoviedbandfirebase.models.Images
import com.example.themoviedbandfirebase.util.Features
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ImagesFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentImagesBinding? = null
    private val binding get() = _binding!!

    //Features
    private val features by lazy { Features() }

    //ViewModel
    @Inject
    lateinit var imagesViewModel: ImagesViewModel

    //Adapter
    private val adapterImages by lazy { AdapterImages(requireContext(), arrayListOf()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentImagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.floatingButtonAddImages.setOnClickListener(this)

        binding.recyclerViewImages.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewImages.hasFixedSize()
        binding.recyclerViewImages.adapter = adapterImages

        imagesViewModel.images.observe(requireActivity()) {
            if (!it.isNullOrEmpty()) {
                adapterImages.setData(it as MutableList<Images>)
            } else {
                adapterImages.setData(arrayListOf())
            }
        }

    }

    override fun onClick(v: View?) {
        when(v?.id){

            R.id.floatingButtonAddImages -> {

                findNavController().navigate(R.id.action_navigation_images_to_addImagesActivity)

            }

        }
    }

}