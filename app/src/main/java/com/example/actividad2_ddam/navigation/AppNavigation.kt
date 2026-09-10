package com.example.actividad2_ddam.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.actividad2_ddam.ui.screens.*

object Routes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val EVENT_LIST = "event_list"
    const val EVENT_FORM = "event_form"
    const val EVENT_EDIT = "event_edit/{eventId}"
    const val CALENDAR = "calendar"
    const val SETTINGS = "settings"

    fun eventEdit(eventId: Int) = "event_edit/$eventId"
}

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Routes.SPLASH
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() },
        popEnterTransition = { fadeIn() },
        popExitTransition = { fadeOut() }
    ) {
        composable(Routes.SPLASH) {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.LOGIN) {
            LoginScreen(
                onIngresar = {
                    navController.navigate(Routes.EVENT_LIST) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.EVENT_LIST) {
            EventListScreen(
                navController = navController,
                onAddEventClick = { navController.navigate(Routes.EVENT_FORM) }
            )
        }

        composable(Routes.EVENT_FORM) {
            EventFormScreen(
                navController = navController,
                onCerrar = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.EVENT_EDIT,
            arguments = listOf(navArgument("eventId") { type = NavType.IntType })
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getInt("eventId") ?: -1
            EventEditScreen(
                eventId = eventId,
                onCerrar = { navController.popBackStack() }
            )
        }

        composable(Routes.CALENDAR) {
            CalendarScreen(navController = navController)
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(
                navController = navController,
                onCerrarSesion = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
