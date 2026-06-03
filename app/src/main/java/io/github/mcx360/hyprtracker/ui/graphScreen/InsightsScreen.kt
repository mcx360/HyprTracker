package io.github.mcx360.hyprtracker.ui.graphScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.mcx360.hyprtracker.ui.HyprTrackerViewModel
import io.github.mcx360.hyprtracker.ui.graphScreen.components.BPBreakdownCard
import io.github.mcx360.hyprtracker.ui.graphScreen.components.EmptyInsightsScreen
import io.github.mcx360.hyprtracker.ui.graphScreen.components.FilterCard
import io.github.mcx360.hyprtracker.ui.graphScreen.components.InfoCards

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GraphScreen(
    modifier: Modifier = Modifier,
    insightsViewModel: InsightsViewModel = viewModel(factory = InsightsViewModel.Factory)
) {
    val uiState by insightsViewModel.uiState.collectAsState()

    if (uiState.hasRecords) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(8.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            FilterCard(
                setFilterDates = { startDate, endDate -> insightsViewModel.setTimePeriod(startDate, endDate) },
                startDate = uiState.startDate,
                endDate = uiState.endDate
            )

            InfoCards(viewModel = insightsViewModel)

            /*
            BPBreakdownCard(
                breakdown = hyprTrackerViewModel.getBPStagesBreakdown(when (filterOption.value) {
                    FilterOption.AllTime -> null
                    FilterOption.Month -> LocalDate.now().minusMonths(1).toString()
                    else -> LocalDate.now().minusWeeks(1).toString()
                })
            )

             */


        }

    }else{
        EmptyInsightsScreen()
    }
}