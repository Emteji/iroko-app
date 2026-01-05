package com.emteji.lifepathchild.app.parent.ui.onboarding

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import com.emteji.lifepathchild.app.parent.ui.theme.*

// --- SCENE 1: PURPOSE (Parent & Child looking at horizon) ---
@Composable
fun BackgroundScenePurpose() {
    androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
        // 1. Sky Gradient (Dawn/Dusk)
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFFFFF8E1), Color(0xFFFFE0B2), Color(0xFFFFCC80)),
                startY = 0f,
                endY = size.height * 0.6f
            )
        )
        
        // 2. Sun (Soft glow)
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFFFFE082), Color(0xFFFFCC80).copy(alpha = 0f)),
                center = Offset(size.width * 0.7f, size.height * 0.35f),
                radius = 200f
            ),
            radius = 200f,
            center = Offset(size.width * 0.7f, size.height * 0.35f)
        )
        
        // 3. Distant City/Village Layers
        drawDistantLandscape(this, size.height * 0.55f, Color(0xFFD7CCC8))
        drawDistantLandscape(this, size.height * 0.60f, Color(0xFFBCAAA4))

        // 4. Foreground Hill (Lush)
        val hillPath = Path().apply {
            moveTo(0f, size.height)
            lineTo(0f, size.height * 0.7f)
            cubicTo(
                size.width * 0.3f, size.height * 0.65f,
                size.width * 0.7f, size.height * 0.75f,
                size.width, size.height * 0.68f
            )
            lineTo(size.width, size.height)
            close()
        }
        drawPath(hillPath, brush = Brush.verticalGradient(colors = listOf(IrokoForestGreen, Color(0xFF1B5E20))))
        
        // 5. Parent & Child Silhouette (Back view)
        drawFigures(this)
    }
}

private fun drawDistantLandscape(scope: DrawScope, horizonY: Float, color: Color) {
    val w = scope.size.width
    val path = Path().apply {
        moveTo(0f, horizonY)
        var x = 0f
        while (x < w) {
            x += 20f + (Math.random() * 30f).toFloat()
            lineTo(x, horizonY - (10f + (Math.random() * 40f).toFloat())) // Buildings/Trees
            lineTo(x + 10f, horizonY)
        }
        lineTo(w, scope.size.height)
        lineTo(0f, scope.size.height)
        close()
    }
    scope.drawPath(path, color)
}

private fun drawFigures(scope: DrawScope) {
    val centerX = scope.size.width / 2
    val standingY = scope.size.height * 0.72f
    
    val parentColor = IrokoBrown
    
    // Parent
    scope.drawOval(
        color = parentColor,
        topLeft = Offset(centerX - 60f, standingY - 180f),
        size = Size(60f, 180f)
    )
    scope.drawCircle(
        color = parentColor,
        radius = 25f,
        center = Offset(centerX - 30f, standingY - 190f)
    )
    // Shawl/Cloth (Gold)
    scope.drawArc(
        color = IrokoGold,
        startAngle = 180f,
        sweepAngle = 180f,
        useCenter = true,
        topLeft = Offset(centerX - 70f, standingY - 180f),
        size = Size(80f, 60f)
    )

    // Child
    scope.drawOval(
        color = parentColor,
        topLeft = Offset(centerX + 10f, standingY - 120f),
        size = Size(40f, 120f)
    )
    scope.drawCircle(
        color = parentColor,
        radius = 20f,
        center = Offset(centerX + 30f, standingY - 130f)
    )
}

// --- SCENE 2: ROOTS (The Giant Iroko) ---
@Composable
fun BackgroundSceneRoots() {
    androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
        // Background
        drawRect(brush = Brush.verticalGradient(colors = listOf(Color(0xFFFFF3E0), Color(0xFFFFE0B2))))
        
        val centerX = size.width / 2
        val groundY = size.height * 0.55f
        
        // 1. Underground Soil (Dark Layer handled by the card overlay, but we add texture here)
        
        // 2. The Great Tree
        drawIrokoTree(this, centerX, groundY)
    }
}

