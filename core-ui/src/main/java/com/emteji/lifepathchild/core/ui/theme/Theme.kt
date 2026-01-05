package com.emteji.lifepathchild.core.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// IROKO DESIGN: "No loud colors. No gradients abuse."
// We prioritize our custom palette over Dynamic Color.

private val IrokoLightScheme = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,
    secondary = Secondary,
    onSecondary = OnSecondary,
    secondaryContainer = SecondaryContainer,
    onSecondaryContainer = OnSecondaryContainer,
    tertiary = Tertiary,
    onTertiary = OnTertiary,
    background = Background,
    onBackground = OnBackground,
    surface = Surface,
    onSurface = OnSurface,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = OnSurfaceVariant,
    error = Error,
    onError = OnError
)

// Dark Theme Adjustment for Accessibility & Comfort
// Background becomes dark charcoal, text becomes white/gray.
private val IrokoDarkScheme = darkColorScheme(
    primary = EarthGold, // Gold is more visible on dark than Deep Brown
    onPrimary = Color.Black,
    primaryContainer = IrokoDeepBrown,
    onPrimaryContainer = EarthGold,
    secondary = ForestCanopyGreen,
    onSecondary = PureWhite,
    background = CharcoalBlack,
    onBackground = PureWhite,
    surface = Color(0xFF2C2C2C), // Slightly lighter than background
    onSurface = PureWhite,
    surfaceVariant = Color(0xFF404040),
    onSurfaceVariant = SoftAshGray,
    error = QuietRed,
    onError = PureWhite
)

@Composable
fun LifePathChildTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // IROKO RULE: "This is legacy design... No trendy design."
    // Dynamic color (Material You) is disabled to enforce brand authority.
    dynamicColor: Boolean = false, 
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        // dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        //     val context = LocalContext.current
        //     if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        // }
        darkTheme -> IrokoDarkScheme
        else -> IrokoLightScheme
    }
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb() // "Calm" status bar matches bg
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
