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

@Composable
fun LoginScreen(
    onLoggedIn: () -> Unit,
    onGoToRegister: () -> Unit,
    vm: LoginViewModel = hiltViewModel()
) {
    val ui by vm.ui.collectAsState()

    var email by rememberSaveable { mutableStateOf("") }
    var pass by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(vm) {
        vm.event.collectLatest { event ->
            when(event){
                LoginViewModel.Event.Success -> onLoggedIn()
            }
        }
    }

    val context = LocalContext.current
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
                    modifier = Modifier.size(130.dp)
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
                    if(ui.loading){
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp
                        )
                    } else{
                        Text(
                            text = "Iniciar Sesión",
                            color = Color(0xFF385A79),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
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
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // GOOGLE
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
    }
}

/*package com.example.actividad2_ddam.ui.screens

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.actividad2_ddam.FormularioCuentaDialog
import com.example.actividad2_ddam.R
import com.example.actividad2_ddam.auth.presentation.login.LoginViewModel
import com.example.actividad2_ddam.model.Repo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onLoggedIn: () -> Unit,
    vm : LoginViewModel = hiltViewModel()
) {

    val ui by vm.ui.collectAsState()

    var email by rememberSaveable() { mutableStateOf("") }
    var pass by rememberSaveable() { mutableStateOf("") }

    LaunchedEffect(vm) {
        vm.event.collectLatest { event ->
            when(event){
                LoginViewModel.Event.Success -> onLoggedIn()
            }
        }
    }

    val context = LocalContext.current


    var correoLogin by remember {
        mutableStateOf("")
    }

    var contrasenaLogin by remember {
        mutableStateOf("")
    }

    var mostrarCrearCuenta by remember {
        mutableStateOf(false)
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    // Estados para las animaciones
    var botonLoginPresionado by remember {
        mutableStateOf(false)
    }

    var botonCrearPresionado by remember {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()

    // Animación del botón Login
    val escalaLogin by animateFloatAsState(
        targetValue =
            if (botonLoginPresionado) {
                0.95f
            } else {
                1f
            },

        animationSpec = tween(250),
        label = "login"
    )

    // Animación del botón Crear Cuenta
    val escalaCrear by animateFloatAsState(
        targetValue =
            if (botonCrearPresionado) {
                0.95f
            } else {
                1f
            },

        animationSpec = tween(250),
        label = "crear"
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
                    .verticalScroll(
                        rememberScrollState()
                    ),

                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                Spacer(
                    modifier = Modifier.height(24.dp)
                )


                Image(
                    painter = painterResource(
                        id = R.drawable.logo_tareum
                    ),

                    contentDescription =
                        "Logo TAREUM",

                    modifier = Modifier.size(130.dp)
                )


                Spacer(
                    modifier = Modifier.height(20.dp)
                )


                // -------------------------
                // CORREO
                // -------------------------

                OutlinedTextField(
                    value = email,

                    onValueChange = {
                        email = it
                    },

                    label = {
                        Text(
                            "Correo electrónico",
                            color = Color.White
                        )
                    },

                    enabled = !ui.loading,

                    singleLine = true,

                    modifier =
                        Modifier.fillMaxWidth(),

                    shape =
                        RoundedCornerShape(12.dp),

                    colors =
                        OutlinedTextFieldDefaults.colors(

                            focusedContainerColor =
                                Color(0xFF3B5E8C)
                                    .copy(alpha = 0.5f),

                            unfocusedContainerColor =
                                Color(0xFF3B5E8C)
                                    .copy(alpha = 0.3f),

                            focusedTextColor =
                                Color.White,

                            unfocusedTextColor =
                                Color.White,

                            focusedBorderColor =
                                Color.White,

                            unfocusedBorderColor =
                                Color.White
                                    .copy(alpha = 0.7f),

                            cursorColor =
                                Color.White
                        )
                )


                Spacer(
                    modifier = Modifier.height(12.dp)
                )


                // -------------------------
                // CONTRASEÑA
                // -------------------------

                OutlinedTextField(
                    value = pass,

                    onValueChange = {
                        pass = it
                    },

                    label = {
                        Text(
                            "Contraseña",
                            color = Color.White
                        )
                    },

                    singleLine = true,

                    visualTransformation =
                        PasswordVisualTransformation(),

                    enabled = !ui.loading,

                    keyboardOptions =
                        KeyboardOptions(
                            keyboardType =
                                KeyboardType.Password
                        ),

                    modifier =
                        Modifier.fillMaxWidth(),

                    shape =
                        RoundedCornerShape(12.dp),

                    colors =
                        OutlinedTextFieldDefaults.colors(

                            focusedContainerColor =
                                Color(0xFF3B5E8C)
                                    .copy(alpha = 0.5f),

                            unfocusedContainerColor =
                                Color(0xFF3B5E8C)
                                    .copy(alpha = 0.3f),

                            focusedTextColor =
                                Color.White,

                            unfocusedTextColor =
                                Color.White,

                            focusedBorderColor =
                                Color.White,

                            unfocusedBorderColor =
                                Color.White
                                    .copy(alpha = 0.7f),

                            cursorColor =
                                Color.White
                        )
                )


                Spacer(
                    modifier = Modifier.height(20.dp)
                )


                // -------------------------
                // INICIAR SESIÓN
                // -------------------------

                Button(
                    enabled = !ui.loading && email.isNotBlank() && pass.isNotBlank(),
                    onClick = {

                        vm.signIn(email, pass)

                        scope.launch {

                            // Animación de 250 ms
                            botonLoginPresionado = true

                            delay(250)

                            botonLoginPresionado = false


                            // Lógica original
//                            if (Repo.usuarioActual == null) {
//
//                                Repo.usuarioActual =
//                                    com.example.actividad2_ddam
//                                        .model.Usuario(
//
//                                            nombre = "Usuario",
//
//                                            correo =
//                                                "usuario@ejemplo.com",
//
//                                            contrasena =
//                                                "Password123",
//
//                                            telefono =
//                                                "5551234567",
//
//                                            edad = 25
//                                        )
//                            }


//                            val u =
//                                Repo.usuarioActual!!


                            if (
                                correoLogin.trim()
                                    .isEmpty() ||
                                contrasenaLogin
                                    .isEmpty()
                            ) {

                                Toast.makeText(
                                    context,
                                    "Por favor, llena todos los campos",
                                    Toast.LENGTH_SHORT
                                ).show()

//                            } else if (
//                                correoLogin
//                                    .trim()
//                                    .equals(
//                                        u.correo.trim(),
//                                        ignoreCase = true
//                                    )
//                                &&
//                                contrasenaLogin ==
//                                u.contrasena
//                            ) {
//
//                                isLoading = true
//                                delay(3000)
//                                isLoading = false
//
//                                Toast.makeText(
//                                    context,
//                                    "¡Bienvenido, ${u.nombre}!",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//
//                                onLoggedIn()
//
//
                            } else {

                                Toast.makeText(
                                    context,
                                    "Credenciales incorrectas",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        }
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .scale(escalaLogin),

                    shape =
                        RoundedCornerShape(28.dp),

                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor =
                                Color(0xFFEDE2FF)
                        )
                ) {
                    if(ui.loading){
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp
                        )
                    } else{
                        Text(
                            text = "Iniciar Sesión",

                            color =
                                Color(0xFF385A79),

                            fontSize = 15.sp,

                            fontWeight =
                                FontWeight.Bold
                        )
                    }
                }

                ui.error?.let {
                    Text(text = it,
                        color = Color.Red,
                        modifier = Modifier.padding(top = 8.dp))
                }


                Spacer(
                    modifier = Modifier.height(16.dp)
                )


                // -------------------------
                // CREAR CUENTA
                // -------------------------

                Button(
                    onClick = {

                        scope.launch {

                            botonCrearPresionado = true

                            delay(250)

                            botonCrearPresionado = false

                            mostrarCrearCuenta = true
                        }
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .scale(escalaCrear),

                    shape =
                        RoundedCornerShape(28.dp),

                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor =
                                Color(0xFF3B5E8C)
                        )
                ) {

                    Text(
                        text = "Crear Cuenta",

                        color = Color.White,

                        fontSize = 15.sp,

                        fontWeight =
                            FontWeight.Medium
                    )
                }


                Spacer(
                    modifier = Modifier.height(16.dp)
                )


                // -------------------------
                // GOOGLE
                // -------------------------

                Button(
                    onClick = {

                        Toast.makeText(
                            context,
                            "Funcionalidad no disponible",
                            Toast.LENGTH_SHORT
                        ).show()
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),

                    shape =
                        RoundedCornerShape(28.dp),

                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor =
                                Color(0xFFEDE2FF)
                        )
                ) {

                    Row(
                        verticalAlignment =
                            Alignment.CenterVertically,

                        horizontalArrangement =
                            Arrangement.Center
                    ) {

                        Text(
                            text =
                                "Ingresar con cuenta de Google",

                            color =
                                Color(0xFF385A79),

                            fontSize = 14.sp,

                            fontWeight =
                                FontWeight.Medium
                        )


                        Spacer(
                            modifier =
                                Modifier.width(10.dp)
                        )


                        Text(
                            text = "G",

                            color =
                                Color(0xFF4285F4),

                            fontSize = 22.sp,

                            fontWeight =
                                FontWeight.Bold
                        )
                    }
                }


                Spacer(
                    modifier = Modifier.height(12.dp)
                )


                Text(
                    text =
                        "*Permite conectar tu calendario con la app",

                    fontSize = 11.sp,

                    color =
                        Color.White.copy(
                            alpha = 0.90f
                        )
                )


                Spacer(
                    modifier = Modifier.height(24.dp)
                )
            }
        }


        // -------------------------
        // CREAR CUENTA
        // -------------------------

        if (mostrarCrearCuenta) {

            FormularioCuentaDialog(

                titulo = "Crear Cuenta",

                onDismiss = {
                    mostrarCrearCuenta = false
                },

                onGuardar = { usuarioNuevo ->

                    Repo.usuarioActual =
                        usuarioNuevo


                    Toast.makeText(
                        context,
                        "¡Cuenta creada con éxito!",
                        Toast.LENGTH_SHORT
                    ).show()


                    mostrarCrearCuenta = false

                    onLoggedIn()
                }
            )
        }

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Color.Black.copy(alpha = 0.5f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                val infiniteTransition =
                    rememberInfiniteTransition(
                        label = "loading"
                    )

                val rotation by
                infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = 360f,
                    animationSpec =
                        infiniteRepeatable(
                            animation = tween(
                                1000,
                                easing =
                                    LinearEasing
                            ),
                            repeatMode =
                                RepeatMode.Restart
                        ),
                    label = "rotation"
                )

                Canvas(
                    modifier =
                        Modifier.size(80.dp)
                ) {
                    val dotCount = 12
                    val radius =
                        size.minDimension / 2f
                    val dotRadius =
                        radius * 0.12f

                    for (i in 0 until dotCount) {
                        val angle =
                            (360f / dotCount) *
                                    i + rotation

                        val rad =
                            Math.toRadians(
                                angle.toDouble()
                            )

                        val x =
                            center.x +
                                    radius * 0.75f *
                                    kotlin.math.cos(
                                        rad
                                    ).toFloat()

                        val y =
                            center.y +
                                    radius * 0.75f *
                                    kotlin.math.sin(
                                        rad
                                    ).toFloat()

                        val alpha =
                            0.3f + 0.7f *
                                    (i.toFloat() /
                                            dotCount)

                        drawCircle(
                            color = Color(
                                0xFFBBA8E8
                            ).copy(
                                alpha = alpha
                            ),
                            radius = dotRadius,
                            center =
                                androidx.compose
                                    .ui.geometry
                                    .Offset(x, y)
                        )
                    }
                }

                Spacer(
                    modifier =
                        Modifier.height(16.dp)
                )

                Text(
                    text = "Cargando...",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight =
                        FontWeight.Medium
                )
            }
        }
    }
}


 */
