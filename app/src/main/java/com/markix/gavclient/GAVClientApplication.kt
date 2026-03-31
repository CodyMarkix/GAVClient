package com.markix.gavclient

import android.app.Application
import android.content.Context
import androidx.compose.runtime.compositionLocalOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.markix.gavclient.logic.repos.SettingsRepository

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_settings")

val LocalSettingsRepository = compositionLocalOf<SettingsRepository> {
    error("No SeedColorRepository provided!")
}
class GAVClientApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}