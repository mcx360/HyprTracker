package io.github.mcx360.hyprtracker.ui.graphScreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.compose.cartesian.data.lineSeries
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import io.github.mcx360.hyprtracker.ui.HyprTrackerViewModel
import io.github.mcx360.hyprtracker.ui.utils.Days
import io.github.mcx360.hyprtracker.ui.utils.DotWithColour

@Composable
fun BPTrendsBreakdown(modifier: Modifier = Modifier, hyprTrackerViewModel: HyprTrackerViewModel){
    Card(modifier = modifier.fillMaxWidth().padding(8.dp).height(300.dp)) {
        Text("BP Trends Breakdown", modifier = modifier.fillMaxWidth().padding(8.dp), textAlign = TextAlign.Start, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        val modelProducer = remember { CartesianChartModelProducer() }
        LaunchedEffect(Unit) {
            modelProducer.runTransaction {
                lineSeries { series(70, 80, 9, 10, 10, 12, 90) }
            }
        }
        CartesianChartHost(
            rememberCartesianChart(
                rememberLineCartesianLayer(rangeProvider = CartesianLayerRangeProvider.fixed(minY = 30.0, maxY = 210.0)),
                startAxis = VerticalAxis.rememberStart(),
                bottomAxis = HorizontalAxis.rememberBottom(
                    valueFormatter = CartesianValueFormatter { _, x, _ ->
                        hyprTrackerViewModel.getWeekDaysFromToday()[x.toInt()]
                    }
                ),
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