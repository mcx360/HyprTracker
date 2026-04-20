package io.github.mcx360.hyprtracker.ui.graphScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.lineSeries
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.pie.data.PieChartModelProducer
import com.patrykandpatrick.vico.compose.pie.data.pieSeries
import io.github.mcx360.hyprtracker.R
import io.github.mcx360.hyprtracker.ui.HyprTrackerViewModel
import io.github.mcx360.hyprtracker.ui.graphScreen.components.EmptyInsightsScreen
import io.github.mcx360.hyprtracker.ui.graphScreen.components.HypertensionStagesPieChart
import io.github.mcx360.hyprtracker.ui.utils.DotWithColour
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GraphScreen(modifier: Modifier = Modifier, hyprTrackerViewModel: HyprTrackerViewModel){
    val uiState by hyprTrackerViewModel.uiState.collectAsState()

    if (uiState.readings.isEmpty()) {
        EmptyInsightsScreen()
    }
    else{
        Column(
            modifier = modifier.fillMaxSize().padding(8.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val showFilterByDropDownMenu = remember { mutableStateOf(false) }
            val filterOption = remember { mutableStateOf("week") }
            val systolicDataShown = remember { mutableStateOf("Average") }
            val diastolicDataShown = remember { mutableStateOf("Average") }
            val pulseDataShown = remember { mutableStateOf("Average") }

                Card(modifier = modifier.fillMaxWidth().padding(8.dp)) {
                    Row(modifier = modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                        Text("Filter by")
                        ExposedDropdownMenuBox(
                            expanded = showFilterByDropDownMenu.value,
                            onExpandedChange = {},
                        ) {
                            OutlinedTextField(
                                value = filterOption.value,
                                readOnly = true,
                                onValueChange = {},
                                trailingIcon = {
                                    IconButton(
                                        onClick = {
                                            showFilterByDropDownMenu.value =
                                                !showFilterByDropDownMenu.value
                                        }) {
                                        if (showFilterByDropDownMenu.value){
                                            Icon(Icons.Filled.KeyboardArrowUp, contentDescription = null)
                                        } else{
                                            Icon(Icons.Filled.KeyboardArrowDown, contentDescription = null)
                                        }
                                    }
                                }
                            )
                            ExposedDropdownMenu(
                                expanded = showFilterByDropDownMenu.value,
                                onDismissRequest = { showFilterByDropDownMenu.value = false },
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Week") },
                                    onClick = {
                                        filterOption.value = "Week"
                                             showFilterByDropDownMenu.value = false
                                              },
                                )
                                DropdownMenuItem(
                                    text = { Text("Month") },
                                    onClick = {
                                        filterOption.value = "Month"
                                        showFilterByDropDownMenu.value = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("All time") },
                                    onClick = {
                                        filterOption.value = "All time"
                                    showFilterByDropDownMenu.value = false
                                    }
                                )
                            }
                        }
                    }
                }
            Row(modifier = modifier.fillMaxWidth().padding(8.dp)) {
                Card(modifier = modifier.weight(0.33f).clickable(onClick = {
                    when (systolicDataShown.value) {
                        "Average" -> {
                            systolicDataShown.value = "Max"
                        }
                        "Max" -> {
                            systolicDataShown.value = "Min"
                        }
                        else -> {
                            systolicDataShown.value = "Average"
                        }
                    }
                })) {
                    Column(
                        modifier = modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Systolic", style = MaterialTheme.typography.titleLarge)
                        Text(when(filterOption.value){
                                "All time" -> if (systolicDataShown.value == "Average") hyprTrackerViewModel.getSystolicAverage(cutoffDate = null).toString() else if (systolicDataShown.value == "Max") hyprTrackerViewModel.getSystolicMax(cutoffDate = null).toString() else hyprTrackerViewModel.getSystolicMin(cutoffDate = null).toString()
                                "Month" -> if (systolicDataShown.value == "Average") hyprTrackerViewModel.getSystolicAverage(cutoffDate = LocalDate.now().minusMonths(1).toString()).toString() else if (systolicDataShown.value == "Max") hyprTrackerViewModel.getSystolicMax(cutoffDate = LocalDate.now().minusMonths(1).toString()).toString() else hyprTrackerViewModel.getSystolicMin(cutoffDate = LocalDate.now().minusMonths(1).toString()).toString()
                                else -> if (systolicDataShown.value == "Average") hyprTrackerViewModel.getSystolicAverage(cutoffDate = LocalDate.now().minusWeeks(1).toString()).toString() else if (systolicDataShown.value == "Max") hyprTrackerViewModel.getSystolicMax(cutoffDate = LocalDate.now().minusWeeks(1).toString()).toString() else hyprTrackerViewModel.getSystolicMin(cutoffDate = LocalDate.now().minusWeeks(1).toString()).toString()
                            })
                        Text(systolicDataShown.value)
                    }
                }
                Card(modifier = modifier.weight(0.33f).padding(horizontal = 8.dp).clickable(onClick = {
                    when (diastolicDataShown.value) {
                        "Average" -> {
                            diastolicDataShown.value = "Max"
                        }
                        "Max" -> {
                            diastolicDataShown.value = "Min"
                        }
                        else -> {
                            diastolicDataShown.value = "Average"
                        }
                    }
                })) {
                    Column(
                        modifier = modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Diastolic", style = MaterialTheme.typography.titleLarge)
                        Text(when(filterOption.value){
                            "All time" -> if (diastolicDataShown.value == "Average") hyprTrackerViewModel.getDiastolicAverage(cutoffDate = null).toString() else if (diastolicDataShown.value == "Max") hyprTrackerViewModel.getDiastolicMax(cutoffDate = null).toString() else hyprTrackerViewModel.getDiastolicMin(cutoffDate = null).toString()
                            "Month" -> if (diastolicDataShown.value == "Average") hyprTrackerViewModel.getDiastolicAverage(cutoffDate = LocalDate.now().minusMonths(1).toString()).toString() else if (diastolicDataShown.value == "Max") hyprTrackerViewModel.getDiastolicMax(cutoffDate = LocalDate.now().minusMonths(1).toString()).toString() else hyprTrackerViewModel.getDiastolicMin(cutoffDate = LocalDate.now().minusMonths(1).toString()).toString()
                            else -> if (diastolicDataShown.value == "Average") hyprTrackerViewModel.getDiastolicAverage(cutoffDate = LocalDate.now().minusWeeks(1).toString()).toString() else if (diastolicDataShown.value == "Max") hyprTrackerViewModel.getDiastolicMax(cutoffDate = LocalDate.now().minusWeeks(1).toString()).toString() else hyprTrackerViewModel.getDiastolicMin(cutoffDate = LocalDate.now().minusWeeks(1).toString()).toString()
                        })
                        Text(diastolicDataShown.value)
                    }
                }
                Card(modifier = modifier.weight(0.33f).clickable(onClick = {
                    when (pulseDataShown.value) {
                        "Average" -> {
                            pulseDataShown.value = "Max"
                        }
                        "Max" -> {
                            pulseDataShown.value = "Min"
                        }
                        else -> {
                            pulseDataShown.value = "Average"
                        }
                    }
                })) {
                    Column(
                        modifier = modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Pulse", style = MaterialTheme.typography.titleLarge)
                        Text(when(filterOption.value){
                            "All time" -> if (pulseDataShown.value == "Average") hyprTrackerViewModel.getPulseAverage(cutoffDate = null).toString() else if (pulseDataShown.value == "Max") hyprTrackerViewModel.getPulseMax(cutoffDate = null).toString() else hyprTrackerViewModel.getPulseMin(cutoffDate = null).toString()
                            "Month" -> if (pulseDataShown.value == "Average") hyprTrackerViewModel.getPulseAverage(cutoffDate = LocalDate.now().minusMonths(1).toString()).toString() else if (diastolicDataShown.value == "Max") hyprTrackerViewModel.getPulseMax(cutoffDate = LocalDate.now().minusMonths(1).toString()).toString() else hyprTrackerViewModel.getPulseMin(cutoffDate = LocalDate.now().minusMonths(1).toString()).toString()
                            else -> if (pulseDataShown.value == "Average") hyprTrackerViewModel.getPulseAverage(cutoffDate = LocalDate.now().minusWeeks(1).toString()).toString() else if (pulseDataShown.value == "Max") hyprTrackerViewModel.getPulseMax(cutoffDate = LocalDate.now().minusWeeks(1).toString()).toString() else hyprTrackerViewModel.getPulseMin(cutoffDate = LocalDate.now().minusWeeks(1).toString()).toString()
                        })
                        Text(pulseDataShown.value)
                    }
                }
            }

            Card(modifier = modifier.fillMaxWidth().padding(8.dp)) {
                //val breakdown = hyprTrackerViewModel.getBPStagesBreakdown(null)
                Text("BP Stages Breakdown", modifier = modifier.fillMaxWidth().padding(8.dp), textAlign = TextAlign.Start, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                val modelProducer = remember { PieChartModelProducer() }
                val dateFilter = when (filterOption.value) {
                    "All time" -> null
                    "Month" -> LocalDate.now().minusMonths(1).toString()
                    else -> LocalDate.now().minusWeeks(1).toString()
                }

                val breakdown = hyprTrackerViewModel.getBPStagesBreakdown(dateFilter)

                LaunchedEffect(filterOption.value) {
                    modelProducer.runTransaction {
                        pieSeries {
                            series(*breakdown.toTypedArray())
                        }
                    }
                }

                HypertensionStagesPieChart(modelProducer, modifier)
                Row(modifier = modifier.fillMaxWidth().padding(8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DotWithColour(colorResource(R.color.Hypertension_Normal_Stage_Colour))
                    Text(" Normal", style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier.padding(4.dp))
                    DotWithColour(colorResource(R.color.Hypertension_High_Normal_Stage_Colour))
                    Text(" Normal High",  style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier.padding(4.dp))
                    DotWithColour(colorResource(R.color.Hypertension_Grade1_Colour))
                    Text(" Grade 1",  style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier.padding(4.dp))
                    DotWithColour(colorResource(R.color.Hypertension_Grade2_Colour))
                    Text(" Grade 2",  style = MaterialTheme.typography.bodySmall)
                }
            }

            Card(modifier = modifier.fillMaxWidth().padding(8.dp).height(300.dp)) {
                Text("BP Trends Breakdown", modifier = modifier.fillMaxWidth().padding(8.dp), textAlign = TextAlign.Start, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                val modelProducer = remember { CartesianChartModelProducer() }
                LaunchedEffect(Unit) {
                    modelProducer.runTransaction {
                        lineSeries {
                            series(
                                13,
                                8,
                                7,
                                12,
                                0,
                                1,
                                15,
                                14,
                                0,
                                11,
                                6,
                                12,
                                0,
                                11,
                                12,
                                11
                            )
                        }
                    }
                    modelProducer.runTransaction {
                        lineSeries { series(6, 7, 8, 9, 10) }
                    }
                }
                CartesianChartHost(
                    rememberCartesianChart(
                        rememberLineCartesianLayer(),
                        startAxis = VerticalAxis.rememberStart(),
                        bottomAxis = HorizontalAxis.rememberBottom(),
                    ),
                    modelProducer,
                )
                Row(modifier = modifier.fillMaxWidth().padding(8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(modifier = modifier.fillMaxWidth().padding(8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        DotWithColour(Color.Blue)
                        Text(" Systolic", style = MaterialTheme.typography.bodySmall)
                        Spacer(modifier.padding(4.dp))
                        DotWithColour(Color.Red)
                        Text(" Diastolic",  style = MaterialTheme.typography.bodySmall)
                        Spacer(modifier.padding(4.dp))
                        DotWithColour(Color.Green)
                        Text(" Pulse",  style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
