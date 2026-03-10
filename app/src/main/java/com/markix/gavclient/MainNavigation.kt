package com.markix.gavclient

import androidx.navigation.NavController
import kotlinx.serialization.Serializable

object NavArguments {
    const val IOC_AGENDA_NAME = "agenda_name"
    const val IOC_AGENDA_ID = "agenda_id"
}

object NavDestinations {
    @Serializable
    object Login

    @Serializable
    object AccountSettings

    @Serializable
    object IOCHome

    @Serializable
    data class IOCAgenda(val name: String, val id: Int)

    @Serializable
    object ProgrammingHome

    @Serializable
    data class ProgrammingSchoolYear(val schoolYear: List<String>)
}

class NavActions(private val navController: NavController) {
    fun navigateToLoginScreen() {
        navController.navigate(NavDestinations.Login)
    }

    fun navigateToAccountSettings() {
        navController.navigate(NavDestinations.AccountSettings)
    }

    fun navigateToIOCHome() {
        navController.navigate(NavDestinations.IOCHome) {
            popUpTo(NavDestinations.Login) {
                inclusive = true
            }
        }
    }

    fun navigateToIOCAgenda(id: Int, name: String) {
        navController.navigate(NavDestinations.IOCAgenda(name, id))
    }

    fun navigateToProgrammingHome() {
        navController.navigate(NavDestinations.ProgrammingHome)
    }

    fun navigateToProgrammingSchoolYear(schoolYear: List<String>) {
        navController.navigate(NavDestinations.ProgrammingSchoolYear(schoolYear))
    }
}