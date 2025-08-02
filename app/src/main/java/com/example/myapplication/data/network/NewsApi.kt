package com.example.myapplication.data.network

import com.example.myapplication.data.model.MovieDetailsResponse
import com.example.myapplication.data.model.MovieSearchResponse
import com.example.myapplication.data.model.MoviesResponse
import com.example.myapplication.utils.ApiConstants
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface NewsApi {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String = ApiConstants.API_KEY,
        @Query("page") page: Int = 1
    ): MoviesResponse

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query")    query: String,           // the user’s text
        @Query("page")     page: Int   = 1,         // paging support
        @Query("api_key")  apiKey: String = ApiConstants.API_KEY
    ): MovieSearchResponse

    @GET("movie/{id}")
    suspend fun getMovieDetails(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String =  ApiConstants.API_KEY
    ): MovieDetailsResponse

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key") apiKey: String = ApiConstants.API_KEY,
        @Query("page") page: Int = 1,
    ) : MoviesResponse
}
