package com.example.actividad2_ddam

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.actividad2_ddam.ui.theme.Actividad2DDAMTheme

// Actividad inicial que gestiona el acceso del usuario
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Actividad2DDAMTheme {
                LoginScreen(
                    onIngresar = {
                        // Navegacion hacia el menu principal tras autenticarse
                        val intent = Intent(this, MainMenuActivity::class.java)
                        startActivity(intent)
                    }
                )
            }
        }
    }
}

// Interfaz de usuario para la pantalla de inicio de sesion
@Composable
fun LoginScreen(onIngresar: () -> Unit) {
    val context = LocalContext.current

    // Configuracion del degradado de fondo
    val fondo = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF33436F),
            Color(0xFF4B84A8),
            Color(0xFF8BB5CE)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(fondo)
            .padding(horizontal = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Logo distintivo de la aplicacion
            Image(
                painter = painterResource(id = R.drawable.logo_tareum),
                contentDescription = "Logo TAREUM",
                modifier = Modifier.size(150.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Titulo principal de la aplicacion
            Text(
                text = "TAREUM",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(34.dp))

            // Acceso estandar como usuario
            Button(
                onClick = { onIngresar() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEDE2FF))
            ) {
                Text(
                    text = "Ingresar como USER",
                    color = Color(0xFF385A79),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(26.dp))

            // Acceso alternativo mediante cuenta externa
            Button(
                onClick = {
                    Toast.makeText(context, "Inicio con Google", Toast.LENGTH_SHORT).show()
                    onIngresar()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEDE2FF))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Ingresar con cuenta de Google",
                        color = Color(0xFF385A79),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "G",
                        color = Color(0xFF4285F4),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Aviso sobre integracion con calendario
            Text(
                text = "*Permite conectar tu calendario con la app",
                fontSize = 11.sp,
                color = Color.White.copy(alpha = 0.90f)
            )
        }
    }
}
