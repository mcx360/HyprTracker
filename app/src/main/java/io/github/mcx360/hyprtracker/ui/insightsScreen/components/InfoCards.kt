package io.github.mcx360.hyprtracker.ui.insightsScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.mcx360.hyprtracker.R
import io.github.mcx360.hyprtracker.ui.insightsScreen.InsightsViewModel
import io.github.mcx360.hyprtracker.ui.model.MinMaxAvg

@Composable
fun InfoCards(viewModel: InsightsViewModel){
    val insightsUIState by viewModel.uiState.collectAsStateWithLifecycle()

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(start =16.dp,top =16.dp, end = 16.dp)
    ) {
        Text(
            text = "Your key blood pressure metrics",
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "change >>",
            textAlign = TextAlign.End,
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.labelLarge
        )
    }

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
    ) {
        info(
            modifier = Modifier.weight(0.33f),
            title = stringResource(R.string.systolic),
            min = insightsUIState.systolicMin,
            avg = insightsUIState.systolicAverage,
            max = insightsUIState.systolicMax
        )
        Spacer(modifier = Modifier.width(8.dp))
        info(
            modifier = Modifier.weight(0.33f),
            title = stringResource(R.string.diastolic),
            min = insightsUIState.diastolicMin,
            avg = insightsUIState.diastolicAverage,
            max = insightsUIState.diastolicMax
        )
        Spacer(modifier = Modifier.width(8.dp))
        info(
            modifier = Modifier.weight(0.33f),
            title = stringResource(R.string.pulse),
            min = insightsUIState.pulseMin,
            avg = insightsUIState.pulseAverage,
            max = insightsUIState.pulseMax
        )
    }
}

@Composable
fun info(
    modifier: Modifier,
    title: String,
    min: String,
    max: String,
    avg: String
){
    val haptic = LocalHapticFeedback.current
    var dataShown by remember { mutableStateOf( MinMaxAvg.Average)}

    OutlinedCard(modifier = modifier.clickable(onClick = {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            dataShown = when (dataShown) {
                MinMaxAvg.Average -> MinMaxAvg.Max
                MinMaxAvg.Max -> MinMaxAvg.Min
                MinMaxAvg.Min -> MinMaxAvg.Average
            }
        })
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Row {
                Text(
                    text = title,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = modifier.weight(1f))
                Icon(
                    painter = painterResource(R.drawable.heart_3_),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary
                )
            }

            Row {
                Text(
                    text = when(dataShown){
                        MinMaxAvg.Min -> min
                        MinMaxAvg.Average -> avg
                        MinMaxAvg.Max -> max
                    },
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = stringResource(R.string.mmHg),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .align(Alignment.Bottom)
                        .padding(horizontal = 8.dp)
                )
            }
            Text(text = stringResource(dataShown.labelRes))
        }
    }
}