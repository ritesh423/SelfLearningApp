package com.example.myapplication.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieDetailsResponse(
    @SerializedName("id")
    val id: Int,

    @SerializedName("overview")
    val overview: String,

    @SerializedName("poster_path")
    val posterPath: String?,

    // ← for the collapsing backdrop image
    @SerializedName("backdrop_path")
    val backdropPath: String?,

    @SerializedName("release_date")
    val releaseDate: String?,

    // ← used in the “Runtime: 120 min” line
    @SerializedName("runtime")
    val runtime: Int?,

    // ← shown under the title as the “tagline”
    @SerializedName("tagline")
    val tagline: String?,

    @SerializedName("title")
    val title: String?,

    // ← your RatingBar and vote count
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("vote_count")
    val voteCount: Int,

    // ← the list of genres that we turn into Chips
    @SerializedName("genres")
    val genres: List<Genre>
) : Parcelable
