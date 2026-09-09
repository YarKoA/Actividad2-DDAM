package com.example.actividad2_ddam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.actividad2_ddam.model.Repo
import com.example.actividad2_ddam.navigation.AppNavigation
import com.example.actividad2_ddam.ui.theme.Actividad2DDAMTheme
import dagger.hilt.android.AndroidEntryPoint

// Actividad principal con Jetpack Compose Navigation y Hilt
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Actividad2DDAMTheme(darkTheme = Repo.modoOscuro, dynamicColor = false) {
                AppNavigation()
            }
        }
    }
}
