package io.github.mcx360.hyprtracker.ui.graphScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import io.github.mcx360.hyprtracker.R
import io.github.mcx360.hyprtracker.ui.HyprTrackerViewModel
import io.github.mcx360.hyprtracker.ui.graphScreen.InsightsViewModel
import io.github.mcx360.hyprtracker.ui.utils.convertMillisToDate
import io.github.mcx360.hyprtracker.ui.utils.formatToDayMonthYear
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterCard(
    modifier: Modifier = Modifier,
    setFilterDates: (String?, String?) -> Unit,
    startDate: String,
    endDate: String,
){
    var selectedIndex by remember { mutableIntStateOf(0) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.Filter_By),
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.weight(0.2f)
        )

        OutlinedTextField(
            value = "${formatToDayMonthYear(startDate)}–${formatToDayMonthYear(endDate)}",
            readOnly = true,
            onValueChange = {},
            label = {Text("Date")},
            modifier = modifier.fillMaxWidth().padding(start = 8.dp).weight(0.8f),
            trailingIcon = {IconButton(onClick = {}){Icon(painter = painterResource(R.drawable.ic_date), contentDescription = null)} }
        )
    }

    Row(
        modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        val options = listOf("Week", "Month", "All","Custom")
        val showCustomDateRangePicker = remember { mutableStateOf(false) }

        SingleChoiceSegmentedButtonRow {
            options.forEachIndexed { index, label ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = options.size
                    ),
                    onClick = {
                        if (index ==3){
                            showCustomDateRangePicker.value = true
                        } else{
                            selectedIndex = index
                        } },
                    selected = index == selectedIndex,
                    label = { Text(label) }
                )
            }
        }

        when(selectedIndex){
            0 -> setFilterDates(LocalDate.now().minusWeeks(1).toString(), LocalDate.now().toString())
            1 -> setFilterDates(LocalDate.now().minusMonths(1).toString(), LocalDate.now().toString())
            2 -> setFilterDates(null, null)
        }

        when{
            showCustomDateRangePicker.value -> RangePicker(onDismissRequest = {showCustomDateRangePicker.value = false}, onDatesGiven = {start, end ->setFilterDates(convertMillisToDate(start), convertMillisToDate(end))}, onFinish = {selectedIndex = 3})
        }
    }
}

@Composable
fun RangePicker(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onDatesGiven: (Long, Long) -> Unit,
    onFinish: () -> Unit
){
    Dialog(
        onDismissRequest = {onDismissRequest()},
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )){

        val state = rememberDateRangePickerState()
        Column(
            modifier
                .fillMaxSize()
                .systemBarsPadding(),
            verticalArrangement = Arrangement.Top
        ) {
            Row(modifier = modifier
                .fillMaxWidth()
                .background(DatePickerDefaults.colors().containerColor)
                .padding(start = 12.dp, end = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = {onDismissRequest()}) {
                    Icon(Icons.Filled.Close, contentDescription = null)
                }
                TextButton(
                    onClick = {
                        onDatesGiven(state.selectedStartDateMillis ?: 0, state.selectedEndDateMillis ?: 0)
                        onFinish()
                        onDismissRequest()
                    },
                    enabled = state.selectedEndDateMillis != null
                ) {
                    Text("Save")
                }
            }
            DateRangePicker(state = state, modifier = modifier.weight(1f), showModeToggle = false)
        }
    }
}