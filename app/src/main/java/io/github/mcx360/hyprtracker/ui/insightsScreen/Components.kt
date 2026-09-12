package io.github.mcx360.hyprtracker.ui.insightsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.patrykandpatrick.vico.compose.pie.rememberPieChart
import io.github.mcx360.hyprtracker.R
import io.github.mcx360.hyprtracker.ui.model.MinMaxAvg

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