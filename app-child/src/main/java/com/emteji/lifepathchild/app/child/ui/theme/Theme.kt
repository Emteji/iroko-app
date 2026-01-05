package com.emteji.lifepathchild.app.child.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

private val ChildColorScheme = lightColorScheme(
    primary = WarmOrange,
    secondary = SoftYellow,
    tertiary = SuccessGreen,
    background = CalmWhite,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = TextDark,
    onTertiary = TextDark,
    onBackground = TextDark,
    onSurface = TextDark,
)

val ChildShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(24.dp), // Friendlier, rounder
    large = RoundedCornerShape(32.dp)
)

@Composable
fun LifePathChildTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Force consistent child branding
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        else -> ChildColorScheme // Children app shouldn't change too much with system dark mode for consistency
    }

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = ChildShapes,
        content = content
    )
}
