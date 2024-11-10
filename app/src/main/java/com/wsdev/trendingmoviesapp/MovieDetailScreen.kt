package com.wsdev.trendingmoviesapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.wsdev.trendingmoviesapp.network.Movie


@Composable
fun MovieDetailScreen(movie: Movie) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter("https://image.tmdb.org/t/p/w500${movie.posterPath}"),
            contentDescription = movie.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = movie.title, style = MaterialTheme.typography.titleLarge)
        Text(text = "Release Date: ${movie.releaseDate}", style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = movie.overview, style = MaterialTheme.typography.bodyMedium)
    }
}