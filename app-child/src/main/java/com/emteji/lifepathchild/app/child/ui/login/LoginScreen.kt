package com.emteji.lifepathchild.app.child.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emteji.lifepathchild.app.child.ui.theme.*
import com.emteji.lifepathchild.core.ui.components.AppTextField
import com.emteji.lifepathchild.core.ui.components.PrimaryButton

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: ChildLoginViewModel = hiltViewModel()
) {
    val pairingCode by viewModel.pairingCode.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Scaffold(
        containerColor = Color.Transparent
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .woodGrainBackground()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            // Main Login Card
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .carvedWood(cornerRadius = 24.dp)
                    .padding(24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Logo / Icon Placeholder
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(WoodGold, CircleShape)
                            .border(4.dp, Color(0xFF8D6E63), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Lock,
                            contentDescription = "Access",
                            tint = Color.White,
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Village Access",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    
                    Text(
                        text = "Enter your pairing code",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFFFFE0B2)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    AppTextField(
                        value = pairingCode,
                        onValueChange = { viewModel.updateCode(it) },
                        label = "Parent Pairing Code"
                    )

                    if (error != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = error!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    PrimaryButton(
                        text = if (isLoading) "Checking..." else "Enter Village",
                        onClick = { viewModel.attemptPairing(onSuccess = onLoginSuccess) },
                        enabled = !isLoading
                    )
                }
            }
        }
    }
}
