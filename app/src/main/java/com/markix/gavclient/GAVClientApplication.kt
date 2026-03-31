package com.markix.gavclient

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "theme_settings")

class GAVClientApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Leave this empty.
        // Calling super.onCreate() ensures the default Android initialization happens.
        // No custom logic is added, so behavior remains identical to not having this class.
    }
}