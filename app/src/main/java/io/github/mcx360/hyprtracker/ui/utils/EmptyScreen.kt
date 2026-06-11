package io.github.mcx360.hyprtracker.ui.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun EmptyScreen(
    modifier: Modifier = Modifier,
    painter: Painter,
    heading: String,
    subHeading: String
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp).weight(0.6f),
            alignment = Alignment.BottomCenter
        )
        Column(modifier = Modifier.weight(0.4f).fillMaxWidth(), verticalArrangement =Arrangement.Top, horizontalAlignment =Alignment.CenterHorizontally) {
            Text(
                text = heading,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                text = subHeading,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
        }
    }
}