package com.example.actividad2_ddam.navigation

sealed class NavRoute(val route : String){
    data object Login : NavRoute("login")
    data object Register : NavRoute("register")
    data object Home : NavRoute("home")
}