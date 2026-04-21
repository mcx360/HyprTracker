package io.github.mcx360.hyprtracker.ui.loggingScreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.mcx360.hyprtracker.ui.utils.DotWithColour

@Composable
fun InfographicLine(
    color: Color,
    stage: Array<String>
) {
    Row(modifier = Modifier
        .padding(vertical = 8.dp, horizontal = 8.dp)
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        DotWithColour(color)
        Text(
            text = stage[0],
            modifier = Modifier
                .weight(2f)
                .padding(4.dp),
        )
        Text(text = stage[1],
            modifier = Modifier
                .weight(1f)
                .padding(4.dp),
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = stage[2],
            modifier = Modifier
                .weight(1f)
                .padding(4.dp),
            fontWeight = FontWeight.Bold,
        )
    }
}