package com.emteji.lifepathchild.app.child.ui.wallet

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun WalletScreen(
    viewModel: WalletViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(text = "My Wallet", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.child != null) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Total XP: ${uiState.child!!.xpTotal}", style = MaterialTheme.typography.displaySmall)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Grit Score: ${uiState.child!!.gritScore}")
                        Text("Omoluabi Score: ${uiState.child!!.omoluabiScore}")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text("Rewards Market (Coming Soon)", style = MaterialTheme.typography.titleMedium)
            } else if (uiState.error != null) {
                Text("Error: ${uiState.error}")
            } else {
                CircularProgressIndicator()
            }
        }
    }
}
