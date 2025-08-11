package com.example.myapplication.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.MovieDetailsResponse
import com.example.myapplication.data.network.tmdbService
import com.example.myapplication.repository.MovieRepository
import kotlinx.coroutines.launch

class DetailsViewModel : ViewModel() {
    private val repo = MovieRepository(apiService = tmdbService)

    private val _details = MutableLiveData<MovieDetailsResponse>()
    val details: LiveData<MovieDetailsResponse> get() = _details

    fun loadDetails(movieId: Int) = viewModelScope.launch {
        _details.value = repo.getMovieDetails(movieId)
    }
}
