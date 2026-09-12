package io.github.mcx360.hyprtracker.ui.insightsScreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.mcx360.hyprtracker.R
import io.github.mcx360.hyprtracker.ui.utils.RangePicker
import io.github.mcx360.hyprtracker.ui.utils.convertMillisToDate
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Filter(setFilterDates: (String?, String?) -> Unit){

    var selectedIndex by remember { mutableIntStateOf(2) }

    //Row with filter title
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
            0 -> setFilterDates(LocalDate.now().minusWeeks(1).toString(), LocalDate.now().toString())
            1 -> setFilterDates(LocalDate.now().minusMonths(1).toString(), LocalDate.now().toString())
            2 -> setFilterDates(null, null)
        }

        when{
            showCustomDateRangePicker.value -> RangePicker(onDismissRequest = {showCustomDateRangePicker.value = false}, onDatesGiven = {start, end ->setFilterDates(convertMillisToDate(start), convertMillisToDate(end))}, onFinish = {selectedIndex = 3})
        }
    }
}