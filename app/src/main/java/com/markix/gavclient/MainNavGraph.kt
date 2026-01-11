package com.markix.gavclient

import android.annotation.SuppressLint
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.text.selection.LocalTextClassifierCoroutineContext
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.markix.gavclient.logic.viewmodels.GAVAPIViewModel
import com.markix.gavclient.ui.apps.ioc.IOCAgenda
import com.markix.gavclient.ui.apps.ioc.IOCHome
import com.markix.gavclient.ui.apps.programming.ProgrammingHome
import com.markix.gavclient.ui.settings.AccountSettings
import com.markix.gavclient.ui.settings.LoginScreen

@SuppressLint("ContextCastToActivity")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavGraph(credentialManager: CredentialManager, activityContext: Context) {
    val navigationController = rememberNavController()
    val GAViewModel: GAVAPIViewModel = viewModel(LocalContext.current as ComponentActivity)

    NavHost(
        navController = navigationController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                GAViewModel,
                onSignIn = {
                    navigationController.navigate(NavDestinations.IOC_HOME)
                }
            )
        }
        composable(
            route = NavDestinations.IOC_HOME,
            enterTransition = {
                fadeIn(tween(100))
            }
        ) {
            IOCHome(
                navigationController,
                GAViewModel
            )
        }
        composable(
            route = NavDestinations.IOC_AGENDA,
            arguments = listOf(
                navArgument(NavArguments.IOC_AGENDA_NAME) { type = NavType.StringType; defaultValue = "Bez jména" },
                navArgument(NavArguments.IOC_AGENDA_ID) { type = NavType.IntType; defaultValue = 1 }
            ),
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up)
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Up)
            }
        ) { entry ->
            IOCAgenda(
                entry.arguments?.getString(NavArguments.IOC_AGENDA_NAME) ?: "Bez Jména",
                entry.arguments?.getInt(NavArguments.IOC_AGENDA_ID) ?: 1,
                navigationController
            )
        }

        composable(
            route = NavDestinations.PROGRAMMING_HOME,
            arguments = listOf(

            ),
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up)
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Up)
            }
            ) {
            ProgrammingHome(
                navigationController
            )
        }

        composable(
            route = "account_settings",
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left)
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left)
            }
        ) {
            AccountSettings(
                navigationController,
                GAViewModel
            )
        }
    }
}