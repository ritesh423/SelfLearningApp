package com.example.myapplication.viewmodels

import android.os.SystemClock
import androidx.lifecycle.*
import com.example.myapplication.data.model.Movie
import com.example.myapplication.data.network.tmdbService
import com.example.myapplication.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.delay

// Simple UI state for the list screen
sealed interface MoviesUiState {
    object Loading : MoviesUiState
    data class Success(val data: List<Movie>) : MoviesUiState
    data class Error(val message: String) : MoviesUiState
}

class MoviesViewModel : ViewModel() {

    private val repository = MovieRepository(apiService = tmdbService)

    private val _state = MutableLiveData<MoviesUiState>(MoviesUiState.Loading)
    val state: LiveData<MoviesUiState> = _state

    /**
     * Shows shimmer for at least [minShimmerMs] but does NOT add extra delay
     * if the network is already slow.
     */
    fun getPopularMovies(
        pageNo: Int,
        minShimmerMs: Long = 800L   // tune: 600–1000ms feels good
    ) {
        _state.value = MoviesUiState.Loading
        viewModelScope.launch {
            val start = SystemClock.elapsedRealtime()
            try {
                val movies = withContext(Dispatchers.IO) {
                    repository.getPopularMovies(pageNo).movies
                }
                // ensure shimmer was visible for at least minShimmerMs
                val elapsed = SystemClock.elapsedRealtime() - start
                val remaining = minShimmerMs - elapsed
                if (remaining > 0) delay(remaining)

                _state.value = MoviesUiState.Success(movies)
            } catch (e: Exception) {
                // also ensure a tiny minimum shimmer in error paths (optional)
                val elapsed = SystemClock.elapsedRealtime() - start
                val remaining = minShimmerMs - elapsed
                if (remaining > 0) delay(remaining)

                _state.value = MoviesUiState.Error(e.message ?: "Something went wrong")
            }
        }
    }

    fun getTopRatedMovies(
        pageNo: Int,
        minShimmerMs: Long = 500L
    ) {
        _state.value = MoviesUiState.Loading
        viewModelScope.launch {
            val start = SystemClock.elapsedRealtime()
            try {
                val movies = withContext(Dispatchers.IO) {
                    repository.getTopRatedMovies(pageNo).movies
                }
                val elapsed = SystemClock.elapsedRealtime() - start
                val remaining = minShimmerMs - elapsed
                if (remaining > 0) delay(remaining)

                _state.value = MoviesUiState.Success(movies)
            } catch (e: Exception) {
                val elapsed = SystemClock.elapsedRealtime() - start
                val remaining = minShimmerMs - elapsed
                if (remaining > 0) delay(remaining)

                _state.value = MoviesUiState.Error(e.message ?: "Something went wrong")
            }
        }
    }
}
