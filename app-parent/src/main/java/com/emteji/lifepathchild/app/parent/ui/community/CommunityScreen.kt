package com.emteji.lifepathchild.app.parent.ui.community

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.emteji.lifepathchild.app.parent.navigation.ParentDestinations
import com.emteji.lifepathchild.app.parent.ui.components.IrokoCard
import com.emteji.lifepathchild.app.parent.ui.components.ParentBottomNavigation
import com.emteji.lifepathchild.app.parent.ui.theme.*

@Composable
fun CommunityScreen(
    navController: NavController,
    viewModel: CommunityViewModel = hiltViewModel()
) {
    val rankings by viewModel.rankings.collectAsState()

    Scaffold(
        containerColor = IrokoCream,
        bottomBar = { 
            ParentBottomNavigation(
                navController = navController,
                currentRoute = ParentDestinations.COMMUNITY
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Village Rankings",
                    style = MaterialTheme.typography.headlineMedium,
                    color = IrokoBrown,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Based on consistency & grit",
                    style = MaterialTheme.typography.bodyMedium,
                    color = IrokoStone
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            // Your Rank Card
            item {
                IrokoCard(
                    backgroundColor = IrokoBrown
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Your Ranking", color = IrokoGold, fontWeight = FontWeight.Bold)
                            Text("#453", style = MaterialTheme.typography.displayMedium, color = IrokoWhite)
                            Text("Top 23%", color = IrokoForestGreen, fontWeight = FontWeight.Bold)
                        }
                        // Badge or Icon
                    }
                }
            }
            
            item {
                Text(
                    text = "Leaderboard",
                    style = MaterialTheme.typography.titleMedium,
                    color = IrokoBrown,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            items(rankings) { item ->
                RankingItem(item)
            }
        }
    }
}

@Composable
fun RankingItem(item: CommunityRank) {
    val isMe = item.name.contains("You")
    
    IrokoCard(
        elevation = if (isMe) 4.dp else 1.dp,
        backgroundColor = if (isMe) IrokoWhite else IrokoCream
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(if (item.rank <= 3) IrokoGold else IrokoStone.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${item.rank}",
                        fontWeight = FontWeight.Bold,
                        color = if (item.rank <= 3) IrokoBrown else IrokoStone
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column {
                    Text(
                        text = item.name,
                        fontWeight = FontWeight.Bold,
                        color = IrokoBrown
                    )
                    Text(
                        text = item.score,
                        style = MaterialTheme.typography.bodySmall,
                        color = IrokoStone
                    )
                }
            }
            
            Icon(
                imageVector = when(item.trend) {
                    "up" -> Icons.Filled.ArrowUpward
                    "down" -> Icons.Filled.ArrowDownward
                    else -> Icons.Filled.Remove
                },
                contentDescription = null,
                tint = when(item.trend) {
                    "up" -> IrokoForestGreen
                    "down" -> ErrorRed
                    else -> IrokoStone
                },
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
