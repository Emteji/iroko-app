package com.emteji.lifepathchild.app.parent.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// --- PRIMARY EARTH ---
// Deep iroko brown. Used for authority areas.
val IrokoBrown = Color(0xFF52361E)
// Dark clay brown. Used for structure.
val IrokoClay = Color(0xFF3E2816)
val IrokoDarkSoil = Color(0xFF2D1B0E) // Even darker for the bottom section

// --- ACCENT GOLD ---
// Soft warm gold. Used sparingly. Only for highlights.
val IrokoGold = Color(0xFFD9A53D)
val IrokoBurntGold = Color(0xFFC68E17) // Darker gold for gradients
val IrokoLightGold = Color(0xFFFFE082) // For text highlights on dark backgrounds

// --- FOREST GREEN ---
// Deep forest green. Growth, health, approval.
val IrokoForestGreen = Color(0xFF2E7D32)
// Soft leaf green. Backgrounds or success states.
val IrokoLeafGreen = Color(0xFF4CAF50)

// --- NEUTRALS & PAPER ---
// Warm off-white. The paper/canvas.
val IrokoWhite = Color(0xFFFAFAFA)
// Cream/Parchment.
val IrokoCream = Color(0xFFFDFBF7)
// Aged Paper (Slightly darker, used for cards on top of cream)
val IrokoPaper = Color(0xFFF5F1E6) 
// Stone grey. Secondary text.
val IrokoStone = Color(0xFF9E9E9E)

// --- TEXT ---
val AncientWood = Color(0xFF3E2723) // Almost black brown
val TextPrimary = Color(0xFF212121)
val TextSecondary = Color(0xFF757575)
val TextOnDark = Color(0xFFF5F5F5)

// --- FUNCTIONAL ---
val SuccessGreen = Color(0xFF2E7D32)
val WarningOrange = Color(0xFFEF6C00)
val ErrorRed = Color(0xFFC62828)

// --- GRADIENTS ---
val ParchmentGradient = Brush.verticalGradient(
    colors = listOf(IrokoCream, IrokoPaper)
)

val CardGradient = Brush.linearGradient(
    colors = listOf(Color(0xFFFFFFFF), Color(0xFFFDFBF7))
)

val PrimaryGradient = Brush.horizontalGradient(
    colors = listOf(IrokoBrown, IrokoClay)
)

val GoldGradient = Brush.horizontalGradient(
    colors = listOf(IrokoGold, IrokoBurntGold)
)
