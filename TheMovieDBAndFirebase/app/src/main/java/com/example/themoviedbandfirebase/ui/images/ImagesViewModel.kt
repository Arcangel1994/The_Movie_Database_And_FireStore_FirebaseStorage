package com.example.themoviedbandfirebase.ui.images

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.themoviedbandfirebase.data.Repository
import com.example.themoviedbandfirebase.models.Images
import com.example.themoviedbandfirebase.util.Features
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ImagesViewModel @Inject constructor(private val repository: Repository, application: Application): AndroidViewModel(application) ,
    LifecycleObserver {

    private var features: Features = Features()

    val images: LiveData<List<Images>?> = repository.local.getImages().asLiveData()

}