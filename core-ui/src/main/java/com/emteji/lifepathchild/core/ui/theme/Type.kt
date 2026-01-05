package com.emteji.lifepathchild.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ==================================================
// IROKO TYPOGRAPHY SYSTEM
// Respectful. Calm. Clear. Serious.
// ==================================================

// PRIMARY FONT FAMILY (Headings) - Serif
val IrokoSerif = FontFamily.Serif

// SECONDARY FONT FAMILY (Body) - Sans Serif
val IrokoSans = FontFamily.SansSerif

val Typography = Typography(
    // H1. Screen Title
    displayLarge = TextStyle(
        fontFamily = IrokoSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    displayMedium = TextStyle(
        fontFamily = IrokoSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 36.sp
    ),
    // H2. Section Header
    headlineMedium = TextStyle(
        fontFamily = IrokoSerif,
        fontWeight = FontWeight.Normal, // "Calm presence"
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    // H3. Card Title
    titleMedium = TextStyle(
        fontFamily = IrokoSans,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    
    // Body Large
    bodyLarge = TextStyle(
        fontFamily = IrokoSans,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    // Body Regular
    bodyMedium = TextStyle(
        fontFamily = IrokoSans,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    
    // Button Text
    labelLarge = TextStyle(
        fontFamily = IrokoSans,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 1.sp // "Wide letter spacing"
    ),
    
    // Caption
    labelMedium = TextStyle(
        fontFamily = IrokoSans,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = IrokoSans,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)
