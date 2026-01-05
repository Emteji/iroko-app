package com.emteji.lifepathchild.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class BinaryOption(
    val id: String,
    val text: String,
    val color: Color = Color(0xFFE0F7FA), // Default soft teal
    val iconEmoji: String? = null // Simple emoji support for now
)

@Composable
fun BinaryChoiceCard(
    optionA: BinaryOption,
    optionB: BinaryOption,
    onOptionSelected: (BinaryOption) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OptionCard(
            option = optionA,
            modifier = Modifier.weight(1f),
            onOptionSelected = onOptionSelected
        )
        OptionCard(
            option = optionB,
            modifier = Modifier.weight(1f),
            onOptionSelected = onOptionSelected
        )
    }
}

@Composable
fun OptionCard(
    option: BinaryOption,
    modifier: Modifier = Modifier,
    onOptionSelected: (BinaryOption) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxHeight()
            .clickable { onOptionSelected(option) },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = option.color),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            option.iconEmoji?.let {
                Text(
                    text = it,
                    fontSize = 48.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            Text(
                text = option.text,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }
    }
}
