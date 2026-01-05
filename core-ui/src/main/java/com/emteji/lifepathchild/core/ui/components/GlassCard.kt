package com.emteji.lifepathchild.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    backgroundColor: Color = Color.White.copy(alpha = 0.7f),
    borderColor: Color = Color.White.copy(alpha = 0.5f),
    borderWidth: Dp = 1.dp,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(backgroundColor)
            .border(borderWidth, borderColor, shape)
    ) {
        content()
    }
}

@Composable
fun IrokoBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    // Simulates the parchment/textured background using a gradient
    val brush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFF9F6F2), // Light Parchment Top
            Color(0xFFF0EBE0), // Mid Parchment
            Color(0xFFE6DFD5)  // Bottom Textured feel
        )
    )
    
    Box(
        modifier = modifier
            .background(brush)
    ) {
        content()
    }
}
