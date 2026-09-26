package io.github.mcx360.hyprtracker.ui.insightsScreen

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AreaChart
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.common.Fill
import com.patrykandpatrick.vico.compose.common.ProvideVicoTheme
import com.patrykandpatrick.vico.compose.common.VicoTheme
import com.patrykandpatrick.vico.compose.common.component.TextComponent
import com.patrykandpatrick.vico.compose.common.vicoTheme
import com.patrykandpatrick.vico.compose.pie.PieChart
import com.patrykandpatrick.vico.compose.pie.PieChartHost
import com.patrykandpatrick.vico.compose.pie.data.PieChartModelProducer
import com.patrykandpatrick.vico.compose.pie.data.PieValueFormatter
import com.patrykandpatrick.vico.compose.pie.data.pieModel
import com.patrykandpatrick.vico.compose.pie.rememberPieChart
import io.github.mcx360.hyprtracker.ui.utils.EmptyScreen
import io.github.mcx360.hyprtracker.R
import io.github.mcx360.hyprtracker.ui.utils.Dot
import io.github.mcx360.hyprtracker.ui.utils.RangePickerDialog
import io.github.mcx360.hyprtracker.ui.utils.convertMillisToDate
import java.time.LocalDate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GraphScreen(insightsViewModel: InsightsViewModel) {
    val uiState by insightsViewModel.uiState.collectAsState()
    var selectedIndex by remember { mutableIntStateOf(2) }

    if (uiState.hasRecords) {
        Column(
            modifier = Modifier
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
                            shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
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
                    showCustomDateRangePicker.value ->
                        RangePickerDialog(
                            onDismissRequest = {showCustomDateRangePicker.value = false},
                            onDatesGiven = { start, end -> insightsViewModel.setTimePeriod(convertMillisToDate(start), convertMillisToDate(end))},
                            onFinish = {selectedIndex = 3 }
                        )
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
                Info(
                    modifier = Modifier.weight(0.33f),
                    title = stringResource(R.string.systolic),
                    min = uiState.systolicMin,
                    avg = uiState.systolicAverage,
                    max = uiState.systolicMax
                )
                Spacer(modifier = Modifier.width(8.dp))
                Info(
                    modifier = Modifier.weight(0.33f),
                    title = stringResource(R.string.diastolic),
                    min = uiState.diastolicMin,
                    avg = uiState.diastolicAverage,
                    max = uiState.diastolicMax
                )
                Spacer(modifier = Modifier.width(8.dp))
                Info(
                    modifier = Modifier.weight(0.33f),
                    title = stringResource(R.string.pulse),
                    min = uiState.pulseMin,
                    avg = uiState.pulseAverage,
                    max = uiState.pulseMax
                )
            }

            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainerHigh)) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = stringResource(R.string.Pie_Chart_Label),
                                modifier = Modifier.padding(top = 8.dp, start = 8.dp),
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
                            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                        )
                    }

                    val modelProducer = remember { PieChartModelProducer() }
                    LaunchedEffect(uiState.bpStages) {
                        modelProducer.runTransaction {
                            pieModel {
                                series(*uiState.bpStages.toTypedArray())
                            }
                        }
                    }
                    HypertensionStagesPieChart(modelProducer)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StageMarker(contentColour = colorResource(R.color.Hypertension_Normal_Stage_Colour), backgroundColour = colorResource(R.color.Hypertension_Normal_Stage_Background), stageName = stringResource(R.string.Normal))
                        Spacer(Modifier.padding(4.dp))
                        StageMarker(contentColour = colorResource(R.color.Hypertension_High_Normal_Stage_Colour), backgroundColour = colorResource(R.color.Hypertension_High_Normal_Stage_Background), stringResource(R.string.High_normal))
                        Spacer(Modifier.padding(4.dp))
                        StageMarker(contentColour = colorResource(R.color.Hypertension_Grade1_Colour), backgroundColour = colorResource(R.color.Hypertension_Grade1_Background), stageName = stringResource(R.string.Grade1))
                        Spacer(Modifier.padding(4.dp))
                        StageMarker(contentColour = colorResource(R.color.Hypertension_Grade2_Colour), backgroundColour = colorResource(R.color.Hypertension_Grade2_Background), stageName = stringResource(R.string.Grade2), )
                    }
                    HorizontalDivider(modifier = Modifier.padding(8.dp), thickness = 2.dp )

                    Row(Modifier.fillMaxWidth().padding(start = 8.dp, end = 8.dp)) {
                        Text(
                            text = "Systolic Range:",
                            modifier = Modifier.padding(4.dp),
                            textAlign = TextAlign.Start,
                        )
                        Text(
                            text = uiState.systolicMin+"–"+uiState.systolicMax,
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth().padding(end = 8.dp),
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    Row(Modifier.fillMaxWidth().padding(start = 8.dp, end = 8.dp)) {
                        Text(
                            text = "Diastolic Range:",
                            modifier = Modifier.padding(4.dp),
                            textAlign = TextAlign.Start,
                        )
                        Text(
                            text = uiState.diastolicMin+"–"+uiState.diastolicMax,
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth().padding(end = 8.dp),
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    Row(Modifier.fillMaxWidth().padding(start = 8.dp, end = 8.dp, bottom = 8.dp)) {
                        Text(
                            text = "Pulse Range:",
                            modifier = Modifier.padding(4.dp),
                            textAlign = TextAlign.Start,
                        )
                        Text(
                            text = uiState.pulseMin+"–"+uiState.pulseMax,
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth().padding(end = 8.dp),
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

@Composable
fun StageMarker(
    contentColour: Color,
    backgroundColour: Color,
    stageName: String
){
    Box(Modifier.background(backgroundColour).padding(4.dp)) {
        Row {
            Dot(contentColour)
            Spacer(Modifier.padding(start = 4.dp))
            Text(
                text = stageName,
                style = MaterialTheme.typography.bodySmall,
                color = contentColour
            )
        }
    }
}

enum class MinMaxAvg(@StringRes val labelRes: Int) {
    Min(R.string.Minimum),
    Max(R.string.Maximum),
    Average(R.string.Average)
}

@Composable
fun Info(
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

@Composable
fun HypertensionStagesPieChart(
    modelProducer: PieChartModelProducer,
    modifier: Modifier = Modifier,
) {
    val theme = VicoTheme(
        candlestickCartesianLayerColors =
            VicoTheme.CandlestickCartesianLayerColors(
                MaterialTheme.colorScheme.outlineVariant,
                MaterialTheme.colorScheme.outlineVariant,
                MaterialTheme.colorScheme.outlineVariant),
        columnCartesianLayerColors = listOf(),
        lineColor = Color.Black,
        textColor = Color.White,
        pieChartColors = listOf(
            colorResource(R.color.Hypertension_Normal_Stage_Colour),
            colorResource(R.color.Hypertension_High_Normal_Stage_Colour),
            colorResource(R.color.Hypertension_Grade1_Colour),
            colorResource(R.color.Hypertension_Grade2_Colour)
        )
    )
    ProvideVicoTheme(theme) {
        PieChartHost(
            chart =
                rememberPieChart(
                    sliceProvider =
                        PieChart.SliceProvider.series(
                            vicoTheme.pieChartColors.mapIndexed { index, color ->
                                PieChart.Slice(
                                    fill = Fill(color),
                                    label = PieChart.SliceLabel.Inside(TextComponent(TextStyle(if (index == 2) Color.Black else Color.White))),
                                )
                            }
                        ),
                    valueFormatter = PieValueFormatter { _, value, _ -> "${value.toInt()}%" },
                ),
            modelProducer = modelProducer,
            modifier = modifier.height(240.dp),
        )
    }
}