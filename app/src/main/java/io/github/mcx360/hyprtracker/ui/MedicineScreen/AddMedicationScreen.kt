package io.github.mcx360.hyprtracker.ui.MedicineScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.mcx360.hyprtracker.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AddMedicationScreen(
    modifier: Modifier,
    openAddMedicationScreen: MutableState<Boolean>,
    snackBarHostState: SnackbarHostState,
    scope: CoroutineScope
){
    val haptic = LocalHapticFeedback.current
    var checked by remember {mutableStateOf(false)}
    val radioButtons = listOf("Continuous", "Number of days", "until selected date")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioButtons[0]) }
    var showFrequencyDropDOwnMenu by remember { mutableStateOf(false) }
    var showIntakeDropDownMenu by remember { mutableStateOf(false) }

    Column(modifier = modifier
        .fillMaxSize()
        .padding(8.dp)
        .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card() {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 16.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Medication Info",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = modifier.padding(4.dp),
                    fontWeight = FontWeight.Bold,
                )

                OutlinedTextField(
                    onValueChange = {},
                    value = "",
                    label = { Text("Medication name") }
                )

                OutlinedTextField(
                    onValueChange = {},
                    value = "",
                    label = { Text("Medication description") }
                )
            }
        }

        Spacer(modifier = modifier.height(16.dp))

        Card() {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Medication Dosage",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = modifier.padding(4.dp),
                    fontWeight = FontWeight.Bold,
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        readOnly = true,
                        onValueChange = {/*daily intake describes how many times a day you take this medicine e.g. 4 times a day*/ },
                        value = "",
                        label = { Text("Frequency") },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    showFrequencyDropDOwnMenu = true
                                }) {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            }
                        }
                    )
                    DropdownMenu(
                        expanded = showFrequencyDropDOwnMenu,
                        onDismissRequest = {showFrequencyDropDOwnMenu = false}
                    ) {
                        DropdownMenuItem(
                            text = {Text("Daily")},
                            onClick = {}
                        )
                        DropdownMenuItem(
                            text = {Text("Every nth day")},
                            onClick = {}
                        )
                        DropdownMenuItem(
                            text = {Text("selected dates")},
                            onClick = {}
                        )
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Info, contentDescription = null)
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        readOnly = true,
                        onValueChange = {/*Intake describes how many times a day you take this medicine if you are scheduled to take it that day e.g. if frequency is 2, how many times you take it every second daye.g. 4 times a day*/ },
                        value = "",
                        label = { Text("Intake ") },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    showIntakeDropDownMenu = true
                                }) {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            }
                        },
                    )
                    DropdownMenu(
                        expanded = showIntakeDropDownMenu,
                        onDismissRequest = {showIntakeDropDownMenu = false}
                    ) {
                        DropdownMenuItem(
                            text = {Text("Once daily")},
                            onClick = {}
                        )
                        DropdownMenuItem(
                            text = {Text("Twice daily")},
                            onClick = {}
                        )
                        DropdownMenuItem(
                            text = {Text("Three times daily")},
                            onClick = {}
                        )
                        DropdownMenuItem(
                            text = {Text("Four times daily")},
                            onClick = {}
                        )
                        DropdownMenuItem(
                            text = {Text("Five times daily")},
                            onClick = {}
                        )
                        DropdownMenuItem(
                            text = {Text("Six times daily")},
                            onClick = {}
                        )
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Info, contentDescription = null)
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        onValueChange = {},
                        value = "",
                        label = { Text("Dosage") }
                    )
                    IconButton(onClick = {/*dosage per intake describes what dosage you take each time e.g. take 1 red and 1 blue tablet*/ }) {
                        Icon(Icons.Default.Info, contentDescription = null)
                    }
                }
            }
        }

        Spacer(modifier = modifier.height(16.dp))

        Card() {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Reminders",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = modifier.padding(4.dp).weight(0.8f),
                        fontWeight = FontWeight.Bold,
                    )
                    Switch(
                        modifier = modifier.align(Alignment.CenterVertically),
                        checked = checked,
                        onCheckedChange = {
                            checked = it
                        }
                    )
                }
                if (checked) {
                    Row() {
                        Text("Reminders daily: 2")
                        IconButton(onClick = {}) {
                            Image(Icons.Filled.Edit, contentDescription = null)
                        }
                    }

                    Row() {
                        Button(onClick = {}) {
                            Icon(
                                painter = painterResource(R.drawable.ic_date),
                                contentDescription = null
                            )
                            Text("12:00")
                        }
                        Button(onClick = {}) {
                            Icon(
                                painter = painterResource(R.drawable.ic_date),
                                contentDescription = null
                            )
                            Text("20:00")
                        }
                    }
                }
            }
        }

        Spacer(modifier = modifier.height(16.dp))

        Card() {
            Text("Duration", modifier = modifier.padding(start = 16.dp))
            Text("Start date: 06/07/2067", modifier = modifier.padding(start = 16.dp))

            Column(modifier.selectableGroup()) {
                radioButtons.forEach { text ->
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .selectable(
                                selected = (text == selectedOption),
                                onClick = {onOptionSelected(text)},
                                role = Role.RadioButton
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = (text ==selectedOption), onClick = null)
                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        }

        Row(
            modifier = modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {
            Button(
                onClick = {
                    openAddMedicationScreen.value = !openAddMedicationScreen.value
                    haptic.performHapticFeedback(HapticFeedbackType.Reject)
                    scope.launch {
                        snackBarHostState.showSnackbar("Canceled adding medication")
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                Text("Cancel")
            }
            Button(
                onClick = {
                    openAddMedicationScreen.value = !openAddMedicationScreen.value
                    haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                    scope.launch {
                        snackBarHostState.showSnackbar("Medication added")
                    }
                },
                modifier = Modifier.weight(1f).padding(16.dp)
            ) {
                Text("Add medication")
            }
        }
    }
}