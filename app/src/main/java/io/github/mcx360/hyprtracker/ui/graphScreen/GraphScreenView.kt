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
import androidx.compose.material.icons.filled.ArrowDropDown
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
import io.github.mcx360.hyprtracker.ui.graphScreen.components.HypertensionStagesPieChart
import io.github.mcx360.hyprtracker.ui.utils.DotWithColour
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GraphScreen(modifier: Modifier = Modifier, hyprTrackerViewModel: HyprTrackerViewModel){
    val uiState by hyprTrackerViewModel.uiState.collectAsState()

    if (uiState.readings.isEmpty()) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.undraw_key_insights),
                contentDescription = null,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
            )
            Text(
                text = stringResource(R.string.Empty_Graph_Screen_Title),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
                modifier = modifier.padding(top = 16.dp)
            )
            Text(
                text = stringResource(R.string.Empty_Graph_Screen_Text),
                textAlign = TextAlign.Center,
                modifier = modifier.padding(16.dp)
            )
        }
    }
    else{
        Column(
            modifier = modifier.fillMaxSize().padding(8.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val showFilterByDropDownMenu = remember { mutableStateOf(false) }
            val filterOption = remember { mutableStateOf("week") }
            val dataShown = remember { mutableStateOf("Average") }

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
                    if (dataShown.value == "Average"){
                        dataShown.value = "Max"
                    } else if (dataShown.value == "Max"){
                        dataShown.value = "Min"
                    } else{
                        dataShown.value = "Average"
                    }
                })) {
                    Column(
                        modifier = modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Systolic", style = MaterialTheme.typography.titleLarge)
                        Text(when(filterOption.value){
                                "All time" -> if (dataShown.value == "Average") hyprTrackerViewModel.getSystolicAverage(cutoffDate = null).toString() else if (dataShown.value == "Max") hyprTrackerViewModel.getSystolicMax(cutoffDate = null).toString() else hyprTrackerViewModel.getSystolicMin(cutoffDate = null).toString()
                                "Month" -> if (dataShown.value == "Average") hyprTrackerViewModel.getSystolicAverage(cutoffDate = LocalDate.now().minusMonths(1).toString()).toString() else if (dataShown.value == "Max") hyprTrackerViewModel.getSystolicMax(cutoffDate = LocalDate.now().minusMonths(1).toString()).toString() else hyprTrackerViewModel.getSystolicMin(cutoffDate = LocalDate.now().minusMonths(1).toString()).toString()
                                else -> if (dataShown.value == "Average") hyprTrackerViewModel.getSystolicAverage(cutoffDate = LocalDate.now().minusWeeks(1).toString()).toString() else if (dataShown.value == "Max") hyprTrackerViewModel.getSystolicMax(cutoffDate = LocalDate.now().minusWeeks(1).toString()).toString() else hyprTrackerViewModel.getSystolicMin(cutoffDate = LocalDate.now().minusWeeks(1).toString()).toString()
                            })
                        Text(dataShown.value)
                    }
                }
                Card(modifier = modifier.weight(0.33f).padding(horizontal = 8.dp).clickable(onClick = {})) {
                    Column(
                        modifier = modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Disatolic", style = MaterialTheme.typography.titleLarge)
                        Text("67")
                        Text("Average")
                    }
                }
                Card(modifier = modifier.weight(0.33f).clickable(onClick = {})) {
                    Column(
                        modifier = modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Pulse", style = MaterialTheme.typography.titleLarge)
                        Text("70")
                        Text("Average")
                    }
                }
            }

            Card(modifier = modifier.fillMaxWidth().padding(8.dp)) {
                Text("BP Stages Breakdown", modifier = modifier.fillMaxWidth().padding(8.dp), textAlign = TextAlign.Start, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                val modelProducer = remember { PieChartModelProducer() }
                LaunchedEffect(Unit) {
                    modelProducer.runTransaction {
                        pieSeries {
                            series(
                                60,
                                20,
                                10,
                                10
                            )
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
