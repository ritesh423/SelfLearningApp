package com.example.myapplication.viewmodels

import androidx.lifecycle.*
import com.example.myapplication.data.model.Movie
import com.example.myapplication.data.network.NewsApi
import com.example.myapplication.data.network.tmdbService
import com.example.myapplication.repository.MovieRepository
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repo: MovieRepository = MovieRepository(apiService = tmdbService)
) : ViewModel() {

    // 1) The list of movies we got back from "search"
    private val _searchResults = MutableLiveData<List<Movie>>(emptyList())
    val searchResults: LiveData<List<Movie>> = _searchResults

    // 2) A loading indicator
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    fun searchMovies(query: String) {
        if (query.isBlank()) return       // don’t fire empty queries

        viewModelScope.launch {
            _isLoading.value = true
            try {
                // repo.searchMovies returns a MovieSearchResponse,
                // so grab its `.results` list
                val resp = repo.searchMovies(query)
                _searchResults.value = resp.results
            } catch (t: Throwable) {
                _searchResults.value = emptyList()
                // you could also expose an error LiveData here
            } finally {
                _isLoading.value = false
            }
        }
    }
}
