//package com.example.actividad2_ddam
//
//import androidx.hilt.navigation.compose.hiltViewModel
//import com.example.actividad2_ddam.auth.data.AuthViewModel
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Surface
//import androidx.compose.runtime.collectAsState
//import androidx.compose.ui.Modifier
//import com.example.actividad2_ddam.navigation.AppNavHost
//import com.example.actividad2_ddam.ui.theme.Actividad2DDAMTheme
//import dagger.hilt.android.AndroidEntryPoint

//@AndroidEntryPoint
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            Actividad2DDAMTheme {
//
//                val authVM : AuthViewModel = hiltViewModel()
//
//                val user by authVM.user.collectAsState()
//
//                AppNavHost(
//                    startOnHome = user != null
//                )
//
//            }
//        }
//    }
//}
