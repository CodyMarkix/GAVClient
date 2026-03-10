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
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import com.markix.gavclient.logic.viewmodels.GAVAPIViewModel
import com.markix.gavclient.logic.viewmodels.PGSchoolYearViewModel
import com.markix.gavclient.ui.apps.ioc.IOCAgenda
import com.markix.gavclient.ui.apps.ioc.IOCHome
import com.markix.gavclient.ui.apps.programming.ProgrammingHome
import com.markix.gavclient.ui.apps.programming.ProgrammingSchoolYearScreen
import com.markix.gavclient.ui.settings.AccountSettings
import com.markix.gavclient.ui.settings.LoginScreen

@SuppressLint("ContextCastToActivity")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavGraph(credentialManager: CredentialManager, activityContext: Context) {
    val navigationController = rememberNavController()
    val navActions: NavActions = remember(navigationController) {
        NavActions(navigationController)
    }
    val GAViewModel: GAVAPIViewModel = viewModel(LocalContext.current as ComponentActivity)

    NavHost(
        navController = navigationController,
        startDestination = NavDestinations.Login
    ) {
        composable<NavDestinations.Login>() {
            LoginScreen(
                GAViewModel,
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
            IOCHome(
                navActions,
                GAViewModel
            )
        }
        composable<NavDestinations.IOCAgenda>(
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up)
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Up)
            }
        ) { backStackEntry ->
            val route: NavDestinations.IOCAgenda = backStackEntry.toRoute()
            IOCAgenda(
                route.id,
                route.name,
                navActions,
                GAViewModel
            )
        }

        composable<NavDestinations.ProgrammingHome>(
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up)
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Up)
            }
        ) {
            ProgrammingHome(
                navActions,
                GAViewModel
            )
        }

        composable<NavDestinations.ProgrammingSchoolYear>(
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up)
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Up)
            }
        ) { backStackEntry ->
            val route: NavDestinations.ProgrammingSchoolYear = backStackEntry.toRoute()
            val pgsysVM: PGSchoolYearViewModel = viewModel()
            ProgrammingSchoolYearScreen(
                GAViewModel,
                navActions,
                route.schoolYear
            )
        }

        composable<NavDestinations.AccountSettings>(
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left)
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left)
            }
        ) {
            AccountSettings(
                navActions,
                GAViewModel
            )
        }
    }
}