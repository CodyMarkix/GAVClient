package com.markix.gavclient

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.credentials.CredentialManager
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.android.volley.toolbox.Volley
import com.markix.gavclient.apps.ioc.IOCHome
import com.markix.gavclient.settings.AccountSettings
import com.markix.gavclient.settings.LoginScreen
import com.markix.gavclient.ui.theme.GyarabVýukaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var queue = Volley.newRequestQueue(this)
        val credentialManager = CredentialManager.create(this)

        val sharedPreferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this)
        enableEdgeToEdge()
        setContent {
            GyarabVýukaTheme {
                MainScreen(credentialManager, this)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(credentialManager: CredentialManager, activityContext: Context) {
    val navigationController = rememberNavController()

    NavHost(navController = navigationController, startDestination = "login", builder = {
        composable("login") {
            LoginScreen(navigationController, credentialManager, activityContext)
        }
        composable("ioc_home") {
            IOCHome(navigationController)
        }
        composable("account_settings") {
            AccountSettings()
        }
    })
}