//package com.example.myapplication.database
//
//import androidx.lifecycle.LiveData
//import androidx.room.Dao
//import androidx.room.Delete
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Query
//import com.example.myapplication.data.model.Movie
//
//@Dao
//interface ArticleDAO {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun upsert(article : Movie) : Long
//
//    @Query("SELECT * FROM articles")
//    fun getAllArticles() : LiveData<List<Movie>>
//
//    @Delete
//    suspend fun deleteArticle(article: Movie)
//}