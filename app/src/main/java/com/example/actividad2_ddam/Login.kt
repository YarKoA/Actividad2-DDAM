package com.example.actividad2_ddam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.actividad2_ddam.auth.data.AuthViewModel
import com.example.actividad2_ddam.model.Repo
import com.example.actividad2_ddam.navigation.AppNavHost
import com.example.actividad2_ddam.navigation.AppNavigation
import com.example.actividad2_ddam.ui.theme.Actividad2DDAMTheme
import dagger.hilt.android.AndroidEntryPoint

// Actividad principal con Jetpack Compose Navigation



@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Actividad2DDAMTheme(darkTheme = Repo.modoOscuro, dynamicColor = false) {
                val authVM : AuthViewModel = hiltViewModel()

                val user by authVM.user.collectAsState()

                AppNavHost(
                    startOnHome = user != null
                )
            }
        }
    }
}
