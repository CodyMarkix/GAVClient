package com.markix.gavclient.logic.repos

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SeedColorRepository(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        val SEED_COLOR = longPreferencesKey("seed_color")
    }

    val seedColor: Flow<Long?> = dataStore.data
        .map { preferences ->
            preferences[SEED_COLOR]
        }

    suspend fun saveSeedColor(colorLong: Long) {
        dataStore.edit { preferences ->
            preferences[SEED_COLOR] = colorLong
        }
    }
}