package com.example.actividad2_ddam.ui.screens

import android.R.attr.enabled
import android.widget.Toast
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.actividad2_ddam.R
import com.example.actividad2_ddam.auth.presentation.login.LoginViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.example.actividad2_ddam.ui.theme.scaledSp
import com.example.actividad2_ddam.ui.theme.scaledWeight

@Composable
fun LoginScreen(
    onLoggedIn: () -> Unit,
    onGoToRegister: () -> Unit,
    vm: LoginViewModel = hiltViewModel()
) {
    val ui by vm.ui.collectAsState()

    val context = LocalContext.current
    val sharedPref = remember { context.getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE) }
    var email by rememberSaveable { mutableStateOf(sharedPref.getString("last_email", "") ?: "") }
    var pass by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val lastEmail = sharedPref.getString("last_email", "") ?: ""
        if (lastEmail.isNotBlank()) {
            email = lastEmail
        }
    }


    LaunchedEffect(vm) {
        vm.event.collectLatest { event ->
            when(event){
                LoginViewModel.Event.Success -> onLoggedIn()
            }
        }
    }

    
    var isLoading by remember { mutableStateOf(false) }

    // Estados para las animaciones
    var botonLoginPresionado by remember { mutableStateOf(false) }
    var botonCrearPresionado by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    // Animaciones
    val escalaLogin by animateFloatAsState(
        targetValue = if (botonLoginPresionado) 0.95f else 1f,
        animationSpec = tween(250), label = "login"
    )

    val escalaCrear by animateFloatAsState(
        targetValue = if (botonCrearPresionado) 0.95f else 1f,
        animationSpec = tween(250), label = "crear"
    )

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
                    modifier = Modifier
                        .size(130.dp)
                        .graphicsLayer { alpha = 0.99f }
                        .drawWithCache {
                            val gradient = Brush.linearGradient(
                                colors = listOf(Color(0xFF8BB5CE), Color(0xFF2C3E6B)),
                                start = Offset(0f, 0f),
                                end = Offset(size.width, size.height * 0.75f)
                            )
                            onDrawWithContent {
                                drawContent()
                                drawRect(
                                    brush = gradient,
                                    size = Size(size.width, size.height * 0.75f),
                                    blendMode = BlendMode.SrcAtop
                                )
                            }
                        }
                )

                Spacer(modifier = Modifier.height(20.dp))

                // CORREO
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo electrónico", color = Color.White) },
                    enabled = !ui.loading,
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

                // CONTRASEÑA
                OutlinedTextField(
                    value = pass,
                    onValueChange = { pass = it },
                    label = { Text("Contraseña", color = Color.White) },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    enabled = !ui.loading,
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

                // INICIAR SESIÓN
                Button(
                    enabled = !ui.loading && email.isNotBlank() && pass.isNotBlank(),
                    onClick = {
                        sharedPref.edit().putString("last_email", email.trim()).apply()
                        vm.signIn(email, pass)
                        scope.launch {
                            botonLoginPresionado = true
                            delay(250)
                            botonLoginPresionado = false
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .scale(escalaLogin),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEDE2FF))
                ) {
                    Text(
                        text = "Iniciar Sesión",
                        color = Color(0xFF385A79),
                        fontSize = 15.sp.scaledSp,
                        fontWeight = FontWeight.Bold.scaledWeight
                    )
                }

                ui.error?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // CREAR CUENTA
                Button(
                    enabled =  !ui.loading,
                    onClick = {
                        scope.launch {
                            botonCrearPresionado = true
                            delay(250)
                            botonCrearPresionado = false


                            onGoToRegister()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .scale(escalaCrear),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B5E8C))
                ) {
                    Text(
                        text = "Crear Cuenta",
                        color = Color.White,
                        fontSize = 15.sp.scaledSp,
                        fontWeight = FontWeight.Medium.scaledWeight
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // GOOGLE
                // GOOGLE
                Button(
                    onClick = {
                        // Reemplazamos el Toast por la llamada a tu ViewModel
                        vm.signInWithGoogle(context)
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
                            fontSize = 14.sp.scaledSp,
                            fontWeight = FontWeight.Medium.scaledWeight
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "G",
                            color = Color(0xFF4285F4),
                            fontSize = 22.sp.scaledSp,
                            fontWeight = FontWeight.Bold.scaledWeight
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "*Permite conectar tu calendario con la app",
                    fontSize = 11.sp.scaledSp,
                    color = Color.White.copy(alpha = 0.90f)
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        androidx.compose.animation.AnimatedVisibility(
            visible = ui.loading,
            enter = androidx.compose.animation.fadeIn(),
            exit = androidx.compose.animation.fadeOut()
        ) {
            CustomLoadingOverlay()
        }
    }
}

@Composable
fun CustomLoadingOverlay(message: String = "Iniciando sesión...") {
    val transition = rememberInfiniteTransition(label = "spinner_transition")
    val rotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "spinner_rotation"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E2841).copy(alpha = 0.85f))
            // Consume all pointer events so the user can't interact with the background
            .pointerInput(Unit) {},
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.custom_spinner),
                contentDescription = "Cargando",
                modifier = Modifier
                    .size(64.dp)
                    .graphicsLayer { rotationZ = rotation }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                color = Color.White,
                fontSize = 16.sp.scaledSp,
                fontWeight = FontWeight.Medium.scaledWeight
            )
        }
    }
}

