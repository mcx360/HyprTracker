package io.github.mcx360.hyprtracker.ui.graphScreen.components

import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
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