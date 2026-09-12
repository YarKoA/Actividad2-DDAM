package com.example.actividad2_ddam.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.Font
import com.example.actividad2_ddam.R

val MontserratAlternates = FontFamily(
    Font(R.font.montserrat_alternates_regular, FontWeight.Normal),
    Font(R.font.montserrat_alternates_medium, FontWeight.Medium),
    Font(R.font.montserrat_alternates_bold, FontWeight.Bold)
)

fun getAppTypography(letraGrande: Boolean, grosorGrueso: Boolean): Typography {
    val scale = if (letraGrande) 1.2f else 1f

    fun getWeight(base: FontWeight): FontWeight {
        if (!grosorGrueso) return base
        return when (base) {
            FontWeight.Normal -> FontWeight.Medium
            FontWeight.Medium -> FontWeight.Bold
            else -> FontWeight.Bold
        }
    }

    return Typography(
        displayLarge = TextStyle(fontFamily = MontserratAlternates, fontWeight = getWeight(FontWeight.Normal), fontSize = 57.sp * scale),
        displayMedium = TextStyle(fontFamily = MontserratAlternates, fontWeight = getWeight(FontWeight.Normal), fontSize = 45.sp * scale),
        displaySmall = TextStyle(fontFamily = MontserratAlternates, fontWeight = getWeight(FontWeight.Normal), fontSize = 36.sp * scale),
        headlineLarge = TextStyle(fontFamily = MontserratAlternates, fontWeight = getWeight(FontWeight.Normal), fontSize = 32.sp * scale),
        headlineMedium = TextStyle(fontFamily = MontserratAlternates, fontWeight = getWeight(FontWeight.Normal), fontSize = 28.sp * scale),
        headlineSmall = TextStyle(fontFamily = MontserratAlternates, fontWeight = getWeight(FontWeight.Normal), fontSize = 24.sp * scale),
        titleLarge = TextStyle(fontFamily = MontserratAlternates, fontWeight = getWeight(FontWeight.Medium), fontSize = 22.sp * scale),
        titleMedium = TextStyle(fontFamily = MontserratAlternates, fontWeight = getWeight(FontWeight.Medium), fontSize = 16.sp * scale),
        titleSmall = TextStyle(fontFamily = MontserratAlternates, fontWeight = getWeight(FontWeight.Medium), fontSize = 14.sp * scale),
        bodyLarge = TextStyle(fontFamily = MontserratAlternates, fontWeight = getWeight(FontWeight.Normal), fontSize = 16.sp * scale),
        bodyMedium = TextStyle(fontFamily = MontserratAlternates, fontWeight = getWeight(FontWeight.Normal), fontSize = 14.sp * scale),
        bodySmall = TextStyle(fontFamily = MontserratAlternates, fontWeight = getWeight(FontWeight.Normal), fontSize = 12.sp * scale),
        labelLarge = TextStyle(fontFamily = MontserratAlternates, fontWeight = getWeight(FontWeight.Medium), fontSize = 14.sp * scale),
        labelMedium = TextStyle(fontFamily = MontserratAlternates, fontWeight = getWeight(FontWeight.Medium), fontSize = 12.sp * scale),
        labelSmall = TextStyle(fontFamily = MontserratAlternates, fontWeight = getWeight(FontWeight.Medium), fontSize = 11.sp * scale)
    )
}

val Typography = getAppTypography(false, false)
