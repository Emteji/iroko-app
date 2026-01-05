package com.emteji.lifepathchild.app.parent.ui.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emteji.lifepathchild.app.parent.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onAnimationFinished: () -> Unit
) {
    var startAnimation by remember { mutableStateOf(false) }
    
    // Auto-navigate after animation
    LaunchedEffect(Unit) {
        startAnimation = true
        delay(4000)
        onAnimationFinished()
    }
    
    // Safety check
    DisposableEffect(Unit) {
        onDispose { }
    }

    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1500, easing = EaseOutQuart),
        label = "Alpha"
    )
    
    val rootProgress = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 2500, delayMillis = 500, easing = EaseOutCubic),
        label = "Roots"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(IrokoCream)
    ) {
        // 1. PREMIUM BACKGROUND
        // A rich, textured parchment feel using a gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ParchmentGradient)
        )

        // 2. THE MAGNIFICENT TREE
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val centerX = w / 2
            val groundLevel = h * 0.6f 

            // --- DEEP ROOTS ANIMATION ---
            if (rootProgress.value > 0f) {
                val rootColor = IrokoBrown.copy(alpha = 0.8f)
                
                // Main Tap Root
                drawLine(
                    color = rootColor,
                    start = Offset(centerX, groundLevel),
                    end = Offset(centerX, groundLevel + (h * 0.25f * rootProgress.value)),
                    strokeWidth = 16f * rootProgress.value,
                    cap = StrokeCap.Round
                )
                
                // Complex Root System
                val rootPath = Path().apply {
                    moveTo(centerX, groundLevel)
                    cubicTo(
                        centerX - 60f, groundLevel + 120f,
                        centerX - 180f, groundLevel + 180f,
                        centerX - 240f, groundLevel + (350f * rootProgress.value)
                    )
                    moveTo(centerX, groundLevel)
                    cubicTo(
                        centerX + 60f, groundLevel + 120f,
                        centerX + 180f, groundLevel + 180f,
                        centerX + 240f, groundLevel + (350f * rootProgress.value)
                    )
                }
                drawPath(rootPath, color = rootColor, style = Stroke(width = 8f * rootProgress.value, cap = StrokeCap.Round))
            }

            // --- THE TRUNK & CANOPY ---
            // Trunk
            val trunkPath = Path().apply {
                moveTo(centerX - 50f, groundLevel)
                quadraticBezierTo(centerX - 25f, groundLevel - 200f, centerX - 80f, groundLevel - 400f)
                lineTo(centerX + 80f, groundLevel - 400f)
                quadraticBezierTo(centerX + 25f, groundLevel - 200f, centerX + 50f, groundLevel)
                close()
            }
            drawPath(trunkPath, color = IrokoBrown)

            // Lush Canopy (Layers of Green)
            drawCircle(color = IrokoForestGreen, radius = 160f, center = Offset(centerX, groundLevel - 450f))
            drawCircle(color = IrokoForestGreen.copy(alpha = 0.8f), radius = 140f, center = Offset(centerX - 120f, groundLevel - 400f))
            drawCircle(color = IrokoForestGreen.copy(alpha = 0.8f), radius = 140f, center = Offset(centerX + 120f, groundLevel - 400f))
            drawCircle(color = IrokoLeafGreen.copy(alpha = 0.6f), radius = 100f, center = Offset(centerX, groundLevel - 550f))
        }

        // 3. TEXT & BRANDING
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp, start = 40.dp, end = 40.dp)
                .alpha(alphaAnim.value),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "IROKO",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = 56.sp,
                    letterSpacing = 4.sp
                ),
                color = IrokoBrown,
                fontWeight = FontWeight.ExtraBold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            HorizontalDivider(
                modifier = Modifier.width(60.dp),
                thickness = 2.dp,
                color = IrokoGold
            )
            
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Deep Roots.\nStrong Branches.",
                style = MaterialTheme.typography.headlineSmall,
                color = IrokoBrown.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                lineHeight = 32.sp
            )
        }
    }
}
