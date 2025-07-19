package com.example.myapplication.repository

import com.example.myapplication.network.TmdbService

class MovieRepository(val apiService: TmdbService) {
    suspend fun getPopularMovies(pageNo: Int) = apiService.getPopularMovies(page = pageNo)
}