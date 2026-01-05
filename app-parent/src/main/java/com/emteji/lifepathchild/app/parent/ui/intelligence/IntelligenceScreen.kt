package com.emteji.lifepathchild.app.parent.ui.intelligence

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.emteji.lifepathchild.app.parent.navigation.ParentDestinations
import com.emteji.lifepathchild.app.parent.ui.components.IrokoCard
import com.emteji.lifepathchild.app.parent.ui.components.IrokoDarkCard
import com.emteji.lifepathchild.app.parent.ui.components.ParentBottomNavigation
import com.emteji.lifepathchild.app.parent.ui.theme.*

@Composable
fun IntelligenceScreen(
    navController: NavController,
    viewModel: IntelligenceViewModel = hiltViewModel()
) {
    val selectedChild by viewModel.selectedChild.collectAsState()
    val insights by viewModel.insights.collectAsState()

    Scaffold(
        containerColor = IrokoCream,
        bottomBar = {
            ParentBottomNavigation(
                navController = navController,
                currentRoute = ParentDestinations.INTELLIGENCE
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(20.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Psychology, contentDescription = null, tint = IrokoBrown, modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "AI Guidance",
                            style = MaterialTheme.typography.headlineMedium,
                            color = IrokoBrown,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Intelligence for ${selectedChild?.name ?: "your child"}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = IrokoStone
                        )
                    }
                }
            }
            
            item {
                 IrokoDarkCard(modifier = Modifier.fillMaxWidth()) {
                     Text("STRATEGY FOCUS", color = IrokoGold, style = MaterialTheme.typography.labelSmall)
                     Spacer(modifier = Modifier.height(8.dp))
                     Text(
                         text = "We are currently building 'Autonomy'.",
                         color = IrokoWhite,
                         style = MaterialTheme.typography.titleLarge,
                         fontWeight = FontWeight.Bold
                     )
                     Spacer(modifier = Modifier.height(8.dp))
                     Text(
                         text = "The AI is assigning tasks that require self-initiation rather than parent prompts.",
                         color = IrokoWhite.copy(alpha = 0.8f),
                         style = MaterialTheme.typography.bodyMedium
                     )
                 }
            }

            items(insights) { insight ->
                InsightItem(insight)
            }
        }
    }
}

@Composable
fun InsightItem(insight: InsightCard) {
    IrokoCard {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (insight.type == InsightType.RISK) Icons.Filled.Warning else Icons.Filled.Lightbulb,
                    contentDescription = null,
                    tint = if (insight.type == InsightType.RISK) ErrorRed else IrokoGold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = insight.title,
                    fontWeight = FontWeight.Bold,
                    color = IrokoBrown,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = insight.body,
                style = MaterialTheme.typography.bodyMedium,
                color = IrokoStone
            )
            
            if (insight.methodUsed != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Method:", style = MaterialTheme.typography.labelSmall, color = IrokoBrown)
                    Spacer(modifier = Modifier.width(4.dp))
                    Surface(
                        color = IrokoBrown.copy(alpha = 0.1f),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = insight.methodUsed,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = IrokoBrown
                        )
                    }
                }
            }
        }
    }
}
