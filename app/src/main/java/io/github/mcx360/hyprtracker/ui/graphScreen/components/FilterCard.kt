package io.github.mcx360.hyprtracker.ui.graphScreen.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.ButtonGroup
import io.github.mcx360.hyprtracker.R
import io.github.mcx360.hyprtracker.ui.model.FilterOption
import io.github.mcx360.hyprtracker.ui.model.MinMaxAvg
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterCard(
    modifier: Modifier = Modifier,
    filterOption : FilterOption,
    updateFilterOption: (FilterOption) -> Unit
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
                    FilterOption.AllTime -> "All time"
                    FilterOption.Custom -> "Custom"
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

        SingleChoiceSegmentedButtonRow {
            options.forEachIndexed { index, label ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = options.size
                    ),
                    onClick = { selectedIndex = index },
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
    }
}