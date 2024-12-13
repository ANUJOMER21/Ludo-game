package com.royalkingmatka.ludogame.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(context: Context) {

    // Using the property delegate for DataStore
    private val Context.dataStore by preferencesDataStore("app_prefs")

    private val dataStore = context.dataStore

    suspend fun setValue(key: Preferences.Key<String>, value: String) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    fun getValue(key: Preferences.Key<String>): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[key]
        }
    }

    // Helper functions for specific keys
    suspend fun setEmail(email: String) {
        setValue(EMAIL, email)
    }

    fun getEmail(): Flow<String?> {
        return getValue(EMAIL)
    }

    suspend fun setUsername(username: String) {
        setValue(USERNAME, username)
    }

    fun getUsername(): Flow<String?> {
        return getValue(USERNAME)
    }

    companion object {
        private val EMAIL = stringPreferencesKey("google_email")
        private val USERNAME = stringPreferencesKey("google_username")
    }
}