private fun drawIrokoTree(scope: DrawScope, x: Float, y: Float) {
    val trunkColor = IrokoBrown
    
    // Roots spreading out
    val rootPath = Path().apply {
        moveTo(x - 40f, y)
        // Left Roots
        cubicTo(x - 100f, y + 100f, x - 200f, y + 150f, x - 300f, y + 400f)
        lineTo(x + 300f, y + 400f)
        // Right Roots
        cubicTo(x + 200f, y + 150f, x + 100f, y + 100f, x + 40f, y)
        close()
    }
    scope.drawPath(rootPath, color = trunkColor)
    
    // Trunk
    val trunkPath = Path().apply {
        moveTo(x - 40f, y)
        quadraticBezierTo(x - 20f, y - 200f, x - 100f, y - 400f) // Left branch
        lineTo(x + 100f, y - 400f) // Top
        quadraticBezierTo(x + 20f, y - 200f, x + 40f, y) // Right branch
        close()
    }
    scope.drawPath(trunkPath, color = trunkColor)
    
    // Canopy (Detailed Leaves)
    scope.drawCircle(color = IrokoForestGreen, radius = 250f, center = Offset(x, y - 450f))
    scope.drawCircle(color = IrokoForestGreen.copy(alpha=0.8f), radius = 200f, center = Offset(x - 150f, y - 400f))
    scope.drawCircle(color = IrokoForestGreen.copy(alpha=0.8f), radius = 200f, center = Offset(x + 150f, y - 400f))
}

// --- SCENE 3: VILLAGE ---
@Composable
fun BackgroundSceneVillage() {
    androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
        drawRect(brush = Brush.verticalGradient(colors = listOf(Color(0xFFE1F5FE), Color(0xFFB3E5FC))))
        
        // Ground
        drawRect(
            color = Color(0xFFD7CCC8),
            topLeft = Offset(0f, size.height * 0.5f),
            size = Size(size.width, size.height * 0.5f)
        )
        
        // Huts
        drawHut(this, size.width * 0.2f, size.height * 0.5f)
        drawHut(this, size.width * 0.5f, size.height * 0.48f)
        drawHut(this, size.width * 0.8f, size.height * 0.52f)
    }
}

private fun drawHut(scope: DrawScope, x: Float, y: Float) {
    // Wall
    scope.drawRect(
        color = IrokoClay,
        topLeft = Offset(x - 40f, y),
        size = Size(80f, 60f)
    )
    // Roof (Thatch)
    val roofPath = Path().apply {
        moveTo(x - 50f, y)
        lineTo(x, y - 50f)
        lineTo(x + 50f, y)
        close()
    }
    scope.drawPath(roofPath, color = IrokoGold)
    // Door
    scope.drawRect(
        color = Color.Black.copy(alpha=0.5f),
        topLeft = Offset(x - 15f, y + 20f),
        size = Size(30f, 40f)
    )
}

// --- SCENE 4: GUARDIAN ---
@Composable
fun BackgroundSceneGuardian() {
    androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
        drawRect(brush = Brush.verticalGradient(colors = listOf(Color(0xFFF3E5F5), Color(0xFFE1BEE7))))
        
        // Symbolic Eye/Shield
        drawCircle(
            color = IrokoBrown.copy(alpha=0.1f),
            radius = 300f,
            center = center
        )
        drawCircle(
            color = IrokoGold.copy(alpha=0.2f),
            radius = 200f,
            center = center
        )
    }
}

// --- SCENE 5: TRANSITION ---
@Composable
fun BackgroundSceneTransition() {
    androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
        drawRect(brush = Brush.verticalGradient(colors = listOf(Color(0xFFE8F5E9), Color(0xFFC8E6C9))))
        
        // Rays of light
        val center = Offset(size.width/2, size.height/2)
        for (i in 0 until 12) {
            rotate(i * 30f, center) {
                drawRect(
                    color = Color.White.copy(alpha=0.2f),
                    topLeft = center,
                    size = Size(1000f, 50f)
                )
            }
        }
    }
}
