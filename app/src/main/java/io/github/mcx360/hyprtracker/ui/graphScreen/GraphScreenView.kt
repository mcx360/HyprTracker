package io.github.mcx360.hyprtracker.ui.graphScreen

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
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import io.github.mcx360.hyprtracker.ui.graphScreen.components.BPBreakdownCard
import io.github.mcx360.hyprtracker.ui.graphScreen.components.BPTrendsBreakdown
import io.github.mcx360.hyprtracker.ui.graphScreen.components.EmptyInsightsScreen
import io.github.mcx360.hyprtracker.ui.graphScreen.components.FilterCard
import io.github.mcx360.hyprtracker.ui.graphScreen.components.HypertensionStagesPieChart
import io.github.mcx360.hyprtracker.ui.graphScreen.components.InfoCards
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

            FilterCard(
                showFilterByDropDownMenu =showFilterByDropDownMenu.value,
                updateShowFilterByDropDownMenu = {showFilterByDropDownMenu.value = it},
                filterOption = filterOption.value,
                updateFilterOption = {filterOption.value = it}
            )

            InfoCards(
                systolicDataShown = systolicDataShown.value,
                updateSystolicDataShown = {systolicDataShown.value = it},
                filterOption = filterOption.value,
                getSystolicAverage = { (it?.let{ hyprTrackerViewModel.getSystolicAverage(it) } ?: hyprTrackerViewModel.getSystolicAverage(null)).toString() },
                getSystolicMax = { (it?.let { hyprTrackerViewModel.getSystolicMax(it) } ?: hyprTrackerViewModel.getSystolicMax(null)).toString() },
                getSystolicMin = { (it?.let { hyprTrackerViewModel.getSystolicMin(it) } ?: hyprTrackerViewModel.getSystolicMin(null)).toString() },
                diastolicDataShown = diastolicDataShown.value,
                getDiastolicMax = { (it?.let { hyprTrackerViewModel.getDiastolicMax(it) } ?: hyprTrackerViewModel.getDiastolicMax(null)).toString() },
                getDiastolicMin = { (it?.let { hyprTrackerViewModel.getDiastolicMin(it) } ?: hyprTrackerViewModel.getDiastolicMin(null)).toString() },
                getDiastolicAverage = { (it?.let { hyprTrackerViewModel.getDiastolicAverage(it) } ?: hyprTrackerViewModel.getDiastolicAverage(null)).toString() },
                updateDiastolicDataShown = {diastolicDataShown.value = it},
                updatePulseDataShown = {pulseDataShown.value = it},
                pulseDataShown = pulseDataShown.value,
                getPulseMax = { (it?.let { hyprTrackerViewModel.getPulseMax(it) } ?: hyprTrackerViewModel.getPulseMax(null)).toString() },
                getPulseMin = { (it?.let { hyprTrackerViewModel.getPulseMin(it) } ?: hyprTrackerViewModel.getPulseMin(null)).toString() },
                getPulseAverage = { (it?.let { hyprTrackerViewModel.getPulseAverage(it) } ?: hyprTrackerViewModel.getPulseAverage(null)).toString() }
            )

            BPBreakdownCard(
                filterOption = filterOption.value,
                breakdown = hyprTrackerViewModel.getBPStagesBreakdown(when (filterOption.value) {
                    "All time" -> null
                    "Month" -> LocalDate.now().minusMonths(1).toString()
                    else -> LocalDate.now().minusWeeks(1).toString()
                })
            )

            BPTrendsBreakdown()
        }
    }
}