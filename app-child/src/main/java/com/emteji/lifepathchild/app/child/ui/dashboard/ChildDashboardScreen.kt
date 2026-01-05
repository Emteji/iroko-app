package com.emteji.lifepathchild.app.child.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emteji.lifepathchild.app.child.ui.components.AIAvatar
import com.emteji.lifepathchild.app.child.ui.components.IrokoWorldBackground
import com.emteji.lifepathchild.app.child.ui.theme.*

import com.emteji.lifepathchild.app.child.ui.emotion.MoodCheckInDialog

@Composable
fun ChildDashboardScreen(
    onNavigateToMissions: () -> Unit,
    onNavigateToPlay: () -> Unit,
    onNavigateToSocial: () -> Unit = {},
    onNavigateToWallet: () -> Unit = {},
    onNavigateToScenarios: () -> Unit = {},
    onNavigateToRewards: () -> Unit = {},
    viewModel: ChildDashboardViewModel = hiltViewModel()
) {
    val isSpeaking by viewModel.isSpeaking.collectAsState()
    var showMoodDialog by remember { mutableStateOf(false) }

    if (showMoodDialog) {
        MoodCheckInDialog(
            onDismiss = { showMoodDialog = false },
            onMoodSelected = { mood -> 
                viewModel.saveMood(mood)
                showMoodDialog = false 
            }
        )
    }

    IrokoWorldBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // Header: AI Companion & Greeting
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AIAvatar(isSpeaking = isSpeaking, isListening = false)
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Good Morning, Joshua.",
                            style = MaterialTheme.typography.headlineSmall,
                            color = IrokoBrown,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "I have a mission for you.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = IrokoStone
                        )
                    }
                }
                
                // Mood Trigger
                IconButton(onClick = { showMoodDialog = true }) {
                    Text("😊", fontSize = 24.sp)
                }
            }
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // Main Action Card (The "Journey" Step)
            JourneyCard(
                title = "Daily Missions",
                subtitle = "3 Tasks Waiting",
                icon = Icons.Filled.Star,
                color = IrokoForestGreen,
                onClick = onNavigateToMissions
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            JourneyCard(
                title = "Play Zone",
                subtitle = "Unlocked at 4:00 PM",
                icon = Icons.Filled.PlayArrow,
                color = IrokoGold,
                onClick = onNavigateToPlay,
                isLocked = true
            )
        }
    }
}

@Composable
fun JourneyCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    onClick: () -> Unit,
    isLocked: Boolean = false
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = IrokoWhite)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(if (isLocked) Color.Gray.copy(alpha = 0.2f) else color.copy(alpha = 0.2f), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isLocked) Color.Gray else color,
                    modifier = Modifier.size(32.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(24.dp))
            
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (isLocked) Color.Gray else IrokoBrown
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = IrokoStone
                )
            }
        }
    }
}
