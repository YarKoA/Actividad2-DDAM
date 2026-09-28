package com.example.actividad2_ddam

import androidx.hilt.navigation.compose.hiltViewModel
import com.example.actividad2_ddam.auth.presentation.AuthViewModel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.actividad2_ddam.navigation.AppNavigation
import com.example.actividad2_ddam.model.Repo
import com.example.actividad2_ddam.ui.theme.Actividad2DDAMTheme
import dagger.hilt.android.AndroidEntryPoint
import com.google.firebase.auth.FirebaseAuth

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            FirebaseAuth.getInstance().signOut()
        }
        enableEdgeToEdge()
        setContent {
            Actividad2DDAMTheme(darkTheme = Repo.modoOscuro, dynamicColor = false) {

                val authVM : AuthViewModel = hiltViewModel()

                val user by authVM.user.collectAsState()

                AppNavigation(
                    startOnHome = user != null
                )

            }
        }
    }
}
