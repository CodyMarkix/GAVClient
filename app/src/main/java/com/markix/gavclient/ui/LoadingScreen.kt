package com.markix.gavclient.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.markix.gavclient.NavDestinations
import kotlinx.coroutines.delay

@Composable
fun LoadingScreen(navController: NavController, destination: NavDestinations, timeDelay: Long) {
    LaunchedEffect(true) {
        delay(timeDelay)
        navController.navigate(destination)
    }

    Scaffold() { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {

        }
    }
}