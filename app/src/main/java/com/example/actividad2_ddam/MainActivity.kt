package com.example.actividad2_ddam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.actividad2_ddam.ui.theme.Actividad2DDAMTheme

// --- Punto 1: Funciones principales ---
// Por: Ricardo

// Función botón login
fun autenticarUsuario(usuario: String, esGoogle: Boolean): Boolean {
    return usuario.isNotBlank() && (esGoogle || usuario.length >= 4)
}

// Función crear actividad
fun crearActividad(titulo: String, hora: String, dia: String): String {
    return "Actividad '$titulo' creada para el $dia a las $hora."
}


// --- Espacio para el equipo ---
// Punto 2: Colección de datos
// Punto 3: Manejo de excepciones y null safety
// Punto 4: Clase, Objeto e Interfaz
// Punto 5: Función lambda / orden superior


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Actividad2DDAMTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TareumPantalla(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

// UI con botones conectados a las funciones de Ricardo
@Composable
fun TareumPantalla(modifier: Modifier = Modifier) {
    var resultado by remember { mutableStateOf("Esperando acción...") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "TAREUM", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(24.dp))

        // Botón login
        Button(
            onClick = {
                val ok = autenticarUsuario("Ricardo", true)
                resultado = if (ok) "Inicio de sesión correcto" else "Error al ingresar"
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ingresar como USER")
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Botón crear actividad
        Button(
            onClick = {
                resultado = crearActividad("Salir a trotar", "10:30 am", "Vie")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Crear Actividad")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = resultado, style = MaterialTheme.typography.bodyMedium)
    }
}

@Preview(showBackground = true)
@Composable
fun TareumPreview() {
    Actividad2DDAMTheme {
        TareumPantalla()
    }
}