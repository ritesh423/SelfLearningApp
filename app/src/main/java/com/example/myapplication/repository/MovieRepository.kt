package com.example.myapplication.repository

import com.example.myapplication.data.network.TmdbService

class MovieRepository(val apiService: TmdbService) {
    suspend fun getPopularMovies(pageNo: Int) = apiService.getPopularMovies(page = pageNo)
}