package com.example.myapplication.data.model

@kotlinx.parcelize.Parcelize
data class Company(
    @com.google.gson.annotations.SerializedName("id") val id: Int,
    @com.google.gson.annotations.SerializedName("name") val name: String
) : android.os.Parcelable

data class CompanySearchResponse(
    @com.google.gson.annotations.SerializedName("results")
    val results: List<Company>
)
