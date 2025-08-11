package com.example.myapplication.repository

import com.example.myapplication.data.model.Movie
import com.example.myapplication.data.model.MovieDetailsResponse
import com.example.myapplication.data.model.MovieSearchResponse
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


//    suspend fun upsert(article : Movie) = db.getArticleDAO().upsert(article)
//
//    fun getFavoriteMovies() = db.getArticleDAO().getAllArticles()
//
//    suspend fun deletArticle(article: Movie) = db.getArticleDAO().deleteArticle(article)
}