package com.wsdev.trendingmoviesapp.utils

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class DataStoreManager(private val context: Context) {
    private val dataStore = context.dataStore

    companion object {
        private val API_KEY = stringPreferencesKey("api_key")
    }

    fun getApiKey(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[API_KEY]
        }
    }

    suspend fun saveApiKey(apiKey: String) {
        dataStore.edit { preferences ->
            preferences[API_KEY] = apiKey
        }
    }
}
