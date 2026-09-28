import re

filepath = r'C:\Users\anvaq\Downloads\Actividad2-DDAM\app\src\main\java\com\example\actividad2_ddam\navigation\AppNavigation.kt'
with open(filepath, 'r', encoding='utf-8') as f:
    content = f.read()

# Make sure imports are present
imports_to_add = "import androidx.compose.animation.scaleIn\nimport androidx.compose.animation.scaleOut\nimport androidx.compose.animation.core.FastOutSlowInEasing\n"
if "scaleIn" not in content:
    content = re.sub(r'(import [^\n]+\n)(?!.*import )', r'\1' + imports_to_add, content, count=1, flags=re.DOTALL)

# Let's just define the new content for AppNavigation entirely, it's safer and cleaner.
new_nav = '''package com.example.actividad2_ddam.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.FastOutSlowInEasing
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
    const val REGISTER = "register"
    const val EVENT_LIST = "event_list"
    const val EVENT_FORM = "event_form"
    const val EVENT_EDIT = "event_edit/{eventId}"
    const val CALENDAR = "calendar"
    const val SETTINGS = "settings"

    fun eventEdit(eventId: Int) = "event_edit/"
}

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startOnHome: Boolean = false
) {
    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH,
        modifier = modifier,

        // Transicion global (fade in + scale para un efecto de desenfoque de movimiento/profundidad)
        enterTransition = {
            fadeIn(animationSpec = tween(400, easing = FastOutSlowInEasing)) +
            scaleIn(initialScale = 0.92f, animationSpec = tween(400, easing = FastOutSlowInEasing))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(400, easing = FastOutSlowInEasing)) +
            scaleOut(targetScale = 1.08f, animationSpec = tween(400, easing = FastOutSlowInEasing))
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(400, easing = FastOutSlowInEasing)) +
            scaleIn(initialScale = 1.08f, animationSpec = tween(400, easing = FastOutSlowInEasing))
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(400, easing = FastOutSlowInEasing)) +
            scaleOut(targetScale = 0.92f, animationSpec = tween(400, easing = FastOutSlowInEasing))
        }
    ) {

        composable(
            route = Routes.SPLASH,
            exitTransition = { fadeOut(animationSpec = tween(300)) } // Crossfade especial para Splash->Login
        ) {
            SplashScreen(
                onSplashFinished = {
                    val nextRoute = if (startOnHome) Routes.EVENT_LIST else Routes.LOGIN
                    navController.navigate(nextRoute) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Routes.LOGIN,
            enterTransition = { fadeIn(animationSpec = tween(300)) } // Entra sin escala para coincidir con Splash
        ) {
            LoginScreen(
                onLoggedIn = {
                    navController.navigate(Routes.EVENT_LIST) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onGoToRegister = {
                    navController.navigate(Routes.REGISTER)
                }
            )
        }

        composable(route = Routes.REGISTER) {
            RegisterScreen(
                onRegistered = {
                    navController.popBackStack()
                },
                onGoToLogin = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Routes.EVENT_LIST) {
            EventListScreen(
                navController = navController,
                onAddEventClick = { navController.navigate(Routes.EVENT_FORM) }
            )
        }

        composable(route = Routes.EVENT_FORM) {
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

        composable(route = Routes.CALENDAR) {
            CalendarScreen(navController = navController)
        }

        composable(route = Routes.SETTINGS) {
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
'''

with open(filepath, 'w', encoding='utf-8') as f:
    f.write(new_nav)
print("Updated AppNavigation.kt with scale/fade animations")
