package com.wsdev.trendingmoviesapp.network

import retrofit2.HttpException
import java.io.IOException

class MovieRepository(private val apiService: ApiService) {

    suspend fun loadMovies(apiKey: String, page: Int = 1): List<Movie> {
        return try {
            val response = apiService.getPopularMovies(apiKey, page = page)
            if (response.isSuccessful) {
                response.body()?.movies ?: emptyList() // Devuelve la lista de películas si la respuesta es válida
            } else {
                // Agregar un log o manejar el código de error
                emptyList()
            }
        } catch (e: HttpException) {
            // Maneja errores HTTP específicos
            println("HTTP Exception: ${e.message()}")
            emptyList()
        } catch (e: IOException) {
            // Maneja errores de conectividad (red, etc.)
            println("Network error: ${e.message}")
            emptyList()
        } catch (e: Exception) {
            // Maneja otros tipos de errores
            println("Unknown error: ${e.message}")
            emptyList()
        }
    }

    suspend fun getMovieDetails(apiKey: String, movieId: Int): Movie {
        return apiService.getMovieDetails(movieId, apiKey)
    }

}