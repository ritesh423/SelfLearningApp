package com.example.myapplication.data.network

import com.example.myapplication.utils.ApiConstants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private val logging = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}
private val client = OkHttpClient.Builder()
    .addInterceptor(logging)
    .build()

val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(ApiConstants.BASE_URL)
    .client(client)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val tmdbService: TmdbService = retrofit.create(TmdbService::class.java)
