package io.github.mcx360.hyprtracker.ui.graphScreen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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

        //Systolic Info
        Card(modifier = modifier.weight(0.33f).clickable(onClick = {
            when (systolicDataShown) {
                "Average" -> updateSystolicDataShown("Max")
                "Max" -> updateSystolicDataShown("Min")
                else -> updateSystolicDataShown("Average")
            } })
        ) {
            Column(
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Systolic", style = MaterialTheme.typography.titleLarge)
                Text(when(filterOption){
                    "All time" -> if (systolicDataShown == "Average") getSystolicAverage(null).toString() else if (systolicDataShown == "Max") getSystolicMax(null).toString() else getSystolicMin(null).toString()
                    "Month" -> if (systolicDataShown == "Average") getSystolicAverage(LocalDate.now().minusMonths(1).toString()).toString() else if (systolicDataShown == "Max") getSystolicMax(LocalDate.now().minusMonths(1).toString()).toString() else getSystolicMin(LocalDate.now().minusMonths(1).toString()).toString()
                    else -> if (systolicDataShown == "Average") getSystolicAverage(LocalDate.now().minusWeeks(1).toString()).toString() else if (systolicDataShown == "Max") getSystolicMax(LocalDate.now().minusWeeks(1).toString()).toString() else getSystolicMin(LocalDate.now().minusWeeks(1).toString()).toString()
                })
                Text(systolicDataShown)
            }
        }

        //Diastolic Info
        Card(modifier = modifier.weight(0.33f).padding(horizontal = 8.dp).clickable(onClick = {
            when (diastolicDataShown) {
                "Average" -> updateDiastolicDataShown("Max")
                "Max" -> updateDiastolicDataShown("Min")
                else -> updateDiastolicDataShown("Average")
            } })
        ) {
            Column(
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Diastolic", style = MaterialTheme.typography.titleLarge)
                Text(when(filterOption){
                    "All time" -> if (diastolicDataShown == "Average") getDiastolicAverage(null) else if (diastolicDataShown == "Max") getDiastolicMax(null) else getDiastolicMin(null)
                    "Month" -> if (diastolicDataShown == "Average") getDiastolicAverage(LocalDate.now().minusMonths(1).toString()) else if (diastolicDataShown == "Max") getDiastolicMax(LocalDate.now().minusMonths(1).toString()) else getDiastolicMin(LocalDate.now().minusMonths(1).toString())
                    else -> if (diastolicDataShown == "Average") getDiastolicAverage(LocalDate.now().minusWeeks(1).toString()) else if (diastolicDataShown == "Max") getDiastolicMax(LocalDate.now().minusWeeks(1).toString()) else getDiastolicMin(LocalDate.now().minusWeeks(1).toString())
                })
                Text(diastolicDataShown)
            }

        }

        //Pulse info
        Card(modifier = modifier.weight(0.33f).clickable(onClick = {
            when (pulseDataShown) {
                "Average" -> updatePulseDataShown("Max")
                "Max" -> updatePulseDataShown("Min")
                else -> updatePulseDataShown("Average")
            } })
        ) {
            Column(
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Pulse", style = MaterialTheme.typography.titleLarge)
                Text(when(filterOption){
                    "All time" -> if (pulseDataShown == "Average") getPulseAverage(null) else if (pulseDataShown == "Max") getPulseMax(null) else getPulseMin(null)
                    "Month" -> if (pulseDataShown == "Average") getPulseAverage(LocalDate.now().minusMonths(1).toString()) else if (diastolicDataShown == "Max") getPulseMax(LocalDate.now().minusMonths(1).toString()) else getPulseMin(LocalDate.now().minusMonths(1).toString())
                    else -> if (pulseDataShown == "Average") getPulseAverage(LocalDate.now().minusWeeks(1).toString()) else if (pulseDataShown == "Max") getPulseMax(LocalDate.now().minusWeeks(1).toString()) else getPulseMin(LocalDate.now().minusWeeks(1).toString())
                })
                Text(pulseDataShown)
            }
        }
    }
}