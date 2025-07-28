package com.example.myapplication.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.Movie
import com.example.myapplication.data.network.tmdbService
//import com.example.myapplication.database.ArticleDatabase
import com.example.myapplication.repository.MovieRepository
import kotlinx.coroutines.launch

class MoviesViewModel : ViewModel() {

    private val repository = MovieRepository(apiService = tmdbService)

    private val _movies= MutableLiveData<List<Movie>>(emptyList())
    val movies: LiveData<List<Movie>> get() = _movies


    fun getPopularMovies(pageNo: Int) {
        viewModelScope.launch {
            _movies.value = repository.getPopularMovies(pageNo).movies
        }
    }

}