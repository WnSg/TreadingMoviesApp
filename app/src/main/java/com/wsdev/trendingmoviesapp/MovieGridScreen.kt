package com.wsdev.trendingmoviesapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.wsdev.trendingmoviesapp.network.RetrofitInstance
import com.wsdev.trendingmoviesapp.network.Movie
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import coil.compose.AsyncImage
import androidx.compose.runtime.Composable
import com.wsdev.trendingmoviesapp.network.MovieRepository
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.navigation.NavHostController
import androidx.compose.foundation.clickable




@Composable
fun MovieGridScreen(apiKey: String, navController: NavHostController) {
    val movieRepository = remember { MovieRepository(RetrofitInstance.apiService) }
    var movieList by remember { mutableStateOf<List<Movie>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var currentPage by remember { mutableStateOf(1) }
    val gridState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(apiKey) {
        loadMovies(apiKey, movieRepository, currentPage) { newMovies ->
            movieList = movieList + newMovies
            isLoading = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(150.dp),
            state = gridState,
            modifier = Modifier.padding(8.dp)
        ) {
            items(movieList) { movie ->
                MovieItem(movie = movie, navController = navController)
            }

            // Indicador de carga al final
            if (isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }

        // Detectar el final de la lista y cargar más elementos
        if (!isLoading && gridState.isScrolledToEnd()) {
            LaunchedEffect(Unit) {
                coroutineScope.launch {
                    isLoading = true
                    currentPage++
                    loadMovies(apiKey, movieRepository, currentPage) { newMovies ->
                        movieList = movieList + newMovies
                        isLoading = false
                    }
                }
            }
        }
    }
}

@Composable
fun MovieItem(movie: Movie, navController: NavHostController) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                navController.navigate("movieDetail/${movie.id}")
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500${movie.posterPath}",
                contentDescription = movie.title,
                modifier = Modifier
                    .height(220.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = movie.title,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1
            )
        }
    }
}


// Función auxiliar para detectar si se ha llegado al final de la lista
fun LazyGridState.isScrolledToEnd(): Boolean {
    val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
    return lastVisibleItemIndex >= layoutInfo.totalItemsCount - 1
}

suspend fun loadMovies(
    apiKey: String,
    repository: MovieRepository,
    page: Int,
    onNewMovies: (List<Movie>) -> Unit
) {
    val newMovies = repository.loadMovies(apiKey, page)
    if (newMovies.isNotEmpty()) {
        onNewMovies(newMovies)
    }
}
