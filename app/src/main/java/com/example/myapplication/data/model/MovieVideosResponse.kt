package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class MovieVideosResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("results") val results: List<MovieVideo>
)

data class MovieVideo(
    @SerializedName("key") val key: String,      // e.g. YouTube video id
    @SerializedName("name") val name: String,
    @SerializedName("site") val site: String,    // "YouTube", "Vimeo"
    @SerializedName("type") val type: String,    // "Trailer", "Teaser", ...
    @SerializedName("official") val official: Boolean
)
