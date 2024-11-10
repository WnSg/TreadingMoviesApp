package com.wsdev.trendingmoviesapp.network

import com.google.gson.annotations.SerializedName

data class PopularMoviesResponse(
    @SerializedName("results") val movies: List<Movie>
)

data class Movie(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("overview") val overview: String
)