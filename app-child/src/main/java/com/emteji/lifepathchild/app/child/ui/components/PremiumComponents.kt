package com.emteji.lifepathchild.app.child.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.emteji.lifepathchild.app.child.ui.theme.*

/**
 * LAYER 1: ENVIRONMENT
 * Illustrated background that changes with time/state.
 */
@Composable
fun IrokoWorldBackground(
    isNight: Boolean = false,
    content: @Composable BoxScope.() -> Unit
) {
    val skyGradient = if (isNight) NightSkyGradient else DaySkyGradient
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(skyGradient)
    ) {
        // Background Illustration (Hills/Trees via Canvas)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            
            // Distant Hills
            val hillPath = Path().apply {
                moveTo(0f, height * 0.7f)
                cubicTo(
                    width * 0.3f, height * 0.65f,
                    width * 0.7f, height * 0.75f,
                    width, height * 0.68f
                )
                lineTo(width, height)
                lineTo(0f, height)
                close()
            }
            drawPath(
                path = hillPath,
                color = if (isNight) Color(0xFF1A237E).copy(alpha = 0.5f) else Color(0xFFC8E6C9)
            )
            
            // Foreground Hills
            val forePath = Path().apply {
                moveTo(0f, height * 0.8f)
                cubicTo(
                    width * 0.4f, height * 0.78f,
                    width * 0.8f, height * 0.85f,
                    width, height * 0.82f
                )
                lineTo(width, height)
                lineTo(0f, height)
                close()
            }
            drawPath(
                path = forePath,
                color = if (isNight) Color(0xFF0D47A1).copy(alpha = 0.5f) else Color(0xFFAED581)
            )
        }
        
        content()
    }
}

/**
 * LAYER 2: COMPANION AI
 * Visual representation of the Voice AI.
 * Pulses when speaking or listening.
 */
@Composable
fun AIAvatar(
    isSpeaking: Boolean,
    isListening: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "AI Pulse")
    
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isSpeaking || isListening) 1.2f else 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Scale"
    )
    
    val ringAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Alpha"
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Outer Ring (Pulsing)
        if (isSpeaking || isListening) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .scale(pulseScale * 1.2f)
                    .clip(CircleShape)
                    .background(IrokoGold.copy(alpha = ringAlpha))
            )
        }
        
        // Inner Core
        Box(
            modifier = Modifier
                .size(80.dp)
                .scale(pulseScale)
                .clip(CircleShape)
                .background(IrokoGold)
                .border(4.dp, IrokoWhite, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            // Face / Symbol
            Canvas(modifier = Modifier.size(40.dp)) {
                // Simple friendly geometric face
                val w = size.width
                val h = size.height
                
                // Eyes
                drawCircle(color = IrokoBrown, radius = 4.dp.toPx(), center = Offset(w * 0.3f, h * 0.4f))
                drawCircle(color = IrokoBrown, radius = 4.dp.toPx(), center = Offset(w * 0.7f, h * 0.4f))
                
                // Smile
                drawPath(
                    path = Path().apply {
                        moveTo(w * 0.3f, h * 0.65f)
                        quadraticBezierTo(w * 0.5f, h * 0.8f, w * 0.7f, h * 0.65f)
                    },
                    color = IrokoBrown,
                    style = Stroke(width = 3.dp.toPx())
                )
            }
        }
    }
}
