package com.example.actividad2_ddam.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.actividad2_ddam.ui.screens.LoginScreen
import com.example.actividad2_ddam.ui.screens.EventListScreen
import com.example.actividad2_ddam.ui.screens.RegisterScreen

@Composable
fun AppNavHost(
    navController : NavHostController = rememberNavController(),
    startOnHome : Boolean
){
    NavHost(
        navController = navController,
        startDestination = if(startOnHome) NavRoute.Home.route else NavRoute.Login.route
    ){
        composable(NavRoute.Login.route){
            LoginScreen(
                onGoToRegister = {navController.navigate(NavRoute.Register.route)},
                onLoggedIn = {navController.navigate(NavRoute.Home.route)}
            )
        }

        composable (NavRoute.Register.route){
            RegisterScreen(
                onRegistered = {navController.navigate(NavRoute.Home.route){popUpTo(0)} },
                onBackToLogin = { navController.popBackStack() }
            )
        }

        composable (NavRoute.Home.route){
            EventListScreen(
                navController = navController,
                onAddEventClick = {
                    // Aquí colocas la acción o navegación al presionar añadir evento
                    // Ejemplo: navController.navigate(NavRoute.AddEvent.route)
                }
            )
        }
    }
}