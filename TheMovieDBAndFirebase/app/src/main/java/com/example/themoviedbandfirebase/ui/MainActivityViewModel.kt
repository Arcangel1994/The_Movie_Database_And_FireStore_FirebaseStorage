package com.example.themoviedbandfirebase.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.viewModelScope
import com.example.themoviedbandfirebase.data.Repository
import com.example.themoviedbandfirebase.models.MyLocation
import com.example.themoviedbandfirebase.util.Features
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val repository: Repository, application: Application): AndroidViewModel(application) , LifecycleObserver {

    private var features: Features = Features()

    fun insertMyLocation(myLocation: List<MyLocation>?) = viewModelScope.launch {
        insertMyLocationSafeCall(myLocation)
    }

    private suspend fun insertMyLocationSafeCall(myLocation: List<MyLocation>?) {
        repository.local.insertMyLocation(myLocation)
    }

}