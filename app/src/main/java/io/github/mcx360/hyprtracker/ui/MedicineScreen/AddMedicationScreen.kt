package io.github.mcx360.hyprtracker.ui.MedicineScreen

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
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import io.github.mcx360.hyprtracker.R
import io.github.mcx360.hyprtracker.ui.HyprTrackerViewModel
import io.github.mcx360.hyprtracker.ui.Utils.DAYS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlin.Unit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedicationScreen(
    modifier: Modifier,
    openAddMedicationScreen: MutableState<Boolean>,
    snackBarHostState: SnackbarHostState,
    scope: CoroutineScope,
    hyprTrackerViewModel: HyprTrackerViewModel
){
    val haptic = LocalHapticFeedback.current
    var checked by remember {mutableStateOf(false)}
    val radioButtons = listOf("Continuous", "Specified number of days", "Until a selected date")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioButtons[0]) }
    var showScheduleDropDownMenu by remember { mutableStateOf(false) }
    var showTimesPerDayDropDownMenu by remember { mutableStateOf(false) }
    var showScheduleInfoDialog by remember { mutableStateOf(false) }
    var showTimesPerDayInfoDialog by remember { mutableStateOf(false) }
    var showDosePerIntakeInfoDialog by remember { mutableStateOf(false) }
    var showSelectSpecifiedNumberOfDaysDialog by remember { mutableStateOf(false) }
    var showDurationDatePicker by remember { mutableStateOf(false) }
    var showSelectedDaysPicker by remember { mutableStateOf(false) }
    val uiState = hyprTrackerViewModel.uiState.collectAsState()

    //Medication Info
    Column(modifier = modifier
        .fillMaxSize()
        .padding(16.dp)
        .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card {
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
                    onValueChange = {hyprTrackerViewModel.updateMedicationName(it)},
                    value = uiState.value.medicationName,
                    label = { Text("Medication name") },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    placeholder = {Text("e.g. Lisinopril")},
                    )

                OutlinedTextField(
                    onValueChange = {hyprTrackerViewModel.updateMedicationDescription(it)},
                    value = uiState.value.medicationDescription,
                    label = { Text("Medication description") },
                    maxLines = 1,
                    placeholder = {Text("e.g. Lowers high blood pressure")},
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    )
                )
            }
        }

        Spacer(modifier = modifier.height(16.dp))

        //Medication schedule and dosage
        Card() {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Medication Schedule & Dosage",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = modifier.padding(4.dp),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )

                Row(verticalAlignment = Alignment.CenterVertically) {

                    ExposedDropdownMenuBox(
                        expanded = showScheduleDropDownMenu,
                        onExpandedChange = { showScheduleDropDownMenu = it }) {
                        OutlinedTextField(
                            readOnly = true,
                            onValueChange = {},
                            value = uiState.value.medicationSchedule,
                            label = { Text("Schedule") },
                            placeholder = { Text("e.g. Every day") },
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        showScheduleDropDownMenu = true
                                    }) {
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                                }
                            },
                            maxLines = 1,
                            modifier = modifier.menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true)
                        )
                        ExposedDropdownMenu(
                            expanded = showScheduleDropDownMenu,
                            onDismissRequest = { showScheduleDropDownMenu = false }) {
                            DropdownMenuItem(
                                onClick = {
                                    hyprTrackerViewModel.updateMedicationSchedule("Every single day")
                                    for (day in DAYS.entries){
                                        hyprTrackerViewModel.addSelectedDays(day.name)
                                    }
                                    showScheduleDropDownMenu = false
                                },
                                text = { Text("Every single day") }
                            )
                            DropdownMenuItem(
                                onClick = {
                                    showSelectedDaysPicker = true
                                },
                                text = { Text("On selected days only") }
                            )
                        }

                    }
                    if (showSelectedDaysPicker){
                        SelectDaysForMedication(onDismiss = {
                            showSelectedDaysPicker = false
                            if (!it.isNullOrEmpty()) {
                                hyprTrackerViewModel.updateMedicationSchedule(it)
                            } },
                            onDaySelected = {
                                hyprTrackerViewModel.addSelectedDays(it)
                            },
                            onDayRemoved = {
                                hyprTrackerViewModel.removeSelectedDays(it)
                            }
                        )
                        showScheduleDropDownMenu = false
                    }
                    IconButton(onClick = {showScheduleInfoDialog = true}) {
                        Icon(Icons.Default.Info, contentDescription = null)
                    }

                    if (showScheduleInfoDialog){
                        InfoDialog(
                            onDismissRequest = {showScheduleInfoDialog = false},
                            info = "Schedule defines how often you take this medication (e.g. every day or on specific days).")
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {

                    ExposedDropdownMenuBox(
                        expanded = showTimesPerDayDropDownMenu,
                        onExpandedChange = {showTimesPerDayDropDownMenu = it}
                    ) {
                        OutlinedTextField(
                            readOnly = true,
                            onValueChange = {},
                            value = when(uiState.value.medicationTimesPerDay){
                                0 -> ""
                                1 -> "One time daily"
                                2 -> "Two times daily"
                                3 -> "Three times daily"
                                4 -> "Four times daily"
                                5 -> "Five times daily"
                                6 -> "Six times daily"
                                else -> ""
                            },
                            label = { Text("Times per day") },
                            placeholder = {Text("e.g. Once daily")},
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        showTimesPerDayDropDownMenu = true
                                    }) {
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                                }
                            },
                            modifier = modifier.menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true)
                        )
                        ExposedDropdownMenu(
                            expanded = showTimesPerDayDropDownMenu,
                            onDismissRequest = {showTimesPerDayDropDownMenu = false}
                        ) {
                            DropdownMenuItem(
                                text = {Text("One time daily")},
                                onClick = {hyprTrackerViewModel.updateMedicationTimesPerDay(1)
                                showTimesPerDayDropDownMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = {Text("Two times daily")},
                                onClick = {hyprTrackerViewModel.updateMedicationTimesPerDay(2)
                                    showTimesPerDayDropDownMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = {Text("Three times daily")},
                                onClick = {hyprTrackerViewModel.updateMedicationTimesPerDay(3)
                                    showTimesPerDayDropDownMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = {Text("Four times daily")},
                                onClick = {hyprTrackerViewModel.updateMedicationTimesPerDay(4)
                                    showTimesPerDayDropDownMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = {Text("Five times daily")},
                                onClick = {hyprTrackerViewModel.updateMedicationTimesPerDay(5)
                                    showTimesPerDayDropDownMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = {Text("Six times daily")},
                                onClick = {hyprTrackerViewModel.updateMedicationTimesPerDay(6)
                                    showTimesPerDayDropDownMenu = false
                                }
                            )
                        }
                    }
                    IconButton(onClick = {showTimesPerDayInfoDialog = true}) {
                        Icon(Icons.Default.Info, contentDescription = null)
                    }

                    if(showTimesPerDayInfoDialog){
                        InfoDialog(
                            info = "Times per day indicates how many times you take the medication on a scheduled day.",
                            onDismissRequest = {showTimesPerDayInfoDialog = false})
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        onValueChange = {hyprTrackerViewModel.updateMedicationDose(it)},
                        value = uiState.value.medicationDosage,
                        label = { Text("Dose per Intake") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        placeholder = {Text("e.g. 1 x 10mg tablet")}
                    )

                    IconButton(onClick = {showDosePerIntakeInfoDialog = true}) {
                        Icon(Icons.Default.Info, contentDescription = null)
                    }

                    if (showDosePerIntakeInfoDialog){
                        InfoDialog(
                            info = "Dose per intake describes the amount of medication you take each time (e.g. 1 tablet or 10 mg).",
                            onDismissRequest = {showDosePerIntakeInfoDialog = false}
                        )
                    }
                }
            }
        }

        Spacer(modifier = modifier.height(16.dp))

        //Notification reminders
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
                    Row {
                        if (uiState.value.medicationSchedule == ""){
                            Text("Enter your medication schedule into the fields above to set reminders", color = MaterialTheme.colorScheme.error,
                                modifier = modifier.padding(8.dp)
                            )
                        }
                        else if (uiState.value.medicationSchedule == "Every single day"){
                            Text("You are scheduled to receive reminders every single day",
                                modifier = modifier.padding(8.dp)
                            )
                        }else{
                            Text("You are scheduled to receive reminders on the following days: " + uiState.value.medicationSelectedDays.toString(),
                                modifier = modifier.padding(8.dp)
                            )
                        }
                    }
                    Row {
                        if (uiState.value.medicationTimesPerDay == 0){
                            Text("Enter how many times per scheduled day you take the medicine in the fields above before setting reminders", color = MaterialTheme.colorScheme.error,
                                modifier = modifier.padding(8.dp)
                                )
                        } else{
                            Text("On each scheduled day you will receive this much reminder(s): " + uiState.value.medicationTimesPerDay,
                                modifier = modifier.padding(8.dp)
                            )
                        }
                        IconButton(onClick = {}) {
                            Image(Icons.Filled.Edit, contentDescription = null)
                        }
                    }
                    Row() {
                        if (uiState.value.medicationTimesPerDay > 0){
                            Text(
                                text = "Enter reminder time(s) below",
                                style = MaterialTheme.typography.titleSmall,
                                modifier = modifier.padding(8.dp),
                            )
                        }
                    }

                    Column() {
                        for (i in 1..uiState.value.medicationTimesPerDay){

                            TextField(
                                value = uiState.value.medicationReminderTimes[i],
                                label = {Text("Reminder" + i)},
                                placeholder = {Text("HH:MM")},
                                onValueChange = {if (it.length <5 && it.isDigitsOnly()) hyprTrackerViewModel.updateMedicationReminderTime(it, i)},
                                trailingIcon = {Icon(
                                    painter = painterResource(R.drawable.ic_date),
                                    contentDescription = null
                                )},
                                keyboardOptions =  KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = if (i != uiState.value.medicationTimesPerDay){
                                        ImeAction.Next
                                    }else{
                                        ImeAction.Done
                                    }
                                ),
                                visualTransformation = VisualTransformation { text ->
                                    var out = ""
                                    for (i in text.indices){
                                        out += if (i == 2) ":${text[i]}" else text[i]

                                    }
                                    TransformedText(
                                        text = AnnotatedString(out),
                                        offsetMapping = object : OffsetMapping {
                                            override fun originalToTransformed(offset: Int): Int {
                                                if (offset<3) return offset
                                                if (offset==3) return offset + 1
                                                if (offset==4) return offset + 1
                                                return offset
                                            }

                                            override fun transformedToOriginal(offset: Int): Int {
                                                if (offset >= 4) return offset -1
                                                return offset
                                            }

                                        }
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = modifier.height(16.dp))

        //Duration
        Card {
            Text(
                text = "Duration",
                modifier = modifier.padding(start = 16.dp, top = 16.dp),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
                )

            Text("Start date: "+ hyprTrackerViewModel.formatToRegularDate(uiState.value.date), modifier = modifier.padding(start = 16.dp))

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
                        })
                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = modifier.padding(start = 16.dp)
                        )
                    }
                }
                if (selectedOption == "Specified number of days"){
                    if (uiState.value.medicationEndDate != ""){
                        Text("medicine recorded until "+hyprTrackerViewModel.formatToRegularDate(uiState.value.medicationEndDate), modifier = modifier.padding(start = 16.dp, bottom = 16.dp))
                    }
                } else if(selectedOption == "Until a selected date"){
                    if (uiState.value.medicationEndDate != ""){
                         Text(hyprTrackerViewModel.formatToRegularDate(uiState.value.medicationEndDate), modifier = modifier.padding(start = 16.dp, bottom = 16.dp))
                    }
                }else{
                    Text("Medicine will be recorded indefinitely unless cancelled by the user", modifier = modifier.padding(start = 16.dp, bottom = 16.dp))
                }
            }
            if (showSelectSpecifiedNumberOfDaysDialog) {
                SelectSpecifiedNumberOfDaysDialog(onDismissRequest = {
                    showSelectSpecifiedNumberOfDaysDialog = false
                }, onNumOfDaysSelected = {
                    if (it != "") {
                        hyprTrackerViewModel.updateMedicationEndDate(LocalDate.now().plusDays(it.toLong()).toString())
                    }
                }
                    )
            }
            if (showDurationDatePicker) {
                DurationDatePicker(onDateSelected = {hyprTrackerViewModel.updateMedicationEndDate(hyprTrackerViewModel.convertMillisToDate(it))}, onDismiss = {showDurationDatePicker = false})
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
                        hyprTrackerViewModel.resetAddMedication()
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
fun SelectSpecifiedNumberOfDaysDialog(
    onDismissRequest: () -> Unit,
    onNumOfDaysSelected: (String) -> Unit
){
    var days by remember { mutableStateOf("") }
        Dialog(onDismissRequest = {onDismissRequest()}) {
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
                    Text("Enter the amount of days that the medicine will be taken for", textAlign = TextAlign.Center)
                    Row(horizontalArrangement = Arrangement.Center) {
                        OutlinedTextField(
                            onValueChange = {if (it.isDigitsOnly()) days = it},
                            value = days,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                            label = {Text("Days")},
                            modifier = Modifier.width(96.dp),
                        )
                    }

                    Button(
                        onClick = {
                            onDismissRequest()
                            onNumOfDaysSelected(days)
                        }
                    ) {
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
            TextButton(onClick = {onDismiss()}) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            dateFormatter = DatePickerDefaults.dateFormatter(),
            showModeToggle = false,
            )
    }
}

@Composable
fun SelectDaysForMedication(
    onDismiss: (String?) -> Unit,
    onDaySelected: (String) -> Unit,
    onDayRemoved: (String) -> Unit
){
    var mondayChecked by remember { mutableStateOf(false) }
    var tuesdayChecked by remember { mutableStateOf(false) }
    var wednesdayChecked by remember { mutableStateOf(false) }
    var thursdayChecked by remember { mutableStateOf(false) }
    var fridayChecked by remember { mutableStateOf(false) }
    var saturdayChecked by remember { mutableStateOf(false) }
    var sundayChecked by remember { mutableStateOf(false) }
    var showWarning = remember { mutableStateOf(false) }

    Dialog(onDismissRequest = {onDismiss(null)
    showWarning.value = false
    }) {
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = mondayChecked,
                        onCheckedChange = { mondayChecked = it }
                    )
                    Text("Monday")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = tuesdayChecked,
                        onCheckedChange = { tuesdayChecked = it }
                    )
                    Text("Tuesday")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = wednesdayChecked,
                        onCheckedChange = { wednesdayChecked = it }
                    )
                    Text("Wednesday")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = thursdayChecked,
                        onCheckedChange = { thursdayChecked = it }
                    )
                    Text("Thursday")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = fridayChecked,
                        onCheckedChange = {
                            fridayChecked = it
                        }
                    )
                    Text("Friday")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = saturdayChecked,
                        onCheckedChange = { saturdayChecked = it }
                    )
                    Text("Saturday")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = sundayChecked,
                        onCheckedChange = { sundayChecked = it }
                    )
                    Text("Sunday")
                }
                Row(horizontalArrangement = Arrangement.Center) {
                    Button(onClick = {onDismiss(null) }, modifier = Modifier.padding(4.dp)) {
                        Text("Cancel")
                    }
                    Button(onClick = {
                        if (mondayChecked || tuesdayChecked || wednesdayChecked || thursdayChecked || fridayChecked || saturdayChecked || sundayChecked){
                        listOf(
                            mondayChecked to DAYS.Monday,
                            tuesdayChecked to DAYS.Tuesday,
                            wednesdayChecked to DAYS.Wednesday,
                            thursdayChecked to DAYS.Thursday,
                            fridayChecked to DAYS.Friday,
                            saturdayChecked to DAYS.Saturday,
                            sundayChecked to DAYS.Sunday
                            ).forEach { (isChecked, day) ->
                            if (isChecked) {
                                onDaySelected(day.name)
                            }
                            if (!isChecked) {
                                onDayRemoved(day.name)
                            }
                        }
                        onDismiss("Selected days only")
                        } else{
                            showWarning.value = true
                        }},
                        modifier = Modifier.padding(4.dp)) {
                        Text("Ok")
                    }
                }
                if (showWarning.value){
                    Text("You must select at least one day!", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}