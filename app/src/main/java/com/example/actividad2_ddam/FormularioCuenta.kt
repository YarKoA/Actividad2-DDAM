package com.example.actividad2_ddam

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun FormularioCuentaDialog(
    titulo: String,
    usuarioInicial: Usuario? = null,
    onDismiss: () -> Unit,
    onGuardar: (Usuario) -> Unit
) {
    var nombre by remember { mutableStateOf(usuarioInicial?.nombre ?: "") }
    var correo by remember { mutableStateOf(usuarioInicial?.correo ?: "") }
    var contrasena by remember { mutableStateOf(usuarioInicial?.contrasena ?: "") }
    var confirmarContrasena by remember { mutableStateOf(usuarioInicial?.contrasena ?: "") }
    var telefono by remember { mutableStateOf(usuarioInicial?.telefono ?: "") }
    var edad by remember { mutableStateOf(usuarioInicial?.edad?.toString() ?: "") }

    var errorNombre by remember { mutableStateOf<String?>(null) }
    var errorCorreo by remember { mutableStateOf<String?>(null) }
    var errorContrasena by remember { mutableStateOf<String?>(null) }
    var errorConfirmar by remember { mutableStateOf<String?>(null) }
    var errorTelefono by remember { mutableStateOf<String?>(null) }
    var errorEdad by remember { mutableStateOf<String?>(null) }

    fun validar(): Boolean {
        var esValido = true

        if (nombre.trim().isEmpty()) {
            errorNombre = "El nombre es obligatorio y no debe ser solo espacios"
            esValido = false
        } else {
            errorNombre = null
        }

        if (correo.trim().isEmpty()) {
            errorCorreo = "El correo es obligatorio"
            esValido = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo.trim()).matches()) {
            errorCorreo = "Formato de correo inválido"
            esValido = false
        } else {
            errorCorreo = null
        }

        if (contrasena.isEmpty()) {
            errorContrasena = "La contraseña es obligatoria"
            esValido = false
        } else if (contrasena.length < 8) {
            errorContrasena = "La contraseña debe tener mínimo 8 caracteres"
            esValido = false
        } else if (!contrasena.any { it.isLetter() } || !contrasena.any { it.isDigit() }) {
            errorContrasena = "La contraseña debe combinar letras y números"
            esValido = false
        } else {
            errorContrasena = null
        }

        if (confirmarContrasena.isEmpty()) {
            errorConfirmar = "Debe confirmar la contraseña"
            esValido = false
        } else if (confirmarContrasena != contrasena) {
            errorConfirmar = "Las contraseñas no coinciden"
            esValido = false
        } else {
            errorConfirmar = null
        }

        if (telefono.trim().isEmpty()) {
            errorTelefono = "El teléfono es obligatorio"
            esValido = false
        } else if (!telefono.all { it.isDigit() }) {
            errorTelefono = "El teléfono solo debe contener números"
            esValido = false
        } else if (telefono.trim().length != 10) {
            errorTelefono = "El teléfono debe tener 10 dígitos"
            esValido = false
        } else {
            errorTelefono = null
        }

        val edadNum = edad.toIntOrNull()
        if (edad.trim().isEmpty()) {
            errorEdad = "La edad es obligatoria"
            esValido = false
        } else if (edadNum == null) {
            errorEdad = "La edad debe ser un número"
            esValido = false
        } else if (edadNum !in 0..100) {
            errorEdad = "La edad debe estar entre 0 y 100 años"
            esValido = false
        } else {
            errorEdad = null
        }

        return esValido
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF1EFFE)),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFF2C3E6B), Color(0xFF4B6B94))
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = titulo,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CampoTextoEspecial(
                        valor = nombre,
                        onValorCambio = { nombre = it },
                        etiqueta = "Nombre completo",
                        error = errorNombre
                    )

                    CampoTextoEspecial(
                        valor = correo,
                        onValorCambio = { correo = it },
                        etiqueta = "Correo electrónico",
                        error = errorCorreo,
                        tipoTeclado = KeyboardType.Email
                    )

                    CampoTextoEspecial(
                        valor = contrasena,
                        onValorCambio = { contrasena = it },
                        etiqueta = "Contraseña",
                        error = errorContrasena,
                        esPassword = true
                    )

                    CampoTextoEspecial(
                        valor = confirmarContrasena,
                        onValorCambio = { confirmarContrasena = it },
                        etiqueta = "Confirmar contraseña",
                        error = errorConfirmar,
                        esPassword = true
                    )

                    CampoTextoEspecial(
                        valor = telefono,
                        onValorCambio = { if (it.all { char -> char.isDigit() }) telefono = it },
                        etiqueta = "Teléfono (10 dígitos)",
                        error = errorTelefono,
                        tipoTeclado = KeyboardType.Number
                    )

                    CampoTextoEspecial(
                        valor = edad,
                        onValorCambio = { if (it.all { char -> char.isDigit() }) edad = it },
                        etiqueta = "Edad (0 a 100 años)",
                        error = errorEdad,
                        tipoTeclado = KeyboardType.Number
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF2C3E6B))
                    ) {
                        Text("Cancelar", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            if (validar()) {
                                val usuarioNuevo = Usuario(
                                    nombre = nombre.trim(),
                                    correo = correo.trim(),
                                    contrasena = contrasena,
                                    telefono = telefono.trim(),
                                    edad = edad.toInt()
                                )
                                onGuardar(usuarioNuevo)
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C3E6B))
                    ) {
                        Text("Guardar", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun CampoTextoEspecial(
    valor: String,
    onValorCambio: (String) -> Unit,
    etiqueta: String,
    error: String? = null,
    esPassword: Boolean = false,
    tipoTeclado: KeyboardType = KeyboardType.Text
) {
    Column {
        OutlinedTextField(
            value = valor,
            onValueChange = onValorCambio,
            label = { Text(etiqueta) },
            isError = error != null,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = if (esPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = tipoTeclado),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color(0xFF1E293B),
                unfocusedTextColor = Color(0xFF1E293B),
                focusedBorderColor = Color(0xFF2C3E6B),
                unfocusedBorderColor = Color(0xFF7A9BBF),
                focusedLabelColor = Color(0xFF2C3E6B),
                unfocusedLabelColor = Color(0xFF385A79),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                errorContainerColor = Color.White,
                errorBorderColor = Color(0xFFC62828),
                errorLabelColor = Color(0xFFC62828)
            ),
            shape = RoundedCornerShape(14.dp)
        )
        if (error != null) {
            Text(
                text = error,
                color = Color(0xFFC62828),
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = 6.dp, top = 2.dp)
            )
        }
    }
}
