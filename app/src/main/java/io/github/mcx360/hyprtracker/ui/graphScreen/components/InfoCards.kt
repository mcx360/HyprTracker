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
import io.github.mcx360.hyprtracker.R
import io.github.mcx360.hyprtracker.ui.model.FilterOption
import java.time.LocalDate
import io.github.mcx360.hyprtracker.ui.model.MinMaxAvg

@Composable
fun InfoCards(
    modifier: Modifier = Modifier,
    filterOption: FilterOption,
    getSystolicAverage: (String?) -> (String),
    getSystolicMax: (String?) -> (String),
    getSystolicMin: (String?) -> (String),
    getDiastolicAverage: (String?) -> (String),
    getDiastolicMax: (String?) -> (String),
    getDiastolicMin: (String?) -> (String),
    getPulseAverage: (String?) -> (String),
    getPulseMax: (String?) -> (String),
    getPulseMin: (String?) -> (String),
){
    Row(modifier = modifier.fillMaxWidth().padding(8.dp)) {

        val haptic = LocalHapticFeedback.current
        var systolicDataShown by remember { mutableStateOf( MinMaxAvg.Average)}
        var diastolicDataShown by remember { mutableStateOf(MinMaxAvg.Average) }
        var pulseDataShown by remember { mutableStateOf(MinMaxAvg.Average) }


        //Systolic Info
        Card(modifier = modifier
            .weight(0.33f)
            .clickable(onClick = {
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                systolicDataShown = when (systolicDataShown) {
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
                    Text(
                        when (filterOption) {
                            FilterOption.AllTime -> when (systolicDataShown) {
                                MinMaxAvg.Average -> getSystolicAverage(null)
                                MinMaxAvg.Max -> getSystolicMax(null)
                                else -> getSystolicMin(null)
                            }

                            FilterOption.Month -> when (systolicDataShown) {
                                MinMaxAvg.Average -> getSystolicAverage(LocalDate.now().minusMonths(1).toString())
                                MinMaxAvg.Max -> getSystolicMax(LocalDate.now().minusMonths(1).toString())
                                else -> getSystolicMin(LocalDate.now().minusMonths(1).toString())
                            }

                            else -> when (systolicDataShown) {
                                MinMaxAvg.Average -> getSystolicAverage(LocalDate.now().minusWeeks(1).toString())
                                MinMaxAvg.Max -> getSystolicMax(LocalDate.now().minusWeeks(1).toString())
                                else -> getSystolicMin(LocalDate.now().minusWeeks(1).toString())
                            }
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
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
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
                    Text(
                        when (filterOption) {
                            FilterOption.AllTime -> when (diastolicDataShown) {
                                MinMaxAvg.Average -> getDiastolicAverage(null)
                                MinMaxAvg.Max -> getDiastolicMax(null)
                                MinMaxAvg.Min -> getDiastolicMin(null)
                            }
                            FilterOption.Month -> when (diastolicDataShown) {
                                MinMaxAvg.Average -> getDiastolicAverage(LocalDate.now().minusMonths(1).toString())
                                MinMaxAvg.Max -> getDiastolicMax(LocalDate.now().minusMonths(1).toString())
                                else -> getDiastolicMin(LocalDate.now().minusMonths(1).toString())
                            }
                            else -> when (diastolicDataShown) {
                                MinMaxAvg.Average -> getDiastolicAverage(LocalDate.now().minusWeeks(1).toString())
                                MinMaxAvg.Max -> getDiastolicMax(LocalDate.now().minusWeeks(1).toString())
                                else -> getDiastolicMin(LocalDate.now().minusWeeks(1).toString())
                            }
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
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
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
                    Text(
                        when (filterOption) {
                            FilterOption.AllTime -> when (pulseDataShown) {
                                MinMaxAvg.Average -> getPulseAverage(null)
                                MinMaxAvg.Max -> getPulseMax(null)
                                else -> getPulseMin(null)
                            }

                            FilterOption.Month -> when (pulseDataShown) {
                                MinMaxAvg.Average -> getPulseAverage(LocalDate.now().minusMonths(1).toString())
                                MinMaxAvg.Max -> getPulseMax(LocalDate.now().minusMonths(1).toString())
                                else -> getPulseMin(LocalDate.now().minusMonths(1).toString())
                            }

                            else -> when (pulseDataShown) {
                                MinMaxAvg.Average -> getPulseAverage(LocalDate.now().minusWeeks(1).toString())
                                MinMaxAvg.Max -> getPulseMax(LocalDate.now().minusWeeks(1).toString())
                                else -> getPulseMin(LocalDate.now().minusWeeks(1).toString())
                            }
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