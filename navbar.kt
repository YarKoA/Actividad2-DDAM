package com.example.actividad2_ddam.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.actividad2_ddam.model.Repo
import com.example.actividad2_ddam.navigation.Routes
import com.example.actividad2_ddam.ui.theme.scaledSp
import com.example.actividad2_ddam.ui.theme.scaledWeight

@Composable
fun BottomNavBar(
    navController: NavController,
    currentScreen: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            color = if (Repo.modoOscuro) Color(0xFF202020) else Color(0xFFF1EFFE),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            shadowElevation = 12.dp
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Box(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .width(130.dp)
                        .height(4.dp)
                        .background(
                            if (Repo.modoOscuro) Color.Gray else Color.Black,
                            CircleShape
                        )
                )
            }
        }
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, bottom = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            NavItemButton(
                icon = Icons.Default.Home,
                label = "HOME",
                isActive = currentScreen == Routes.EVENT_LIST,
                onClick = {
                    if (currentScreen != Routes.EVENT_LIST) {
                        navController.navigate(Routes.EVENT_LIST) {
                            popUpTo(Routes.EVENT_LIST) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
            
            NavItemButton(
                icon = Icons.Default.DateRange,
                label = "CALENDARIO",
                isActive = currentScreen == Routes.CALENDAR,
                onClick = {
                    if (currentScreen != Routes.CALENDAR) {
                        navController.navigate(Routes.CALENDAR) {
                            popUpTo(Routes.EVENT_LIST) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
            
            NavItemButton(
                icon = Icons.Default.Settings,
                label = "AJUSTES",
                isActive = currentScreen == Routes.SETTINGS,
                onClick = {
                    if (currentScreen != Routes.SETTINGS) {
                        navController.navigate(Routes.SETTINGS) {
                            popUpTo(Routes.EVENT_LIST) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun NavItemButton(
    icon: ImageVector,
    label: String,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .offset(y = (-30).dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
    ) {
        Surface(
            shape = CircleShape,
            color = if (isActive) Color(0xFFC4E4F4) else Color(0xFF384F66),
            border = BorderStroke(2.dp, if (isActive) Color.White else Color.Transparent),
            modifier = Modifier.size(64.dp),
            shadowElevation = 4.dp
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    modifier = Modifier.size(if (isActive) 24.dp else 28.dp),
                    tint = if (isActive) Color(0xFF384F66) else Color.White
                )
                if (isActive) {
                    Text(
                        text = label,
                        fontSize = 10.sp.scaledSp,
                        fontWeight = FontWeight.Bold.scaledWeight,
                        color = Color(0xFF384F66)
                    )
                }
            }
        }
    }
}
