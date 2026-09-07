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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.actividad2_ddam.ui.theme.Actividad2DDAMTheme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

// Actividad principal de inicio de sesión (Login)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Actividad2DDAMTheme(darkTheme = Repo.modoOscuro, dynamicColor = false) {
                LoginScreen(
                    onIngresar = {
                        val intent = Intent(this, MainMenuActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                )
            }
        }
    }
}

// Pantalla composable de Login con validación de credenciales
@Composable
fun LoginScreen(onIngresar: () -> Unit) {
    val context = LocalContext.current
    var correoLogin by remember { mutableStateOf("") }
    var contrasenaLogin by remember { mutableStateOf("") }
    var mostrarCrearCuenta by remember { mutableStateOf(false) }

    // Fondo degradado de la pantalla de inicio
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

            // Logotipo de la app
            Image(
                painter = painterResource(id = R.drawable.logo_tareum),
                contentDescription = "Logo TAREUM",
                modifier = Modifier.size(130.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Campo de texto para el Correo electrónico
            OutlinedTextField(
                value = correoLogin,
                onValueChange = { correoLogin = it },
                label = { Text("Correo electrónico", color = Color.White) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF3B5E8C).copy(alpha = 0.5f),
                    unfocusedContainerColor = Color(0xFF3B5E8C).copy(alpha = 0.3f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.7f),
                    cursorColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Campo de texto para la Contraseña
            OutlinedTextField(
                value = contrasenaLogin,
                onValueChange = { contrasenaLogin = it },
                label = { Text("Contraseña", color = Color.White) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF3B5E8C).copy(alpha = 0.5f),
                    unfocusedContainerColor = Color(0xFF3B5E8C).copy(alpha = 0.3f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.7f),
                    cursorColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Botón de Inicio de Sesión con validación previa de cuenta
            Button(
                onClick = {
                    when {
                        Repo.usuarioActual == null -> {
                            Toast.makeText(context, "Primero debes crear tu cuenta", Toast.LENGTH_SHORT).show()
                        }
                        correoLogin.trim().isEmpty() || contrasenaLogin.isEmpty() -> {
                            Toast.makeText(context, "Ingresa tu correo y contraseña", Toast.LENGTH_SHORT).show()
                        }
                        correoLogin.trim().equals(Repo.usuarioActual!!.correo.trim(), ignoreCase = true) &&
                                contrasenaLogin == Repo.usuarioActual!!.contrasena -> {
                            Toast.makeText(context, "¡Bienvenido de nuevo, ${Repo.usuarioActual!!.nombre}!", Toast.LENGTH_SHORT).show()
                            onIngresar()
                        }
                        else -> {
                            Toast.makeText(context, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEDE2FF))
            ) {
                Text(
                    text = "Iniciar Sesión",
                    color = Color(0xFF385A79),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para abrir el formulario de Creación de Cuenta
            Button(
                onClick = { mostrarCrearCuenta = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B5E8C))
            ) {
                Text(
                    text = "Crear Cuenta",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón opcional de Google
            Button(
                onClick = {
                    Toast.makeText(context, "Funcionalidad no disponible", Toast.LENGTH_SHORT).show()
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

            Text(
                text = "*Permite conectar tu calendario con la app",
                fontSize = 11.sp,
                color = Color.White.copy(alpha = 0.90f)
            )
        }

        // Diálogo flotante para registrar una nueva cuenta
        if (mostrarCrearCuenta) {
            FormularioCuentaDialog(
                titulo = "Crear Cuenta",
                onDismiss = { mostrarCrearCuenta = false },
                onGuardar = { usuarioNuevo ->
                    Repo.usuarioActual = usuarioNuevo
                    Toast.makeText(context, "Cuenta creada con éxito. Ahora puedes iniciar sesión.", Toast.LENGTH_LONG).show()
                    mostrarCrearCuenta = false
                    correoLogin = usuarioNuevo.correo
                    contrasenaLogin = usuarioNuevo.contrasena
                }
            )
        }
    }
}
