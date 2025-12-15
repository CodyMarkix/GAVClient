package com.markix.gavclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.credentials.CredentialManager
import com.markix.gavclient.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val credentialManager = CredentialManager.create(this)

        val sharedPreferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                MainNavGraph(credentialManager, this)
            }
        }
    }
}
