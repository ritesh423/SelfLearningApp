package com.example.myapplication.repository

import com.example.myapplication.data.model.Movie
import com.example.myapplication.data.model.MovieDetailsResponse
import com.example.myapplication.data.network.NewsApi
//import com.example.myapplication.database.ArticleDatabase

class MovieRepository(
    val apiService : NewsApi,
//    val db : ArticleDatabase
) {
    suspend fun getPopularMovies(pageNo: Int) = apiService.getPopularMovies(page = pageNo)

    suspend fun getTopRatedMovies(pageNo: Int) = apiService.getTopRatedMovies(page = pageNo)

    suspend fun getMovieDetails(id: Int): MovieDetailsResponse =
        apiService.getMovieDetails(id)

//    suspend fun upsert(article : Movie) = db.getArticleDAO().upsert(article)
//
//    fun getFavoriteMovies() = db.getArticleDAO().getAllArticles()
//
//    suspend fun deletArticle(article: Movie) = db.getArticleDAO().deleteArticle(article)
}