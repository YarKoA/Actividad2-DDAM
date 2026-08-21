package com.example.actividad2_ddam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.actividad2_ddam.ui.theme.Actividad2DDAMTheme

// --- Punto 1:las funciones-
// Por: Ricardo

// Función botón login
fun autenticarUsuario(usuario: String, esGoogle: Boolean): Boolean {
    return usuario.isNotBlank() && (esGoogle || usuario.length >= 4)
}

// Función crear actividad
fun crearActividad(titulo: String, hora: String, dia: String): String {
    return "Actividad '$titulo' creada para el $dia a las $hora."
}


// Lo que falta
// Punto 2: Colección de datos
// Punto 3: Manejo de excepciones y null safety
// Punto 4: Clase, Objeto e Interfaz
// Punto 5: Función lambda / orden superior


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Pruebas rápidas en consola para checar que mis funciones si sirvan
        val loginOk = autenticarUsuario("Ricardo", true)
        val nuevaTarea = crearActividad("Salir a trotar", "10:30 am", "Vie")
        println("Login estado: $loginOk | $nuevaTarea")

        setContent {
            Actividad2DDAMTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Ricardo",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Que onda wey $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Actividad2DDAMTheme {
        Greeting("Android")
    }
}