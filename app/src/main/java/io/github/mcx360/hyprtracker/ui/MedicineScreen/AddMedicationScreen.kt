package io.github.mcx360.hyprtracker.ui.MedicineScreen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.ui.window.Dialog
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import io.github.mcx360.hyprtracker.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.annotations.TestOnly
import kotlin.Unit

@Composable
fun AddMedicationScreen(
    modifier: Modifier,
    openAddMedicationScreen: MutableState<Boolean>,
    snackBarHostState: SnackbarHostState,
    scope: CoroutineScope
){
    val haptic = LocalHapticFeedback.current
    var checked by remember {mutableStateOf(false)}
    val radioButtons = listOf("Continuous", "Specified number of days", "Until a selected date")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioButtons[0]) }
    var showFrequencyDropDOwnMenu by remember { mutableStateOf(false) }
    var showIntakeDropDownMenu by remember { mutableStateOf(false) }
    var showFrequencyInfoDialog by remember { mutableStateOf(false) }
    var showIntakeInfoDialog by remember { mutableStateOf(false) }
    var showDosageInfoDialog by remember { mutableStateOf(false) }
    var showSelectSpecifiedNumberOfDaysDialog by remember { mutableStateOf(false) }
    var showDurationDatePicker by remember { mutableStateOf(false) }
    var showSelectedDaysPicker by remember { mutableStateOf(false) }




    Column(modifier = modifier
        .fillMaxSize()
        .padding(16.dp)
        .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card() {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Medication Info",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = modifier.padding(4.dp),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary,
                )

                OutlinedTextField(
                    onValueChange = {},
                    value = "",
                    label = { Text("Medication name") },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                )

                OutlinedTextField(
                    onValueChange = {},
                    value = "",
                    label = { Text("Medication description") },
                    maxLines = 1,
                    placeholder = {Text("Description, e.g. 1 tablet daily")},
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                )
            }
        }

        Spacer(modifier = modifier.height(16.dp))

        Card() {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Medication Dosage",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = modifier.padding(4.dp),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        readOnly = true,
                        onValueChange = {},
                        value = "",
                        label = { Text("Frequency") },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    showFrequencyDropDOwnMenu = true
                                }) {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            }
                        },
                    )
                    DropdownMenu(
                        expanded = showFrequencyDropDOwnMenu,
                        onDismissRequest = {showFrequencyDropDOwnMenu = false},
                    ) {
                        DropdownMenuItem(
                            text = {Text("Every single day")},
                            onClick = {}
                        )
                        DropdownMenuItem(
                            text = {Text("On selected days only ")},
                            onClick = {showSelectedDaysPicker = true}
                        )
                        if (showSelectedDaysPicker){
                            SelectDaysForMedication(onDismiss = {showSelectedDaysPicker = false})
                        }

                    }
                    IconButton(onClick = {showFrequencyInfoDialog = true}) {
                        Icon(Icons.Default.Info, contentDescription = null)
                    }
                    if (showFrequencyInfoDialog){
                        InfoDialog(
                            onDismissRequest = {showFrequencyInfoDialog = false},
                            info = "Frequency specifies how frequently you take the medicine e.g. daily, weekly, or every certain amount of days")
                    }

                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        readOnly = true,
                        onValueChange = {},
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
                            text = {Text("One time daily")},
                            onClick = {}
                        )
                        DropdownMenuItem(
                            text = {Text("Two times daily")},
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
                    IconButton(onClick = {showIntakeInfoDialog = true}) {
                        Icon(Icons.Default.Info, contentDescription = null)
                    }
                    if(showIntakeInfoDialog){
                        InfoDialog(
                            info = "Intake describes how many times a day you take this medicine if you are scheduled to take it that day e.g. if frequency is 2, how many times you take it every second daye.g. 4 times a day",
                            onDismissRequest = {showIntakeInfoDialog = false})
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        onValueChange = {},
                        value = "",
                        label = { Text("Dosage") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        )
                    )
                    IconButton(onClick = {showDosageInfoDialog = true}) {
                        Icon(Icons.Default.Info, contentDescription = null)
                    }
                    if (showDosageInfoDialog){
                        InfoDialog(
                            info = "dosage per intake describes what dosage you take each time e.g. take 1 red and 1 blue tablet",
                            onDismissRequest = {showDosageInfoDialog = false}
                        )
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
                        text = "Notification Reminders",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = modifier.padding(4.dp).weight(0.8f),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
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
            Text(
                text = "Duration",
                modifier = modifier.padding(start = 16.dp, top = 16.dp),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
                )
            Text("Start date: 06/07/2067", modifier = modifier.padding(start = 16.dp))

            Column(modifier.selectableGroup()) {
                radioButtons.forEach { text ->
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .selectable(
                                selected = (text == selectedOption),
                                onClick = { onOptionSelected(text)
                                    when(text){
                                        "Specified number of days" -> showSelectSpecifiedNumberOfDaysDialog = true
                                        "Until a selected date" -> showDurationDatePicker = true
                                    }
                                          },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = (text==selectedOption), onClick = {
                            onOptionSelected(text)
                            when(text){
                                "Specified number of days" -> showSelectSpecifiedNumberOfDaysDialog = true
                                "Until a selected date" -> showDurationDatePicker = true
                            }
                        }
                        )
                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = modifier.padding(start = 16.dp)
                        )
                    }
                }
                if (selectedOption == "Specified number of days"){
                    Text("medicine recorded for next 66 days", modifier = modifier.padding(start = 16.dp, bottom = 16.dp))
                } else if(selectedOption == "Until a selected date"){
                    Text("Medicine recorded until 07/07/2067", modifier = modifier.padding(start = 16.dp, bottom = 16.dp))
                }else{

                }
            }
            if (showSelectSpecifiedNumberOfDaysDialog) {
                SelectSpecifiedNumberOfDaysDialog(onDismissRequest = {showSelectSpecifiedNumberOfDaysDialog = false})
            }
            if (showDurationDatePicker) {
                DurationDatePicker(onDateSelected = {}, onDismiss = {showDurationDatePicker = false})
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

@Composable
fun InfoDialog(info: String, onDismissRequest: () -> Unit){
    Dialog(onDismissRequest = {onDismissRequest()}) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(8.dp)
                ) {
                Text(info)
                Button(onClick = {onDismissRequest()}, modifier = Modifier.padding(8.dp)) {
                    Text("Ok")
                }
            }
        }
    }
}

