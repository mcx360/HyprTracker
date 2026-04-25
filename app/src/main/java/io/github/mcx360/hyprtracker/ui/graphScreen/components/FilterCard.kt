package io.github.mcx360.hyprtracker.ui.graphScreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.mcx360.hyprtracker.R
import io.github.mcx360.hyprtracker.ui.model.FilterOption
import io.github.mcx360.hyprtracker.ui.model.MinMaxAvg

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterCard(
    modifier: Modifier = Modifier,
    showFilterByDropDownMenu: Boolean,
    updateShowFilterByDropDownMenu: (Boolean) -> Unit,
    filterOption : FilterOption,
    updateFilterOption: (FilterOption) -> Unit
){
    Card(modifier = modifier
        .fillMaxWidth()
        .padding(8.dp)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.Filter_By),
                style = MaterialTheme.typography.titleLarge,
            )
            ExposedDropdownMenuBox(
                expanded = showFilterByDropDownMenu,
                onExpandedChange = {updateShowFilterByDropDownMenu(!showFilterByDropDownMenu)},
            ) {
                TextButton(
                    onClick = {},
                    modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .menuAnchor(
                        type = MenuAnchorType.PrimaryEditable,
                        enabled = true
                    ),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_date),
                        contentDescription = null
                    )
                    Text(stringResource(filterOption.labelRes))
                    IconButton(onClick = { updateShowFilterByDropDownMenu(!showFilterByDropDownMenu)}) {
                        if (showFilterByDropDownMenu) {
                            Icon(Icons.Filled.KeyboardArrowUp, contentDescription = null)
                        } else {
                            Icon(Icons.Filled.KeyboardArrowDown, contentDescription = null)
                        }
                    }
                }

                ExposedDropdownMenu(
                    expanded = showFilterByDropDownMenu,
                    onDismissRequest = { updateShowFilterByDropDownMenu(false) },
                ) {
                    DropdownMenuItem(
                        text = { Text(text = stringResource(R.string.Week)) },
                        onClick = {
                            updateFilterOption(FilterOption.Week)
                            updateShowFilterByDropDownMenu(false)
                        },
                    )
                    DropdownMenuItem(
                        text = { Text(text = stringResource(R.string.Month)) },
                        onClick = {
                            updateFilterOption(FilterOption.Month)
                            updateShowFilterByDropDownMenu(false)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = stringResource(R.string.All_Time)) },
                        onClick = {
                            updateFilterOption(FilterOption.AllTime)
                            updateShowFilterByDropDownMenu(false)
                        }
                    )
                }
            }
        }
    }
}