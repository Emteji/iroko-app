package com.emteji.lifepathchild.app.child.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// IROKO PREMIUM PALETTE - CHILD
// Concept: "The Village Garden"

val IrokoWhite = Color(0xFFFAFAFA)
val IrokoCream = Color(0xFFFDFBF7) // Warm paper/parchment base
val IrokoBrown = Color(0xFF52361E) // Deep Earth / Authority
val IrokoGold = Color(0xFFD9A53D) // Value / Reward
val IrokoBurntGold = Color(0xFFC68E17)
val IrokoForestGreen = Color(0xFF2E7D32) // Growth / Success
val IrokoDarkSoil = Color(0xFF2D1B0E)
val IrokoStone = Color(0xFF757575) // Neutral Text
val IrokoSkyBlue = Color(0xFF81D4FA) // Day Environment
val IrokoNightBlue = Color(0xFF1A237E) // Night Environment

// Legacy / Helper colors
val WarmOrange = Color(0xFFFF7043)
val SoftYellow = Color(0xFFFFEE58)
val CalmWhite = IrokoCream
val SuccessGreen = IrokoForestGreen
val TextDark = IrokoBrown
val TextLight = IrokoStone

// Gradients
val DaySkyGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFFE1F5FE), Color(0xFFFFFDE7))
)

val NightSkyGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFF1A237E), Color(0xFF311B92))
)

val ForestGradient = Brush.verticalGradient(
    colors = listOf(IrokoForestGreen, Color(0xFF1B5E20))
)

val GoldGradient = Brush.linearGradient(
    colors = listOf(IrokoGold, IrokoBurntGold)
)
