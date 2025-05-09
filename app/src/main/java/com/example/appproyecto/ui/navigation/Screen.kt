package com.example.appproyecto.ui.navigation

sealed class Screen(val ruta: String) {
    object Register : Screen("register")
    object Login : Screen("login")
    object Welcome : Screen("welcome")
    object Config : Screen("cofig")
    object Menu : Screen("menu")
}