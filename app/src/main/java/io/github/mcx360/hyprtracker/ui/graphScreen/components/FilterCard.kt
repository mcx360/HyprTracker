package io.github.mcx360.hyprtracker.ui.graphScreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.mcx360.hyprtracker.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterCard(
    modifier: Modifier = Modifier,
    showFilterByDropDownMenu: Boolean,
    updateShowFilterByDropDownMenu: (Boolean) -> Unit,
    filterOption : String,
    updateFilterOption: (String) -> Unit
){
    Card(modifier = modifier.fillMaxWidth().padding(8.dp)) {
        Row(
            modifier = modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Filter By", style = MaterialTheme.typography.titleLarge)
            ExposedDropdownMenuBox(
                expanded = showFilterByDropDownMenu,
                onExpandedChange = {updateShowFilterByDropDownMenu(!showFilterByDropDownMenu)},
            ) {
                OutlinedTextField(
                    value = filterOption,
                    readOnly = true,
                    onValueChange = { updateShowFilterByDropDownMenu(!showFilterByDropDownMenu) },
                    leadingIcon = {Icon(painter = painterResource(R.drawable.ic_date), contentDescription = null)},
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                if (showFilterByDropDownMenu) updateShowFilterByDropDownMenu(
                                    false
                                ) else updateShowFilterByDropDownMenu(true)
                            }) {
                            if (showFilterByDropDownMenu) {
                                Icon(Icons.Filled.KeyboardArrowUp, contentDescription = null)
                            } else {
                                Icon(Icons.Filled.KeyboardArrowDown, contentDescription = null)
                            }
                        }
                    },
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .menuAnchor(
                            type = MenuAnchorType.PrimaryEditable,
                            enabled = true
                        )
                )
                ExposedDropdownMenu(
                    expanded = showFilterByDropDownMenu,
                    onDismissRequest = { updateShowFilterByDropDownMenu(false) },
                ) {
                    DropdownMenuItem(
                        text = { Text("Week") },
                        onClick = {
                            updateFilterOption("Week")
                            updateShowFilterByDropDownMenu(false)
                        },
                    )
                    DropdownMenuItem(
                        text = { Text("Month") },
                        onClick = {
                            updateFilterOption("Month")
                            updateShowFilterByDropDownMenu(false)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("All time") },
                        onClick = {
                            updateFilterOption("All time")
                            updateShowFilterByDropDownMenu(false)
                        }
                    )
                }
            }
        }
    }
}