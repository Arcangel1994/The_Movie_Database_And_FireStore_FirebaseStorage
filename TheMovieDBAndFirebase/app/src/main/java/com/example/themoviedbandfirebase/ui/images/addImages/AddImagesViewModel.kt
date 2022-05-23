package com.example.themoviedbandfirebase.ui.images.addImages

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.viewModelScope
import com.example.themoviedbandfirebase.data.Repository
import com.example.themoviedbandfirebase.models.Images
import com.example.themoviedbandfirebase.util.Features
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddImagesViewModel @Inject constructor(private val repository: Repository, application: Application): AndroidViewModel(application) , LifecycleObserver {

    private var features: Features = Features()

    fun insertImages(images: Images) = viewModelScope.launch {
        insertImagesSafeCall(images)
    }

    private suspend fun insertImagesSafeCall(images: Images) {
        repository.local.insertImages(images)
    }

}