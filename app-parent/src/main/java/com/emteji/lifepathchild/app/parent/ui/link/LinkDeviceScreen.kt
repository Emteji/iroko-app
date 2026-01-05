package com.emteji.lifepathchild.app.parent.ui.link

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emteji.lifepathchild.app.parent.ui.components.IrokoButton
import com.emteji.lifepathchild.app.parent.ui.components.IrokoCard
import com.emteji.lifepathchild.app.parent.ui.theme.*

@Composable
fun LinkDeviceScreen(
    childId: String,
    onContinue: () -> Unit
) {
    // For demo purposes, we'll just show the first 8 chars as a "code" 
    // In production, we'd map this UUID to a short 6-digit code in the DB.
    val pairingCode = childId.take(8).uppercase()

    Scaffold(
        containerColor = IrokoCream
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Background Texture
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(ParchmentGradient)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Village Established",
                    style = MaterialTheme.typography.displaySmall,
                    color = IrokoBrown,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "To connect the Child App, enter this code on their device:",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = IrokoStone
                )
                
                Spacer(modifier = Modifier.height(48.dp))
                
                IrokoCard(
                    backgroundColor = IrokoBrown,
                    elevation = 8.dp,
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.QrCode,
                            contentDescription = null,
                            tint = IrokoGold,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = pairingCode,
                            style = MaterialTheme.typography.displayMedium,
                            color = IrokoGold,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 8.sp
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(48.dp))
                
                Text(
                    text = "Download 'Iroko Child' on the target device to proceed.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = IrokoStone.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                IrokoButton(
                    text = "I've Linked the Device",
                    onClick = onContinue,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
