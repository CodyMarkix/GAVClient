package com.markix.gavclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.credentials.CredentialManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.markix.gavclient.logic.repos.SettingsRepository
import com.markix.gavclient.logic.viewmodels.ThemeViewModel
import com.markix.gavclient.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context = this
        val credentialManager = CredentialManager.create(context)
        val dataStore = context.dataStore


        enableEdgeToEdge()

        // Lines 45 - 49 from StackOverflow: https://stackoverflow.com/questions/69688138/how-to-hide-navigationbar-and-statusbar-in-jetpack-compose#69689196
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        insetsController.apply {
            hide(WindowInsetsCompat.Type.navigationBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        setContent {
            val repository = remember { SettingsRepository(dataStore) }
            val seedColor by repository.seedColor.collectAsState(initial = null)

            AppTheme(
                seedColor = seedColor
            ) {
                 CompositionLocalProvider(LocalSettingsRepository provides repository) {
                     Box(
                         modifier = Modifier
                             .fillMaxSize()
                             .background(MaterialTheme.colorScheme.surface)
                     )
                     MainNavGraph(credentialManager, context)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()


    }
}
