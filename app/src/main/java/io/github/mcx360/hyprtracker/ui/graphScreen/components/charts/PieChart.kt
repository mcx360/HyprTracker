package io.github.mcx360.hyprtracker.ui.graphScreen.components.charts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
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
import com.patrykandpatrick.vico.compose.pie.data.pieSeries
import com.patrykandpatrick.vico.compose.pie.rememberPieChart
import io.github.mcx360.hyprtracker.R
import io.github.mcx360.hyprtracker.ui.model.FilterOption
import io.github.mcx360.hyprtracker.ui.utils.DotWithColour

@Composable
fun BPBreakdownCard(
    modifier: Modifier = Modifier,
    filterOption: FilterOption,
    breakdown: List<Float>
){
    Card(modifier = modifier
        .fillMaxWidth()
        .padding(8.dp)
    ) {
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

        val singleSliceColor = if (nonZeroIndex != -1)  chartColors[nonZeroIndex]  else  MaterialTheme.colorScheme.primary

        Text(
            text = stringResource(R.string.Pie_Chart_Label),
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

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
        } else if (hasNoData){
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
        }
        else {
            val modelProducer = remember { PieChartModelProducer() }
            LaunchedEffect(filterOption) {
                modelProducer.runTransaction {
                    pieSeries {
                        series(*breakdown.toTypedArray())
                    }
                }
            }

            HypertensionStagesPieChart(modelProducer)
        }

        Row(modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            DotWithColour(colorResource(R.color.Hypertension_Normal_Stage_Colour))
            Spacer(modifier.padding(start = 4.dp))
            Text(stringResource(R.string.Normal), style = MaterialTheme.typography.bodySmall)

            Spacer(modifier.padding(4.dp))

            DotWithColour(colorResource(R.color.Hypertension_High_Normal_Stage_Colour))
            Spacer(modifier.padding(start = 4.dp))
            Text(stringResource(R.string.High_normal),  style = MaterialTheme.typography.bodySmall)

            Spacer(modifier.padding(4.dp))

            DotWithColour(colorResource(R.color.Hypertension_Grade1_Colour))
            Spacer(modifier.padding(start = 4.dp))
            Text(stringResource(R.string.Grade1),  style = MaterialTheme.typography.bodySmall)

            Spacer(modifier.padding(4.dp))

            DotWithColour(colorResource(R.color.Hypertension_Grade2_Colour))
            Spacer(modifier.padding(start = 4.dp))
            Text(stringResource(R.string.Grade2),  style = MaterialTheme.typography.bodySmall)
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
        pieChartColors = listOf(colorResource(
            R.color.Hypertension_Normal_Stage_Colour),
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
                                    label =
                                        PieChart.SliceLabel.Inside(
                                            TextComponent(TextStyle(if (index == 2) Color.Black else Color.White))
                                        ),
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