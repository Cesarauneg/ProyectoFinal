package com.example.appproyecto

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.compose.rememberNavController
import com.example.appproyecto.ui.theme.AppProyectoTheme
import com.example.appproyecto.ui.navigation.Screen
import com.example.appproyecto.ui.screens.*
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import androidx.compose.animation.*
import androidx.compose.animation.core.tween

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppProyectoTheme {
                val navController = rememberNavController()

                AnimatedNavHost(
                    navController = navController,
                    startDestination = Screen.Welcome.ruta,
                    enterTransition = { fadeIn(animationSpec = tween(500)) },
                    exitTransition = { fadeOut(animationSpec = tween(500)) },
                    ) {
                    composable(Screen.Register.ruta) {
                        RegisterScreen(navController)
                    }
                    composable(Screen.Login.ruta) {
                        LoginScreen(navController)
                    }
                    composable(Screen.Welcome.ruta) {
                        WelcomeScreen(navController)
                    }
                    composable(Screen.Config.ruta) {
                        ConfigScreen(navController)
                    }
                    composable(Screen.Menu.ruta) {
                        MenuScreen(navController)
                    }
                }
            }
        }
    }
}