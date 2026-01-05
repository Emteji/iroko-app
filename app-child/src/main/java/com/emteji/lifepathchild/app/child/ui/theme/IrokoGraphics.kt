package com.emteji.lifepathchild.app.child.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Iroko Wood Theme Colors
val WoodDark = Color(0xFF5D4037)
val WoodMedium = Color(0xFF8D6E63)
val WoodLight = Color(0xFFD7CCC8)
val WoodGold = Color(0xFFFFB74D)

// Gradient for Wood Texture
val WoodGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFF8D6E63), Color(0xFF6D4C41))
)

val SkyGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFF4FC3F7), Color(0xFFE1F5FE))
)

// Modifier to create a "Carved" look (Inner Shadow / Bevel)
fun Modifier.carvedWood(
    cornerRadius: Dp = 16.dp,
    elevation: Dp = 4.dp
): Modifier = this
    .background(
        brush = Brush.linearGradient(
            colors = listOf(Color(0xFF8D6E63), Color(0xFF795548))
        ),
        shape = RoundedCornerShape(cornerRadius)
    )
    .border(
        width = 2.dp,
        brush = Brush.verticalGradient(
            colors = listOf(Color(0xFFA1887F), Color(0xFF4E342E)) // Highlight top, Shadow bottom
        ),
        shape = RoundedCornerShape(cornerRadius)
    )

// Custom Painter for a simple "Wood Grain" effect
fun Modifier.woodGrainBackground(): Modifier = this.drawBehind {
    val path = Path()
    val width = size.width
    val height = size.height
    
    // Draw base background
    drawRect(color = Color(0xFFFFF8E1)) // Light wood base

    // Draw random grain lines
    val grainColor = Color(0xFFD7CCC8).copy(alpha = 0.5f)
    
    // Simple simulated grain
    for (i in 0..10) {
        val y = height * (i / 10f)
        drawLine(
            color = grainColor,
            start = Offset(0f, y),
            end = Offset(width, y + (if (i % 2 == 0) 50f else -50f)),
            strokeWidth = 2.dp.toPx()
        )
    }
}

@Composable
fun IrokoCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .padding(4.dp)
            .carvedWood()
            .padding(16.dp)
    ) {
        content()
    }
}
