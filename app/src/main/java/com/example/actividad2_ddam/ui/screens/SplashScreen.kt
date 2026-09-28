package com.example.actividad2_ddam.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.actividad2_ddam.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    var animateUp by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(1000)
        animateUp = true
        delay(800)
        onSplashFinished()
    }

    val fondo = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF33436F),
            Color(0xFF4B84A8),
            Color(0xFF8BB5CE)
        )
    )

    // Animates the logo upwards
    val bias by animateFloatAsState(
        targetValue = if (animateUp) -0.65f else 0f,
        animationSpec = tween(800, easing = FastOutSlowInEasing),
        label = "logo_anim"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(fondo),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f + bias))
            
            // Logo exacto de LoginScreen
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
            
            Spacer(modifier = Modifier.weight(1f - bias))
        }
    }
}
