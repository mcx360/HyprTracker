package io.github.mcx360.hyprtracker.ui.insightsScreen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AreaChart
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.pie.data.PieChartModelProducer
import com.patrykandpatrick.vico.compose.pie.data.pieSeries
import io.github.mcx360.hyprtracker.ui.utils.EmptyScreen
import io.github.mcx360.hyprtracker.R
import io.github.mcx360.hyprtracker.ui.utils.Dot
import io.github.mcx360.hyprtracker.ui.utils.RangePickerDialog
import io.github.mcx360.hyprtracker.ui.utils.convertMillisToDate
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GraphScreen(
    modifier: Modifier = Modifier,
    insightsViewModel: InsightsViewModel
) {
    insightsViewModel.checkRecordsAreAvailable()
    val uiState by insightsViewModel.uiState.collectAsState()
    var selectedIndex by remember { mutableIntStateOf(2) }

    if (uiState.hasRecords) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(8.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.Filter_By),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(top = 16.dp, start = 16.dp, bottom = 4.dp),
                )
            }

            //Row with segmented button choices
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                val options = listOf(stringResource(R.string.Week), stringResource(R.string.Month), stringResource(R.string.All),stringResource(R.string.Custom))
                val showCustomDateRangePicker = remember { mutableStateOf(false) }

                SingleChoiceSegmentedButtonRow {
                    options.forEachIndexed { index, label ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = options.size
                            ),
                            onClick = { if (index == 3) showCustomDateRangePicker.value = true else selectedIndex = index  },
                            selected = index == selectedIndex,
                            label = { Text(text = label) }
                        )
                    }
                }

                when(selectedIndex){
                    0 -> insightsViewModel.setTimePeriod(LocalDate.now().minusWeeks(1).toString(), LocalDate.now().toString())
                    1 -> insightsViewModel.setTimePeriod(LocalDate.now().minusMonths(1).toString(), LocalDate.now().toString())
                    2 -> insightsViewModel.setTimePeriod(null, null)
                }

                when{
                    showCustomDateRangePicker.value -> RangePickerDialog(onDismissRequest = {showCustomDateRangePicker.value = false}, onDatesGiven = { start, end -> insightsViewModel.setTimePeriod(convertMillisToDate(start), convertMillisToDate(end))}, onFinish = {selectedIndex = 3})
                }
            }

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(start =16.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Key metrics",
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                TextButton(onClick = {}) {
                    Text(
                        text = "change >>",
                        textAlign = TextAlign.End,
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
            ) {
                info(
                    modifier = Modifier.weight(0.33f),
                    title = stringResource(R.string.systolic),
                    min = uiState.systolicMin,
                    avg = uiState.systolicAverage,
                    max = uiState.systolicMax
                )
                Spacer(modifier = Modifier.width(8.dp))
                info(
                    modifier = Modifier.weight(0.33f),
                    title = stringResource(R.string.diastolic),
                    min = uiState.diastolicMin,
                    avg = uiState.diastolicAverage,
                    max = uiState.diastolicMax
                )
                Spacer(modifier = Modifier.width(8.dp))
                info(
                    modifier = Modifier.weight(0.33f),
                    title = stringResource(R.string.pulse),
                    min = uiState.pulseMin,
                    avg = uiState.pulseAverage,
                    max = uiState.pulseMax
                )
            }

            OutlinedCard(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                val breakdown = uiState.bpStages
                val systolicRange = uiState.systolicMin+"–"+uiState.systolicMax
                val diastolicRange = uiState.diastolicMin+"–"+uiState.diastolicMax
                val pulseRange = uiState.pulseMin+"–"+uiState.pulseMax
                val nonZeroValues = breakdown.filter { it > 0f }
                val singleFullSlice = nonZeroValues.size == 1
                val hasNoData = breakdown.all { it == 0f }
                val chartColors = listOf(
                    colorResource(R.color.Hypertension_Normal_Stage_Colour),
                    colorResource(R.color.Hypertension_High_Normal_Stage_Colour),
                    colorResource(R.color.Hypertension_Grade1_Colour),
                    colorResource(R.color.Hypertension_Grade2_Colour)
                )
                val nonZeroIndex = breakdown.indexOfFirst { it > 0f }
                val singleSliceColor = if (nonZeroIndex != -1) chartColors[nonZeroIndex] else MaterialTheme.colorScheme.primary

                Column(modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainerHigh)) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = stringResource(R.string.Pie_Chart_Label),
                                modifier = modifier.padding(top = 8.dp, start = 8.dp),
                                textAlign = TextAlign.Start,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.weight(1f))

                            Icon(
                                imageVector = Icons.Filled.AreaChart,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.align(Alignment.CenterVertically).padding(end = 8.dp)
                            )
                        }
                        Text(
                            text = "Based on ISH classification",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = modifier.padding(start = 8.dp, bottom = 8.dp)
                        )
                    }

                    if (singleFullSlice) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .size(240.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                drawCircle(color = singleSliceColor)
                            }
                            Text(
                                text = "100%",
                                color = Color.White
                            )
                        }
                    } else if (hasNoData) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .size(240.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                drawCircle(color = Color.Gray)
                            }
                            Text(
                                text = "No data",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        val modelProducer = remember { PieChartModelProducer() }
                        LaunchedEffect(breakdown) {
                            modelProducer.runTransaction {
                                pieSeries {
                                    series(*breakdown.toTypedArray())
                                }
                            }
                        }
                        HypertensionStagesPieChart(modelProducer)
                    }

                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier.background(colorResource(R.color.Hypertension_Normal_Stage_Background)).padding(4.dp)) {
                            Row {
                                Dot(colorResource(R.color.Hypertension_Normal_Stage_Colour))
                                Spacer(modifier.padding(start = 4.dp))
                                Text(
                                    text = stringResource(R.string.Normal),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = colorResource(R.color.Hypertension_Normal_Stage_Colour))
                            }
                        }
                        Spacer(modifier.padding(4.dp))

                        Box(
                            modifier
                                .background(colorResource(R.color.Hypertension_High_Normal_Stage_Background))
                                .padding(4.dp)
                        ) {
                            Row {
                                Dot(colorResource(R.color.Hypertension_High_Normal_Stage_Colour))
                                Spacer(modifier.padding(start = 4.dp))
                                Text(
                                    text = stringResource(R.string.High_normal),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = colorResource(R.color.Hypertension_High_Normal_Stage_Colour)
                                )
                            }
                        }

                        Spacer(modifier.padding(4.dp))

                        Box(modifier =modifier.background(colorResource(R.color.Hypertension_Grade1_Background)).padding(4.dp)) {
                            Row {
                                Dot(colorResource(R.color.Hypertension_Grade1_Colour))
                                Spacer(modifier.padding(start = 4.dp))
                                Text(
                                    text = stringResource(R.string.Grade1),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = colorResource(R.color.Hypertension_Grade1_Colour)
                                )
                            }
                        }

                        Spacer(modifier.padding(4.dp))

                        Box(modifier = modifier.background(colorResource(R.color.Hypertension_Grade2_Background)).padding(4.dp)) {
                            Row {
                                Dot(colorResource(R.color.Hypertension_Grade2_Colour))
                                Spacer(modifier.padding(start = 4.dp))
                                Text(
                                    stringResource(R.string.Grade2),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = colorResource(R.color.Hypertension_Grade2_Colour)
                                )
                            }
                        }
                    }
                    HorizontalDivider(modifier = modifier.padding(8.dp), thickness = 2.dp )

                    Row(modifier.fillMaxWidth().padding(start = 8.dp, end = 8.dp)) {
                        Text(
                            text = "Systolic Range:",
                            modifier = modifier.padding(4.dp),
                            textAlign = TextAlign.Start,
                            //color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = systolicRange,
                            textAlign = TextAlign.End,
                            modifier = modifier.fillMaxWidth().padding(end = 8.dp),
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    Row(modifier.fillMaxWidth().padding(start = 8.dp, end = 8.dp)) {
                        Text(
                            text = "Diastolic Range:",
                            modifier = modifier.padding(4.dp),
                            textAlign = TextAlign.Start,
                            //color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            diastolicRange,
                            textAlign = TextAlign.End,
                            modifier = modifier.fillMaxWidth().padding(end = 8.dp),
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    Row(modifier.fillMaxWidth().padding(start = 8.dp, end = 8.dp, bottom = 8.dp)) {
                        Text(
                            text = "Pulse Range:",
                            modifier = modifier.padding(4.dp),
                            textAlign = TextAlign.Start,
                            //color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = pulseRange,
                            textAlign = TextAlign.End,
                            modifier = modifier.fillMaxWidth().padding(end = 8.dp),
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
        }
    }else{
        EmptyScreen(
            painter = painterResource(R.drawable.undraw_key_insights),
            heading = stringResource(R.string.Empty_Graph_Screen_Title),
            subHeading = stringResource(R.string.Empty_Graph_Screen_Text)
        )
    }
}