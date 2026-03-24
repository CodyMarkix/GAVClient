package com.markix.gavclient

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.markix.gavclient.logic.viewmodels.GAVAPIViewModel
import com.markix.gavclient.logic.viewmodels.GAVClientViewModel
import com.markix.gavclient.ui.DebugAPI
import com.markix.gavclient.ui.apps.storage.StorageHome
import com.markix.gavclient.ui.apps.ioc.IOCAgenda
import com.markix.gavclient.ui.apps.ioc.IOCHome
import com.markix.gavclient.ui.apps.programming.ProgrammingHome
import com.markix.gavclient.ui.apps.programming.ProgrammingSchoolYearScreen
import com.markix.gavclient.ui.apps.seminars.SeminarsHome
import com.markix.gavclient.ui.settings.AccountSettings
import com.markix.gavclient.ui.settings.LoginScreen
import okhttp3.OkHttpClient

@SuppressLint("ContextCastToActivity")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavGraph(credentialManager: CredentialManager, activityContext: Context) {
    val navigationController = rememberNavController()
    val navActions: NavActions = remember(navigationController) {
        NavActions(navigationController)
    }
    val gaVM: GAVAPIViewModel = viewModel(LocalContext.current as ComponentActivity)
    val gaClientVM: GAVClientViewModel = viewModel(LocalContext.current as ComponentActivity)

    NavHost(
        navController = navigationController,
        startDestination = NavDestinations.Login
    ) {
        composable<NavDestinations.Login>() {
            LoginScreen(
                gaVM,
                credentialManager,
                onSignIn = {
                    navActions.navigateToIOCHome()
                }
            )
        }
        composable<NavDestinations.IOCHome>(
            enterTransition = {
                fadeIn(tween(100))
            }
        ) {
            if (gaClientVM.checkForUpdates()) {

            }

            IOCHome(
                navActions,
                gaVM
            )
        }
        composable<NavDestinations.IOCAgenda>(
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up)
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down)
            }
        ) { backStackEntry ->
            val route: NavDestinations.IOCAgenda = backStackEntry.toRoute()
            IOCAgenda(
                route.id,
                route.name,
                navActions,
                gaVM
            )
        }

        composable<NavDestinations.debugAPI>() {
            DebugAPI(gaVM)
        }

        composable<NavDestinations.ProgrammingHome>(
            enterTransition = {
                fadeIn()
            },
            exitTransition = {
                fadeOut()
            }
        ) {
            ProgrammingHome(
                navActions,
                gaVM
            )
        }

        composable<NavDestinations.ProgrammingSchoolYear>(
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up)
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down)
            }
        ) { backStackEntry ->
            val route: NavDestinations.ProgrammingSchoolYear = backStackEntry.toRoute()
            ProgrammingSchoolYearScreen(
                gaVM,
                navActions,
                route.schoolYear
            )
        }

        composable<NavDestinations.AccountSettings>(
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left)
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
            }
        ) {
            AccountSettings(
                navActions,
                gaVM
            )
        }

        composable<NavDestinations.SeminarsHome>(
            enterTransition = {
                fadeIn()
            },
            exitTransition = {
                fadeOut()
            }
        ) {
            SeminarsHome(gaVM, navActions)
        }

        composable<NavDestinations.StorageHome>(
            enterTransition = {
                fadeIn()
            },
            exitTransition = {
                fadeOut()
            }
        ) {
            StorageHome(navActions, gaVM)
        }
    }
}