package com.emteji.lifepathchild.app.child.ui.rewards

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emteji.lifepathchild.core.data.model.Reward
import com.emteji.lifepathchild.core.ui.components.AppCard

@Composable
fun RewardsScreen(
    viewModel: ChildRewardsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Marketplace", style = MaterialTheme.typography.headlineMedium)
                Text(text = "${uiState.currentXp} XP", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else if (uiState.error != null) {
                Text(text = "Error: ${uiState.error}", color = MaterialTheme.colorScheme.error)
            } else if (uiState.rewards.isEmpty()) {
                Text("No rewards available yet.")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(uiState.rewards) { reward ->
                        RewardItem(
                            reward = reward,
                            canAfford = uiState.currentXp >= reward.xpCost,
                            onRedeem = { viewModel.redeemReward(reward) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RewardItem(
    reward: Reward,
    canAfford: Boolean,
    onRedeem: () -> Unit
) {
    AppCard(
        title = reward.description,
        description = "${reward.xpCost} XP"
    ) {
        Button(
            onClick = onRedeem,
            enabled = canAfford,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) {
            Text(if (canAfford) "Redeem" else "Need more XP")
        }
    }
}
