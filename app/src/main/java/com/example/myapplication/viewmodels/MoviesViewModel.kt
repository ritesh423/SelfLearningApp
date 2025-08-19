package com.example.myapplication.viewmodels

import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.*
import com.example.myapplication.data.model.Movie
import com.example.myapplication.data.network.tmdbService
import com.example.myapplication.repository.MovieRepository
import kotlinx.coroutines.*

sealed interface MoviesUiState {
    data object Loading : MoviesUiState
    data class Success(val data: List<Movie>) : MoviesUiState
    data class Error(val message: String) : MoviesUiState
}

class MoviesViewModel : ViewModel() {

    private val TAG = "MoviesVM"
    private val repository = MovieRepository(apiService = tmdbService)

    private val _state = MutableLiveData<MoviesUiState>(MoviesUiState.Loading)
    val state: LiveData<MoviesUiState> = _state

    /** The unfiltered list currently shown (used for local rating filters). */
    private var baseList: List<Movie> = emptyList()

    /** Track the active job so we can cancel when switching filters/tabs. */
    private var loadJob: Job? = null

    /* ---------------------------------- Public API ---------------------------------- */

    fun getPopularMovies(
        pageNo: Int,
        minShimmerMs: Long = 500L,
        forceRefresh: Boolean = false
    ) {
        if (!forceRefresh && _state.value is MoviesUiState.Success && baseList.isNotEmpty()) {
            // Already have data; keep it unless you explicitly refresh.
            return
        }
        loadWith(
            minShimmerMs = minShimmerMs,
            loader = { repository.getPopularMovies(pageNo).movies },
            label = "Popular"
        )
    }

    fun getTopRatedMovies(
        pageNo: Int,
        minShimmerMs: Long = 500L,
        forceRefresh: Boolean = false
    ) {
        if (!forceRefresh && _state.value is MoviesUiState.Success && baseList.isNotEmpty()) {
            return
        }
        loadWith(
            minShimmerMs = minShimmerMs,
            loader = { repository.getTopRatedMovies(pageNo).movies },
            label = "TopRated"
        )
    }

    /** Local filter: show only movies with voteAverage >= minRating. Pass null to clear. */
    fun applyLocalRatingFilter(minRating: Double?) {
        val filtered = if (minRating == null) baseList
        else baseList.filter { (it.voteAverage ?: 0.0) >= minRating }
        _state.value = MoviesUiState.Success(filtered)
    }

    /**
     * Server-side fetch: show movies by company name (e.g., "dc").
     * Requires repository methods: searchCompany() + discoverByCompany() or a convenience
     * discoverByCompanyName(). (See TODO in repository if you haven’t added them yet.)
     */
    fun loadByCompanyName(
        companyName: String,
        pageNo: Int = 1,
        minShimmerMs: Long = 500L
    ) {
        loadWith(
            minShimmerMs = minShimmerMs,
            loader = { repository.discoverByCompanyName(companyName, pageNo).movies },
            label = "Company:${companyName}"
        )
    }

    /* ---------------------------------- Internals ---------------------------------- */

    private fun loadWith(
        minShimmerMs: Long,
        loader: suspend () -> List<Movie>,
        label: String
    ) {
        _state.value = MoviesUiState.Loading
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            val start = SystemClock.elapsedRealtime()
            try {
                val movies = withContext(Dispatchers.IO) { loader() }
                ensureMinShimmer(start, minShimmerMs)
                setBaseAndEmit(movies)
                Log.d(TAG, "$label: SUCCESS items=${movies.size}")
            } catch (ce: CancellationException) {
                Log.d(TAG, "$label: cancelled")
                throw ce
            } catch (e: Exception) {
                ensureMinShimmer(start, minShimmerMs)
                _state.value = MoviesUiState.Error(e.message ?: "Something went wrong")
                Log.e(TAG, "$label: ERROR ${e.message}", e)
            }
        }
    }

    private fun setBaseAndEmit(movies: List<Movie>) {
        baseList = movies
        _state.value = MoviesUiState.Success(movies)
    }

    private suspend fun ensureMinShimmer(start: Long, minMs: Long) {
        val elapsed = SystemClock.elapsedRealtime() - start
        val remaining = (minMs - elapsed).coerceAtLeast(0)
        if (remaining > 0) delay(remaining)
    }
}
