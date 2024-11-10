package com.wsdev.trendingmoviesapp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.wsdev.trendingmoviesapp.network.RetrofitInstance
import kotlinx.coroutines.launch

@Composable
fun ApiKeyScreen(onApiKeySaved: (String) -> Unit) {
    var apiKeyInput by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }

    // Define the error message here
    val defaultErrorMessage = stringResource(id = R.string.invalid_api_key)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Text(
                text = stringResource(id = R.string.enter_api_key),
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = apiKeyInput,
                onValueChange = { apiKeyInput = it },
                label = { Text(stringResource(id = R.string.api_key)) },
                isError = isError,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                coroutineScope.launch {
                    if (apiKeyInput.isNotBlank()) {
                        isLoading = true
                        val result = validateApiKey(apiKeyInput, defaultErrorMessage)
                        if (result.isSuccessful) {
                            isError = false
                            onApiKeySaved(apiKeyInput)
                        } else {
                            isError = true
                            errorMessage = result.errorMessage
                        }
                        isLoading = false
                    } else {
                        isError = true
                        errorMessage = defaultErrorMessage
                    }
                }
            }) {
                Text(text = stringResource(id = R.string.save_api_key))
            }

            if (isError) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

suspend fun validateApiKey(apiKey: String, defaultErrorMessage: String): ValidationResult {
    return try {
        val response = RetrofitInstance.apiService.checkApiKey(apiKey)
        if (response.isSuccessful && response.body()?.success == true) {
            ValidationResult(true, "")
        } else {
            ValidationResult(false, response.body()?.status_message ?: defaultErrorMessage)
        }
    } catch (e: Exception) {
        ValidationResult(false, e.localizedMessage ?: defaultErrorMessage)
    }
}

data class ValidationResult(val isSuccessful: Boolean, val errorMessage: String)
