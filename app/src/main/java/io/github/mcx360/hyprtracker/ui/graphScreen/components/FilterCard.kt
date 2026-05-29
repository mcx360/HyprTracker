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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
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
import io.github.mcx360.hyprtracker.ui.model.FilterOption
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterCard(
    modifier: Modifier = Modifier,
    filterOption : FilterOption,
    updateFilterOption: (FilterOption) -> Unit,
    lastDate: LocalDate
){
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
                value = when(filterOption){
                    FilterOption.Week -> "${LocalDate.now().minusWeeks(1).dayOfMonth} ${LocalDate.now().minusWeeks(1).month.toString().substring(0,3).lowercase().replaceFirstChar {it.uppercase() }} ${LocalDate.now().minusWeeks(1).year}–${LocalDate.now().dayOfMonth} ${LocalDate.now().month.toString().substring(0,3).lowercase().replaceFirstChar { it.uppercase() }} ${LocalDate.now().year}"
                    FilterOption.Month -> "${LocalDate.now().minusMonths(1).dayOfMonth} ${LocalDate.now().minusMonths(1).month.toString().substring(0,3).lowercase().replaceFirstChar {it.uppercase() }} ${LocalDate.now().minusMonths(1).year}–${LocalDate.now().dayOfMonth} ${LocalDate.now().month.toString().substring(0,3).lowercase().replaceFirstChar { it.uppercase() }} ${LocalDate.now().year}"
                    FilterOption.AllTime -> "${lastDate.dayOfMonth} ${lastDate.month.toString().substring(0,3).lowercase().replaceFirstChar { it.uppercase()}} ${lastDate.year}–${LocalDate.now().dayOfMonth} ${LocalDate.now().month.toString().substring(0, 3).lowercase().replaceFirstChar { it.uppercase() }} ${LocalDate.now().year}"
                    FilterOption.Custom -> "Custom_date-custom_date"
                },
                readOnly = true,
                onValueChange = {},
                label = {Text("Date")},
                modifier = modifier.fillMaxWidth().padding(start = 8.dp).weight(0.8f),
                trailingIcon = {IconButton(onClick = {}){Icon(painter = painterResource(R.drawable.ic_date), contentDescription = null)} }
            )
        }

    Row(modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
        var selectedIndex by remember { mutableIntStateOf(0) }
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
                        selectedIndex = index
                              if (selectedIndex ==3){
                                  showCustomDateRangePicker.value = true
                              }
                              },
                    selected = index == selectedIndex,
                    label = { Text(label) }
                )
            }
        }

        when(selectedIndex){
            0 -> updateFilterOption(FilterOption.Week)
            1 -> updateFilterOption(FilterOption.Month)
            2 -> updateFilterOption(FilterOption.AllTime)
            3 -> updateFilterOption(FilterOption.Custom)
        }

        val start = remember { mutableLongStateOf(0) }
        val end = remember { mutableLongStateOf(0) }
        when{
            showCustomDateRangePicker.value -> RangePicker(onDismissRequest = {showCustomDateRangePicker.value = false}, onStartGiven = {start.value = it}, onEndGiven = {end.value = it})
        }
    }
}

@Composable
fun RangePicker(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onStartGiven: (Long) -> Unit,
    onEndGiven: (Long) -> Unit
){
    Dialog(onDismissRequest = {},
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )){

        val state = rememberDateRangePickerState()
        Column(modifier.fillMaxSize().systemBarsPadding(), verticalArrangement = Arrangement.Top) {
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
                       onStartGiven(state.selectedStartDateMillis ?: 0)
                        onEndGiven(state.selectedEndDateMillis ?: 0)
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