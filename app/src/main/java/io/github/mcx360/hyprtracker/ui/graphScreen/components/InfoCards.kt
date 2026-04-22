package io.github.mcx360.hyprtracker.ui.graphScreen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import io.github.mcx360.hyprtracker.R
import java.time.LocalDate

@Composable
fun InfoCards(
    modifier: Modifier = Modifier,
    systolicDataShown: String,
    updateSystolicDataShown: (String) -> Unit,
    filterOption: String,
    getSystolicAverage: (String?) -> (String),
    getSystolicMax: (String?) -> (String),
    getSystolicMin: (String?) -> (String),
    diastolicDataShown: String,
    getDiastolicAverage: (String?) -> (String),
    getDiastolicMax: (String?) -> (String),
    getDiastolicMin: (String?) -> (String),
    updateDiastolicDataShown: (String) -> Unit,
    pulseDataShown: String,
    getPulseAverage: (String?) -> (String),
    getPulseMax: (String?) -> (String),
    getPulseMin: (String?) -> (String),
    updatePulseDataShown: (String) -> Unit,
){
    Row(modifier = modifier.fillMaxWidth().padding(8.dp)) {

        val haptic = LocalHapticFeedback.current

        //Systolic Info
        Card(modifier = modifier.weight(0.33f).clickable(onClick = {
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            when (systolicDataShown) {
                "Average" -> updateSystolicDataShown("Max")
                "Max" -> updateSystolicDataShown("Min")
                else -> updateSystolicDataShown("Average")
            } })
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
                    Text("Systolic", textAlign = TextAlign.Start)
                    Spacer(modifier.weight(1f))
                    Icon(
                        painter = painterResource(R.drawable.heart_3_),
                        contentDescription = null,
                    )
                }

                Row {
                    Text(
                        when (filterOption) {
                            "All time" -> when (systolicDataShown) {
                                "Average" -> getSystolicAverage(null)
                                "Max" -> getSystolicMax(null)
                                else -> getSystolicMin(null)
                            }

                            "Month" -> when (systolicDataShown) {
                                "Average" -> getSystolicAverage(LocalDate.now().minusMonths(1).toString())
                                "Max" -> getSystolicMax(LocalDate.now().minusMonths(1).toString())
                                else -> getSystolicMin(LocalDate.now().minusMonths(1).toString())
                            }

                            else -> when (systolicDataShown) {"Average" -> getSystolicAverage(LocalDate.now().minusWeeks(1).toString())
                                "Max" -> getSystolicMax(LocalDate.now().minusWeeks(1).toString())
                                else -> getSystolicMin(LocalDate.now().minusWeeks(1).toString())
                            }
                        },
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "mmHg",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .align(Alignment.Bottom)
                            .padding(horizontal = 8.dp)
                        )
                }
                Text(systolicDataShown)
            }
        }

        //Diastolic Info
        Card(
            modifier = modifier
                .weight(0.33f)
                .padding(horizontal = 8.dp)
                .clickable(onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    when (diastolicDataShown) {
                        "Average" -> updateDiastolicDataShown("Max")
                        "Max" -> updateDiastolicDataShown("Min")
                        else -> updateDiastolicDataShown("Average")
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
                    Text("Diastolic")
                    Spacer(modifier.weight(1f))
                    Icon(
                        painter = painterResource(R.drawable.heart_3_),
                        contentDescription = null,
                    )
                }
                Row {
                    Text(
                        when (filterOption) {
                            "All time" -> when (diastolicDataShown) {
                                "Average" -> getDiastolicAverage(null)
                                "Max" -> getDiastolicMax(null)
                                else -> getDiastolicMin(null)
                            }
                            "Month" -> when (diastolicDataShown) {
                                "Average" -> getDiastolicAverage(LocalDate.now().minusMonths(1).toString())
                                "Max" -> getDiastolicMax(LocalDate.now().minusMonths(1).toString())
                                else -> getDiastolicMin(LocalDate.now().minusMonths(1).toString())
                            }
                            else -> when (diastolicDataShown) {
                                "Average" -> getDiastolicAverage(LocalDate.now().minusWeeks(1).toString())
                                "Max" -> getDiastolicMax(LocalDate.now().minusWeeks(1).toString())
                                else -> getDiastolicMin(LocalDate.now().minusWeeks(1).toString())
                            }
                        },
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "mmHg",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .align(Alignment.Bottom)
                            .padding(horizontal = 8.dp)
                    )
                }
                Text(
                    diastolicDataShown,
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
                    when (pulseDataShown) {
                        "Average" -> updatePulseDataShown("Max")
                        "Max" -> updatePulseDataShown("Min")
                        else -> updatePulseDataShown("Average")
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
                    Text(text = "Pulse")
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
                            "All time" -> when (pulseDataShown) {
                                "Average" -> getPulseAverage(null)
                                "Max" -> getPulseMax(null)
                                else -> getPulseMin(null)
                            }

                            "Month" -> when (pulseDataShown) {
                                "Average" -> getPulseAverage(LocalDate.now().minusMonths(1).toString())
                                "Max" -> getPulseMax(LocalDate.now().minusMonths(1).toString())
                                else -> getPulseMin(LocalDate.now().minusMonths(1).toString())
                            }

                            else -> when (pulseDataShown) {
                                "Average" -> getPulseAverage(LocalDate.now().minusWeeks(1).toString())
                                "Max" -> getPulseMax(LocalDate.now().minusWeeks(1).toString())
                                else -> getPulseMin(LocalDate.now().minusWeeks(1).toString())
                            }
                        },
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold

                    )
                    Text(
                        text = "bpm",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .align(Alignment.Bottom)
                            .padding(horizontal = 8.dp)
                    )
                }
                Text(pulseDataShown)
            }
        }
    }
}