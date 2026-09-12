package com.example.actividad2_ddam.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import com.example.actividad2_ddam.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {

    // Animación del logo
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            R.raw.logo_animation
        )
    )

    val logoAnimationState =
        animateLottieCompositionAsState(
            composition = composition,
            iterations = 1
        )

    // Tiempo indicado en el documento:
    // 2145 ms antes de pasar al Login
    LaunchedEffect(Unit) {

        delay(2145)

        onSplashFinished()
    }

    val fondo = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF2C3E6B),
            Color(0xFF4B6B94),
            Color(0xFF8BB5CE)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(fondo),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment =
                Alignment.CenterHorizontally,

            verticalArrangement =
                Arrangement.Center
        ) {

            if (composition != null) {

                LottieAnimation(
                    composition = composition,
                    progress = {
                        logoAnimationState.progress
                    },
                    modifier = Modifier.size(220.dp)
                )

            } else {

                Image(
                    painter = painterResource(
                        id = R.drawable.logo_tareum
                    ),
                    contentDescription = "Logo TAREUM",
                    modifier = Modifier.size(180.dp)
                )
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = "TAREUM",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Gestión inteligente de actividades",
                color = Color.White.copy(
                    alpha = 0.85f
                ),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}