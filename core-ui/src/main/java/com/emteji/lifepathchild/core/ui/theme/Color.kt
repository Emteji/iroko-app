package com.emteji.lifepathchild.core.ui.theme

import androidx.compose.ui.graphics.Color

// ==================================================
// IROKO COLOR SYSTEM
// Legacy Design. Calm strength. Cultural depth.
// ==================================================

// PRIMARY
val IrokoDeepBrown = Color(0xFF4E3826) // #4A3624 to #5A402A range - Midpoint
val ForestCanopyGreen = Color(0xFF2E4A35) // Deep muted green

// SECONDARY
val EarthGold = Color(0xFFC5A065) // Value. Reward. Wisdom.
val ClayBeige = Color(0xFFF5F2EE) // Warmth. Neutral space. (Slightly lighter than pure beige for UI)

// NEUTRAL
val PureWhite = Color(0xFFFFFFFF)
val CharcoalBlack = Color(0xFF1F1F1F) // Serious text.
val SoftAshGray = Color(0xFFE0E0E0) // Structure.

// STATUS
val CalmSuccessGreen = Color(0xFF558B6E) // Completed tasks.
val WarningAmber = Color(0xFFFFB300) // Attention.
val QuietRed = Color(0xFFC62828) // Safety alerts.

// MATERIAL MAPPING
val Primary = IrokoDeepBrown
val OnPrimary = PureWhite
val PrimaryContainer = ClayBeige
val OnPrimaryContainer = IrokoDeepBrown

val Secondary = ForestCanopyGreen
val OnSecondary = PureWhite
val SecondaryContainer = ForestCanopyGreen.copy(alpha = 0.2f)
val OnSecondaryContainer = Color(0xFF0F2012)

val Tertiary = EarthGold
val OnTertiary = Color(0xFF2A1C00)

val Background = PureWhite
val OnBackground = CharcoalBlack

val Surface = PureWhite
val OnSurface = CharcoalBlack
val SurfaceVariant = ClayBeige
val OnSurfaceVariant = CharcoalBlack

val Error = QuietRed
val OnError = PureWhite

// Legacy placeholders to prevent breakage (mapped to new system)
val IrokoGreen = ForestCanopyGreen
val IrokoGold = EarthGold
val IrokoEarth = IrokoDeepBrown
val IrokoLightGreen = SecondaryContainer
val IrokoCream = ClayBeige
val Purple80 = IrokoDeepBrown
val PurpleGrey80 = SoftAshGray
val Pink80 = EarthGold
val Purple40 = IrokoDeepBrown
val PurpleGrey40 = CharcoalBlack
val Pink40 = EarthGold
