package com.wsdev.trendingmoviesapp.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Path

interface ApiService {
    @GET("authentication/token/new")
    suspend fun checkApiKey(@Query("api_key") apiKey: String): Response<AuthenticationResponse>

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): Response<PopularMoviesResponse>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Movie
}