@Composable
fun SelectSpecifiedNumberOfDaysDialog(onDismissRequest: () -> Unit){
        Dialog(onDismissRequest = {onDismissRequest}) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Enter the amount of days that the medicine will be taken for")
                    OutlinedTextField(
                        onValueChange = {},
                        value = "",
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        label = {Text("Days")}
                    )
                    Button(onClick = onDismissRequest) {
                        Text("Confirm")
                    }
                }
            }
        }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DurationDatePicker(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
fun SelectDaysForMedication(onDismiss: () -> Unit){
    var mondayChecked by remember { mutableStateOf(false) }
    var tuesdayChecked by remember { mutableStateOf(false) }
    var wednesdayhecked by remember { mutableStateOf(false) }
    var thursdayChecked by remember { mutableStateOf(false) }
    var fridayChecked by remember { mutableStateOf(false) }
    var saturdayChecked by remember { mutableStateOf(false) }
    var sundayChecked by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
            ) {
                Text("Select days")
                Row() {
                    Text("Monday")
                    Checkbox(
                        checked = mondayChecked,
                        onCheckedChange = { mondayChecked = it }
                    )
                }
                Row() {
                    Text("Tuesday")
                    Checkbox(
                        checked = tuesdayChecked,
                        onCheckedChange = { tuesdayChecked = it }
                    )
                }
                Row() {
                    Text("Wednesday")
                    Checkbox(
                        checked = wednesdayhecked,
                        onCheckedChange = { wednesdayhecked = it }
                    )
                }
                Row() {
                    Text("Thursday")
                    Checkbox(
                        checked = thursdayChecked,
                        onCheckedChange = { thursdayChecked = it }
                    )
                }
                Row() {
                    Text("Friday")
                    Checkbox(
                        checked = fridayChecked,
                        onCheckedChange = {
                            fridayChecked = it
                        }
                    )
                }
                Row() {
                    Text("Saturday")
                    Checkbox(
                        checked = saturdayChecked,
                        onCheckedChange = { saturdayChecked = it }
                    )
                }
                Row() {
                    Text("Sunday")
                    Checkbox(
                        checked = sundayChecked,
                        onCheckedChange = { sundayChecked = it }
                    )
                }
                Row() {
                    Button(onClick = {onDismiss()}) {
                        Text("Ok")
                    }
                }
            }
        }
    }
}