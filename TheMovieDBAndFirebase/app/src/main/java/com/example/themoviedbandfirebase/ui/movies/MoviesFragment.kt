package com.example.themoviedbandfirebase.ui.movies

import android.os.Bundle
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.themoviedbandfirebase.R
import com.example.themoviedbandfirebase.adapter.AdapterMostPopular
import com.example.themoviedbandfirebase.databinding.FragmentMoviesBinding
import com.example.themoviedbandfirebase.models.MostPopularResult
import com.example.themoviedbandfirebase.util.Dialog
import com.example.themoviedbandfirebase.util.Features
import com.example.themoviedbandfirebase.util.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@AndroidEntryPoint
class MoviesFragment : Fragment() {

    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!

    //Features
    private val features by lazy { Features() }

    //Dialog
    private val dialog by lazy { Dialog(requireActivity()) }

    //Adapter
    private val adapterMostPopular by lazy { AdapterMostPopular(requireContext(), arrayListOf()) }

    //ViewModel
    @Inject
    lateinit var moviesViewModel: MoviesViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)

        if(displayMetrics.widthPixels < 1500){
            binding.recyclerViewMostPopular.layoutManager = GridLayoutManager(requireContext(), 2)
        }else{
            binding.recyclerViewMostPopular.layoutManager = GridLayoutManager(requireContext(), 3)
        }
        binding.recyclerViewMostPopular.setHasFixedSize(true)

        adapterMostPopular.mostPopularClicklistener = object : AdapterMostPopular.MostPopularClicklistener {
            override fun mostPopularNowClick(movie: MostPopularResult, movieCard: View) {

                val bundle = bundleOf("movie" to movie)
                findNavController().navigate(R.id.action_navigation_movies_to_movieDetailsActivity, bundle)

            }
        }

        binding.recyclerViewMostPopular.adapter = adapterMostPopular

        moviesViewModel.getMostPopular()

        moviesViewModel.movies.observe(viewLifecycleOwner) { response ->

            when (response) {

                is NetworkResult.Success -> {
                    response.data?.let {
                        if (it.isNotEmpty()) {
                            adapterMostPopular.setData(it as MutableList<MostPopularResult>)
                        } else {
                            Toast.makeText(requireContext(), "No tienes una copia de respaldo.", Toast.LENGTH_LONG).show()
                        }
                        dialog.dismissDialog()
                    }
                }

                is NetworkResult.Error -> {
                    response.data?.let {
                        if (it.isNotEmpty()) {
                            Toast.makeText(requireContext(), "No tienes acceso a internet", Toast.LENGTH_LONG).show()
                            adapterMostPopular.setData(it as MutableList<MostPopularResult>)
                        } else {
                            Toast.makeText(requireContext(), "No tienes una copia de respaldo.", Toast.LENGTH_LONG).show()
                        }
                        dialog.dismissDialog()
                    }
                }

                is NetworkResult.Loading -> {
                    dialog.showDialog()
                }

            }
        }

    }

}