package com.markix.gavclient.logic.repos

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepository(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        val SEED_COLOR = longPreferencesKey("seed_color")
        val LAST_UPDATE_CHECK = stringPreferencesKey("last_update_check")
    }

    val seedColor: Flow<Long?> = dataStore.data
        .map { preferences ->
            preferences[SEED_COLOR]
        }

    val lastUpdateCheck: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[LAST_UPDATE_CHECK]
        }

    suspend fun saveSeedColor(colorLong: Long) {
        dataStore.edit { preferences ->
            preferences[SEED_COLOR] = colorLong
        }
    }

    suspend fun saveLastUpdateCheck(timestamp: String) {
        dataStore.edit { preferences ->
            preferences[LAST_UPDATE_CHECK] = timestamp
        }
    }
}