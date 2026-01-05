package com.emteji.lifepathchild.app.child.ui.focus

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emteji.lifepathchild.app.child.ui.components.AIAvatar
import com.emteji.lifepathchild.app.child.ui.components.IrokoWorldBackground
import com.emteji.lifepathchild.app.child.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskFocusScreen(
    taskId: String,
    onBack: () -> Unit,
    onComplete: () -> Unit,
    viewModel: TaskFocusViewModel = hiltViewModel()
) {
    val task by viewModel.task.collectAsState()
    val isSpeaking by viewModel.isSpeaking.collectAsState()
    
    LaunchedEffect(taskId) {
        viewModel.loadTask(taskId)
    }

    IrokoWorldBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = IrokoBrown)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        ) { padding ->
            if (task == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = IrokoGold)
                }
            } else {
                val currentTask = task!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    
                    // Top: AI Companion (Voice Interface)
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        AIAvatar(isSpeaking = isSpeaking, isListening = false)
                        Spacer(modifier = Modifier.height(16.dp))
                        Card(
                            colors = CardDefaults.cardColors(containerColor = IrokoWhite.copy(alpha = 0.9f)),
                            shape = RoundedCornerShape(16.dp),
                        ) {
                            Text(
                                text = "I am timing you. Do this with excellence.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = IrokoBrown,
                                modifier = Modifier.padding(16.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    // Middle: Task Instruction
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = currentTask.title,
                            style = MaterialTheme.typography.displaySmall,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            color = IrokoBrown
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = currentTask.description ?: "Focus on the details.",
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Center,
                            color = IrokoStone
                        )
                    }

                    // Bottom: Controls
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Voice Button
                        IconButton(
                            onClick = { /* Toggle Mic */ },
                            modifier = Modifier
                                .size(64.dp)
                                .background(IrokoWhite, CircleShape)
                        ) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = IrokoBrown)
                        }
                        
                        // Complete Button
                        Button(
                            onClick = { viewModel.completeTask(onComplete) },
                            modifier = Modifier
                                .height(64.dp)
                                .weight(1f)
                                .padding(start = 16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = IrokoForestGreen),
                            shape = RoundedCornerShape(32.dp)
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Complete", style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }
            }
        }
    }
}
