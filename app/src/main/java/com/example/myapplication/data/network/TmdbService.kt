package com.example.myapplication.data.network

import com.example.myapplication.utils.ApiConstants
import retrofit2.http.GET
import retrofit2.http.Query


interface TmdbService {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String = ApiConstants.API_KEY,
        @Query("page") page: Int = 1
    ): MoviesResponse

//    @GET("search/movie")
//    suspend fun searchMovies(
//        @Query("api_key") apiKey: String =  ApiConstants.API_KEY,
//        @Query("query") query: String
//    ): MovieResponse
//
//    @GET("movie/{id}")
//    suspend fun getMovieDetails(
//        @Path("id") id: Int,
//        @Query("api_key") apiKey: String =  ApiConstants.API_KEY
//    ): Movie
}
