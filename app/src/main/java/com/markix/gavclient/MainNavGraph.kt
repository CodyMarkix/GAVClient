package com.markix.gavclient

import android.content.Context
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.credentials.CredentialManager
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.markix.gavclient.ui.apps.ioc.IOCAgenda
import com.markix.gavclient.ui.apps.ioc.IOCHome
import com.markix.gavclient.ui.settings.AccountSettings
import com.markix.gavclient.ui.settings.LoginScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavGraph(credentialManager: CredentialManager, activityContext: Context) {
    val navigationController = rememberNavController()

    NavHost(
        navController = navigationController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                navigationController,
                onSignIn = {
                    navigationController.navigate(NavDestinations.IOC_HOME)
                }
            )
        }
        composable(NavDestinations.IOC_HOME) {
            IOCHome(navigationController)
        }
        composable(
            route = "ioc_agenda",
            arguments = listOf(
                navArgument(NavArguments.IOC_AGENDA_NAME) { type = NavType.StringType; defaultValue = "Bez jména" },
                navArgument(NavArguments.IOC_AGENDA_ID) { type = NavType.IntType}
            )
        ) { entry ->
            IOCAgenda(
                entry.arguments?.getString(NavArguments.IOC_AGENDA_NAME) ?: "Bez Jména",
                navigationController
            )
        }
        composable("account_settings") {
            AccountSettings()
        }
    }
}