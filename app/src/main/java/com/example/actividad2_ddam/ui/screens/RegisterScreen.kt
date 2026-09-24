package com.example.actividad2_ddam.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.actividad2_ddam.auth.presentation.register.RegisterViewModel
import com.example.actividad2_ddam.model.Repo
import com.example.actividad2_ddam.model.Usuario

@Composable
fun RegisterScreen(
    onBackToLogin: () -> Unit,
    onRegistered: () -> Unit,
    vm : RegisterViewModel = hiltViewModel()
) {

    val ui by vm.ui.collectAsState()

    var email by rememberSaveable { mutableStateOf("") }
    var pass by rememberSaveable { mutableStateOf("") }


    LaunchedEffect(Unit) {
        vm.event.collect{ event ->
            if (event is RegisterViewModel.RegisterEvent.Success){
                onRegistered()
            }
        }
    }
    val context = LocalContext.current

    // Estados para guardar la información del formulario
    var nombre by rememberSaveable { mutableStateOf("") }
    var correo by rememberSaveable { mutableStateOf("") }
    var contrasena by rememberSaveable { mutableStateOf("") }
    var telefono by rememberSaveable { mutableStateOf("") }
    var edad by rememberSaveable { mutableStateOf("") }

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

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Crear Nueva Cuenta",
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(24.dp))

                // NOMBRE
                CustomTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = "Nombre completo"
                )

                Spacer(modifier = Modifier.height(12.dp))

                // CORREO
                CustomTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Correo electrónico",
                    keyboardType = KeyboardType.Email
                )

                Spacer(modifier = Modifier.height(12.dp))

                // CONTRASEÑA
                CustomTextField(
                    value = pass,
                    onValueChange = { pass = it },
                    label = "Contraseña",
                    isPassword = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // TELÉFONO
                CustomTextField(
                    value = telefono,
                    onValueChange = { telefono = it },
                    label = "Teléfono",
                    keyboardType = KeyboardType.Phone
                )

                Spacer(modifier = Modifier.height(12.dp))

                // EDAD
                CustomTextField(
                    value = edad,
                    onValueChange = { edad = it },
                    label = "Edad",
                    keyboardType = KeyboardType.Number
                )

                Spacer(modifier = Modifier.height(32.dp))

                // BOTÓN REGISTRARSE
                Button(
                    enabled = !ui.loading && email.isNotBlank() && pass.isNotBlank(),
                    onClick = {
                        if (nombre.isBlank() || email.isBlank() || pass.isBlank() || telefono.isBlank() || edad.isBlank()) {
                            Toast.makeText(context, "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show()
                        } else {

                            vm.register(email, pass)
                            val edadInt = edad.toIntOrNull() ?: 0

                            // Guardamos en tu repositorio (Igual a como lo tenías en tu lógica comentada)
                            Repo.usuarioActual = Usuario(
                                nombre = nombre,
                                correo = email,
                                contrasena = pass,
                                telefono = telefono,
                                edad = edadInt
                            )

                            Toast.makeText(context, "¡Cuenta creada con éxito!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEDE2FF))
                ) {
                    Text(
                        text = "Registrarse",
                        color = Color(0xFF385A79),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // BOTÓN CANCELAR / REGRESAR
                Button(
                    onClick = { onBackToLogin() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B5E8C))
                ) {
                    Text(
                        text = "Cancelar",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

// --------------------------------------------------------
// COMPONENTE AUXILIAR PARA RECICLAR EL ESTILO DE LOS TEXTFIELDS
// --------------------------------------------------------
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color.White) },
        singleLine = true,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
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
}