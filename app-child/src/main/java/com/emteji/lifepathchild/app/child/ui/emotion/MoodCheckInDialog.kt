package com.emteji.lifepathchild.app.child.ui.emotion

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.emteji.lifepathchild.app.child.ui.theme.*

@Composable
fun MoodCheckInDialog(
    onDismiss: () -> Unit,
    onMoodSelected: (String) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = IrokoWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "How are you feeling?",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = IrokoBrown
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = IrokoStone)
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Mood Grid
                val moods = listOf(
                    MoodOption("Happy", "😊", Color(0xFFFFF176)),
                    MoodOption("Calm", "😌", Color(0xFFAED581)),
                    MoodOption("Tired", "😴", Color(0xFFE0E0E0)),
                    MoodOption("Sad", "😔", Color(0xFF90CAF9)),
                    MoodOption("Angry", "😠", Color(0xFFEF9A9A)),
                    MoodOption("Proud", "😎", Color(0xFFFFB74D))
                )
                
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        moods.take(3).forEach { mood ->
                            MoodItem(mood, onMoodSelected)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        moods.takeLast(3).forEach { mood ->
                            MoodItem(mood, onMoodSelected)
                        }
                    }
                }
            }
        }
    }
}

data class MoodOption(val label: String, val emoji: String, val color: Color)

@Composable
fun MoodItem(mood: MoodOption, onClick: (String) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(80.dp)
            .clickable { onClick(mood.label) }
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(mood.color, CircleShape)
                .border(2.dp, IrokoWhite, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = mood.emoji, fontSize = 32.sp)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = mood.label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = IrokoBrown
        )
    }
}
