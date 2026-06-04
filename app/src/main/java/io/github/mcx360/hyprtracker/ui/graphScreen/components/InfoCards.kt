package io.github.mcx360.hyprtracker.ui.graphScreen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.mcx360.hyprtracker.R
import io.github.mcx360.hyprtracker.ui.graphScreen.InsightsUIState
import io.github.mcx360.hyprtracker.ui.graphScreen.InsightsViewModel
import java.time.LocalDate
import io.github.mcx360.hyprtracker.ui.model.MinMaxAvg

@Composable
fun InfoCards(
    modifier: Modifier = Modifier,
    viewModel: InsightsViewModel
){
    Row(modifier = modifier.fillMaxWidth().padding(8.dp)) {

        val haptic = LocalHapticFeedback.current
        val insightsUIState by viewModel.uiState.collectAsStateWithLifecycle()
        var systolicDataShown by remember { mutableStateOf( MinMaxAvg.Average)}
        var diastolicDataShown by remember { mutableStateOf(MinMaxAvg.Average) }
        var pulseDataShown by remember { mutableStateOf(MinMaxAvg.Average) }

        //Systolic Info
        Card(modifier = modifier
            .weight(0.33f)
            .clickable(onClick = {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                systolicDataShown = when (systolicDataShown) {
                    MinMaxAvg.Average -> MinMaxAvg.Max
                    MinMaxAvg.Max -> MinMaxAvg.Min
                    MinMaxAvg.Min -> MinMaxAvg.Average
                }
            })
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {

                Row(
                    horizontalArrangement = Arrangement.Start,
                    modifier = modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.systolic),
                        textAlign = TextAlign.Start
                    )
                    Spacer(modifier.weight(1f))
                    Icon(
                        painter = painterResource(R.drawable.heart_3_),
                        contentDescription = null,
                    )
                }


                Row {
                    Text(text = when(systolicDataShown){
                        MinMaxAvg.Min -> insightsUIState.systolicMin
                        MinMaxAvg.Average -> insightsUIState.systolicAverage
                        MinMaxAvg.Max -> insightsUIState.systolicMax
                    },
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
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
                Text(text = stringResource(systolicDataShown.labelRes))
            }
        }

        //Diastolic Info
        Card(
            modifier = modifier
                .weight(0.33f)
                .padding(horizontal = 8.dp)
                .clickable(onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    diastolicDataShown = when (diastolicDataShown) {
                        MinMaxAvg.Average -> MinMaxAvg.Max
                        MinMaxAvg.Max -> MinMaxAvg.Min
                        else -> MinMaxAvg.Average
                    }
                })
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    modifier = modifier.fillMaxWidth()
                ){
                    Text(text = stringResource(R.string.diastolic))
                    Spacer(modifier.weight(1f))
                    Icon(
                        painter = painterResource(R.drawable.heart_3_),
                        contentDescription = null,
                    )
                }


                Row {
                    Text(text = when(diastolicDataShown){
                        MinMaxAvg.Min -> insightsUIState.diastolicMin
                        MinMaxAvg.Average -> insightsUIState.diastolicAverage
                        MinMaxAvg.Max -> insightsUIState.diastolicMax
                    },
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
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
                Text(
                    text = stringResource(diastolicDataShown.labelRes),
                    modifier = modifier.fillMaxWidth(),
                )
            }
        }

        //Pulse info
        Card(
            modifier = modifier
                .weight(0.33f)
                .clickable(onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    pulseDataShown = when (pulseDataShown) {
                        MinMaxAvg.Average -> MinMaxAvg.Max
                        MinMaxAvg.Max -> MinMaxAvg.Min
                        else -> MinMaxAvg.Average
                    }
                })
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    modifier = modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.pulse))
                    Spacer(modifier.weight(1f))
                    Icon(
                        painter = painterResource(R.drawable.activity_1_),
                        contentDescription = null,
                    )
                }
                Row(
                    modifier = modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(text =  when(pulseDataShown){
                        MinMaxAvg.Min -> insightsUIState.pulseMin
                        MinMaxAvg.Average -> insightsUIState.pulseAverage
                        MinMaxAvg.Max -> insightsUIState.pulseMax
                    },
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = stringResource(R.string.bpm),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .align(Alignment.Bottom)
                            .padding(horizontal = 8.dp)
                    )
                }
                Text(stringResource(pulseDataShown.labelRes))
            }
        }
    }
}