package com.wsdev.trendingmoviesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Brightness7
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.wsdev.trendingmoviesapp.ui.theme.TrendingMoviesAppTheme
import com.wsdev.trendingmoviesapp.utils.DataStoreManager
import com.wsdev.trendingmoviesapp.utils.updateLocale
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import com.wsdev.trendingmoviesapp.MovieGridScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.wsdev.trendingmoviesapp.network.MovieRepository
import com.wsdev.trendingmoviesapp.network.RetrofitInstance
import com.wsdev.trendingmoviesapp.network.Movie


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    val context = LocalContext.current
    val dataStoreManager = remember { DataStoreManager(context) }
    val coroutineScope = rememberCoroutineScope()
    val navController = rememberNavController()

    var apiKey by remember { mutableStateOf<String?>(null) }
    var isApiKeyLoaded by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var isDarkTheme by remember { mutableStateOf(false) }
    var isLanguageMenuExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            isLoading = true
            val savedKey = dataStoreManager.getApiKey().first()
            if (!savedKey.isNullOrEmpty()) {
                apiKey = savedKey
            }
            isApiKeyLoaded = true
            isLoading = false
        }
    }

    TrendingMoviesAppTheme(darkTheme = isDarkTheme) {
        Scaffold(
            topBar = {
                @OptIn(ExperimentalMaterial3Api::class)
                TopAppBar(
                    title = { Text(text = stringResource(id = R.string.title_trending_movies)) },
                    actions = {
                        IconButton(onClick = { isDarkTheme = !isDarkTheme }) {
                            Icon(
                                imageVector = if (isDarkTheme) Icons.Filled.Brightness7 else Icons.Filled.Brightness4,
                                contentDescription = stringResource(id = R.string.theme_switch)
                            )
                        }
                        IconButton(onClick = { isLanguageMenuExpanded = true }) {
                            Icon(
                                imageVector = Icons.Filled.Language,
                                contentDescription = stringResource(id = R.string.language_switch)
                            )
                        }
                        DropdownMenu(
                            expanded = isLanguageMenuExpanded,
                            onDismissRequest = { isLanguageMenuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("English") },
                                onClick = {
                                    updateLocale(context, "en")
                                    isLanguageMenuExpanded = false
                                    (context as? ComponentActivity)?.recreate()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Español") },
                                onClick = {
                                    updateLocale(context, "es")
                                    isLanguageMenuExpanded = false
                                    (context as? ComponentActivity)?.recreate()
                                }
                            )
                        }
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            },
            bottomBar = {
                BottomNavigationBar(navController)
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                if (isLoading) {
                    LoadingScreen()
                } else {
                    if (!isApiKeyLoaded || apiKey.isNullOrEmpty()) {
                        ApiKeyScreen(
                            onApiKeySaved = { key ->
                                coroutineScope.launch {
                                    isLoading = true
                                    dataStoreManager.saveApiKey(key)
                                    apiKey = key
                                    isApiKeyLoaded = true
                                    isLoading = false
                                }
                            }
                        )
                    } else {
                        HomeScreenContent(navController, apiKey!!)
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    NavigationBar {
        val items = listOf("home", "favorites", "profile")
        items.forEach { screen ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = when (screen) {
                            "home" -> Icons.Filled.Home
                            "favorites" -> Icons.Filled.Favorite
                            "profile" -> Icons.Filled.Person
                            else -> Icons.Filled.Home
                        },
                        contentDescription = null
                    )
                },
                label = { Text(screen.replaceFirstChar { it.uppercase() }) },
                selected = navController.currentDestination?.route == screen,
                onClick = {
                    // Navega a la pantalla de inicio
                    if (screen == "home") {
                        navController.navigate("movieGrid") {
                            popUpTo("movieGrid") { inclusive = true }
                            launchSingleTop = true
                        }
                    } else {
                        navController.navigate(screen) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun HomeScreenContent(navController: NavHostController, apiKey: String) {
    val movieRepository = remember { MovieRepository(RetrofitInstance.apiService) }

    NavHost(navController, startDestination = "movieGrid") {
        composable("movieGrid") {
            MovieGridScreen(apiKey = apiKey, navController = navController)
        }
        composable("movieDetail/{movieId}") { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")?.toIntOrNull()
            var movie by remember { mutableStateOf<Movie?>(null) }
            var isLoading by remember { mutableStateOf(true) }

            LaunchedEffect(movieId) {
                if (movieId != null) {
                    isLoading = true
                    try {
                        movie = movieRepository.getMovieDetails(apiKey, movieId)
                    } catch (e: Exception) {
                        // Manejo de errores, por ejemplo, mostrando un mensaje al usuario
                        movie = null
                    } finally {
                        isLoading = false
                    }
                }
            }

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center))
            } else {
                movie?.let {
                    MovieDetailScreen(movie = it)
                } ?: run {
                    Text("Movie details not found", modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center))
                }
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}
