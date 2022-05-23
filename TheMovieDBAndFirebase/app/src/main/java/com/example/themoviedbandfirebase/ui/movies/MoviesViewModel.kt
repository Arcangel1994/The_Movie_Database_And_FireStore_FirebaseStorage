package com.example.themoviedbandfirebase.ui.movies

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.themoviedbandfirebase.MyApplication
import com.example.themoviedbandfirebase.data.Repository
import com.example.themoviedbandfirebase.models.MostPopularResult
import com.example.themoviedbandfirebase.util.Features
import com.example.themoviedbandfirebase.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(private val repository: Repository, application: Application): AndroidViewModel(application) , LifecycleObserver {

    private var features: Features = Features()

    var movies: MutableLiveData<NetworkResult<List<MostPopularResult>?>> = MutableLiveData()

    fun getMostPopular() = viewModelScope.launch {
        getMostPopularSafeCall()
    }

    private suspend fun getMostPopularSafeCall() {
        movies.value = NetworkResult.Loading()
        if (features.isConnected(MyApplication.context!!)){
            try {

                val response = repository.remote.getMostPopular()

                if(response.isSuccessful){

                    repository.local.deleteMostPopular()

                    repository.local.insertMostPopular(response.body()!!.results)

                    movies.value = NetworkResult.Success(response.body()!!.results)

                }else{

                    val responseRoom = repository.local.getMostPopular()

                    movies.value = NetworkResult.Error("Error en la consulta.", responseRoom)

                }

            }catch (e: Exception){

                val responseRoom = repository.local.getMostPopular()

                movies.value = NetworkResult.Error("Error en la conexion a la ruta.", responseRoom)

            }

        }else{

            val responseRoom = repository.local.getMostPopular()

            movies.value = NetworkResult.Error("Error en la conexion a internet.", responseRoom)

        }
    }

}