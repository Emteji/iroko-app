package com.emteji.lifepathchild.app.parent.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.emteji.lifepathchild.app.parent.ui.theme.*

/**
 * A premium card component that simulates layered paper/parchment with a soft shadow.
 * Used for all main content blocks in the Parent App.
 */
@Composable
fun IrokoCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    backgroundColor: Color = IrokoWhite,
    elevation: Dp = 2.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier
            .shadow(elevation, shape)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        shape = shape,
        color = backgroundColor,
        border = null // Optional: Add subtle border if needed for "paper" edge effect
    ) {
        Column(
            modifier = Modifier
                .background(CardGradient) // Subtle gradient for texture
                .padding(16.dp)
        ) {
            content()
        }
    }
}

/**
 * A specialized card for "Authority" actions, using the deep brown brand color.
 */
@Composable
fun IrokoDarkCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    IrokoCard(
        modifier = modifier,
        backgroundColor = IrokoBrown,
        content = {
            Column(
                modifier = Modifier
                    .background(PrimaryGradient)
                    .padding(16.dp)
            ) {
                content()
            }
        }
    )
}

/**
 * Primary action button with the signature "Gold" look or "Brown" look.
 */
@Composable
fun IrokoButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPrimary: Boolean = true,
    isLoading: Boolean = false,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isPrimary) IrokoBrown else IrokoGold,
            contentColor = if (isPrimary) IrokoGold else IrokoBrown,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = if (isPrimary) IrokoGold else IrokoBrown
            )
        } else {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                color = if (enabled) (if (isPrimary) IrokoGold else IrokoBrown) else Color.White
            )
        }
    }
}

/**
 * Status indicator for Child Stats (Level, Grit, etc.)
 */
@Composable
fun IrokoStatBar(
    label: String,
    value: Float, // 0.0 to 1.0
    color: Color = IrokoGold,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = IrokoStone
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.LightGray.copy(alpha = 0.3f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(value)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(4.dp))
                    .background(color)
            )
        }
    }
}
