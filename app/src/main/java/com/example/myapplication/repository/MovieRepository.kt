package com.example.myapplication.repository

import com.example.myapplication.data.model.Movie
import com.example.myapplication.data.model.MovieDetailsResponse
import com.example.myapplication.data.model.MovieSearchResponse
import com.example.myapplication.data.model.MovieVideo
import com.example.myapplication.data.network.NewsApi

//import com.example.myapplication.database.ArticleDatabase

class MovieRepository(
    val apiService: NewsApi,
//    val db : ArticleDatabase
) {
    suspend fun getPopularMovies(pageNo: Int) = apiService.getPopularMovies(page = pageNo)

    suspend fun getTopRatedMovies(pageNo: Int) = apiService.getTopRatedMovies(page = pageNo)

    suspend fun getMovieDetails(id: Int): MovieDetailsResponse =
        apiService.getMovieDetails(id)

    suspend fun searchMovies(query: String, page: Int = 1): MovieSearchResponse {
        return apiService.searchMovies(query, page)
    }

    suspend fun searchCompany(query: String) = apiService.searchCompany(query = query)
    suspend fun discoverByCompany(companyId: Int, page: Int) =
        apiService.discoverByCompany(companyId = companyId, page = page)

    /** Convenience: find best company by name, then discover movies for it */
    suspend fun discoverByCompanyName(name: String, page: Int) = run {
        val search = searchCompany(name)
        val companyId = search.results.firstOrNull()?.id
            ?: throw IllegalArgumentException("No company found for \"$name\"")
        discoverByCompany(companyId, page)
    }

    suspend fun getMovieVideos(movieId: Int) = apiService.getMovieVideos(movieId)

    suspend fun pickBestTrailer(movieId: Int): MovieVideo? {
        val vids = getMovieVideos(movieId).results
        return vids.firstOrNull { it.type.equals("Trailer", true) && it.official }
            ?: vids.firstOrNull { it.type.equals("Trailer", true) }
            ?: vids.firstOrNull()
    }

    /**
     * If you have a direct MP4/HLS URL for your trailer, return it here.
     * TMDB usually gives YouTube keys -> return null to trigger YouTube fallback.
     */
    suspend fun resolvePlayableUrl(video: MovieVideo?): String? {
        if (video == null) return null
        return when (video.site.lowercase()) {
            "youtube" -> null
            "vimeo"   -> null
            else      -> null
        }
    }

}