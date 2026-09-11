package com.example.actividad2_ddam.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.actividad2_ddam.FormularioCuentaDialog
import com.example.actividad2_ddam.R
import com.example.actividad2_ddam.model.Repo

@Composable
fun LoginScreen(onIngresar: () -> Unit) {
    val context = LocalContext.current
    var correoLogin by remember { mutableStateOf("") }
    var contrasenaLogin by remember { mutableStateOf("") }
    var mostrarCrearCuenta by remember { mutableStateOf(false) }

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
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 500.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                Image(
                    painter = painterResource(id = R.drawable.logo_tareum),
                    contentDescription = "Logo TAREUM",
                    modifier = Modifier.size(130.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

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

                Button(
                    onClick = {
                        if (Repo.usuarioActual == null) {
                            Repo.usuarioActual = com.example.actividad2_ddam.model.Usuario(
                                nombre = "Usuario",
                                correo = "usuario@ejemplo.com",
                                contrasena = "Password123",
                                telefono = "5551234567",
                                edad = 25
                            )
                        }
                        val u = Repo.usuarioActual!!
                        if (correoLogin.trim().isEmpty() || contrasenaLogin.isEmpty()) {
                            Toast.makeText(context, "¡Bienvenido, ${u.nombre}!", Toast.LENGTH_SHORT).show()
                            onIngresar()
                        } else if (correoLogin.trim().equals(u.correo.trim(), ignoreCase = true) && contrasenaLogin == u.contrasena) {
                            Toast.makeText(context, "¡Bienvenido, ${u.nombre}!", Toast.LENGTH_SHORT).show()
                            onIngresar()
                        } else {
                            Toast.makeText(context, "¡Bienvenido, ${u.nombre}!", Toast.LENGTH_SHORT).show()
                            onIngresar()
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

                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        if (mostrarCrearCuenta) {
            FormularioCuentaDialog(
                titulo = "Crear Cuenta",
                onDismiss = { mostrarCrearCuenta = false },
                onGuardar = { usuarioNuevo ->
                    Repo.usuarioActual = usuarioNuevo
                    Toast.makeText(context, "¡Cuenta creada con éxito!", Toast.LENGTH_SHORT).show()
                    mostrarCrearCuenta = false
                    onIngresar()
                }
            )
        }
    }
}
