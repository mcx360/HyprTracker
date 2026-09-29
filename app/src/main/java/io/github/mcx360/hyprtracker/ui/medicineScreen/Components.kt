package io.github.mcx360.hyprtracker.ui.medicineScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.compose.ui.window.Dialog
import androidx.core.text.isDigitsOnly
import io.github.mcx360.hyprtracker.R
import io.github.mcx360.hyprtracker.ui.utils.DurationDatePicker
import io.github.mcx360.hyprtracker.ui.utils.InfoDialog
import io.github.mcx360.hyprtracker.ui.utils.convertMillisToDate
import io.github.mcx360.hyprtracker.ui.utils.formatToRegularDate
import java.time.DayOfWeek
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationScheduleAndDosageCard(
    isMedicationScheduleFieldInError: Boolean,
    isMedicationTimesPerDayFieldInError: Boolean,
    isMedicationDosePerIntakeInError: Boolean,
    setIsMedicationScheduleFieldInErrorToFalse: () -> Unit,
    setIsMedicationTimesPerDayFieldInErrorToFalse: () -> Unit,
    setIsMedicationDosePerIntakeInErrorToFalse: () -> Unit,
    updateMedicationSchedule: (String) -> Unit,
    updateMedicationTimesPerDay: (Int) -> Unit,
    updateMedicationDose: (String) -> Unit,
    addSelectedDay: (String) -> Unit,
    removeSelectedDay: (String) -> Unit,
    medicationSchedule: String,
    medicationTimesPerDay: Int,
    medicationDosage: String
){
    Card {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.Start) {
            val showScheduleDropDownMenu = remember { mutableStateOf(false) }
            val showTimesPerDayDropDownMenu = remember { mutableStateOf(false) }
            val showSelectedDaysPicker = remember { mutableStateOf(false) }
            val showScheduleInfoDialog = remember { mutableStateOf(false) }
            val showTimesPerDayInfoDialog = remember { mutableStateOf(false) }
            val showDosePerIntakeInfoDialog = remember { mutableStateOf(false) }
            //title
            Text(
                text = "Medication Schedule & Dosage",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(4.dp),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                //Box for medication schedule
                ExposedDropdownMenuBox(
                    expanded = showScheduleDropDownMenu.value,
                    onExpandedChange = { showScheduleDropDownMenu.value = true }) {

                    //Medication schedule field
                    OutlinedTextField(
                        isError = isMedicationScheduleFieldInError,
                        readOnly = true,
                        onValueChange = {},
                        value = medicationSchedule,
                        label = { Text(text = "Schedule*") },
                        placeholder = { Text(text = "e.g. Every day") },
                        trailingIcon = { Icon(if (showScheduleDropDownMenu.value) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown, contentDescription = null) },
                        maxLines = 1,
                        modifier = Modifier.menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = true),
                    )

                    //Medication schedule menu
                    ExposedDropdownMenu(
                        expanded = showScheduleDropDownMenu.value,
                        onDismissRequest = { showScheduleDropDownMenu.value = false }) {
                        DropdownMenuItem(
                            onClick = {
                                updateMedicationSchedule("Every single day")
                                setIsMedicationScheduleFieldInErrorToFalse()
                                for (day in DayOfWeek.entries) { addSelectedDay(day.name) }
                                showScheduleDropDownMenu.value = false
                            },
                            text = { Text(text = "Every single day") }
                        )
                        DropdownMenuItem(
                            onClick = {
                                setIsMedicationScheduleFieldInErrorToFalse()
                                showScheduleDropDownMenu.value = false
                                showSelectedDaysPicker.value = true
                            },
                            text = { Text(text = "On selected days only") }
                        )
                    }
                }
                when {
                    showSelectedDaysPicker.value ->
                        SelectDaysForMedication(
                            onDismiss = {
                                if (!it.isNullOrEmpty())  updateMedicationSchedule(it)
                                showSelectedDaysPicker.value = false
                            },
                            onDaySelected = { addSelectedDay(it) },
                            onDayRemoved = { removeSelectedDay(it) }
                        )
                }

                //Schedule info Dialog popup
                IconButton(onClick = { showScheduleInfoDialog.value = true }) {
                    Icon(Icons.Default.Info, contentDescription = null)
                }
                if (showScheduleInfoDialog.value) {
                    InfoDialog(
                        onDismissRequest = { showScheduleInfoDialog.value = false },
                        info = "Schedule defines how often you take this medication (e.g. every day or on specific days).",
                        title = "Schedule"
                    )
                }
            }

            if (isMedicationScheduleFieldInError) {
                Text(
                    text = "medication scheduled intake is needed ",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(start = 12.dp, top = 4.dp),
                    style = MaterialTheme.typography.bodySmall
                )
            } else {
                Text(
                    text = "*required",
                    modifier = Modifier.padding(start = 12.dp, top = 4.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {

                //Box for times per day
                ExposedDropdownMenuBox(
                    expanded = showTimesPerDayDropDownMenu.value,
                    onExpandedChange = { showTimesPerDayDropDownMenu.value = it }
                ) {
                    //times per day field
                    OutlinedTextField(
                        isError = isMedicationTimesPerDayFieldInError,
                        readOnly = true,
                        onValueChange = {},
                        value = when (medicationTimesPerDay) {
                            0 -> ""
                            1 -> "One time daily"
                            2 -> "Two times daily"
                            3 -> "Three times daily"
                            4 -> "Four times daily"
                            5 -> "Five times daily"
                            6 -> "Six times daily"
                            else -> ""
                        },
                        label = { Text(text = "Times per day*") },
                        placeholder = { Text(text = "e.g. Once daily") },
                        trailingIcon = { Icon(if (showTimesPerDayDropDownMenu.value) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown , null) },
                        modifier = Modifier.menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true),
                    )

                    //times per day menu
                    ExposedDropdownMenu(
                        expanded = showTimesPerDayDropDownMenu.value,
                        onDismissRequest = { showTimesPerDayDropDownMenu.value = false }
                    ) {
                        for (i in 1..6) {
                            DropdownMenuItem(
                                text = { Text("$i time(s) daily") },
                                onClick = {
                                    setIsMedicationTimesPerDayFieldInErrorToFalse()
                                    updateMedicationTimesPerDay(i)
                                    showTimesPerDayDropDownMenu.value = false
                                }
                            )
                        }
                    }
                }

                //times per day info dialog popup
                IconButton(onClick = { showTimesPerDayInfoDialog.value = true }) {
                    Icon(Icons.Default.Info, contentDescription = null)
                }
                if (showTimesPerDayInfoDialog.value) {
                    InfoDialog(
                        info = "Times per day indicates how many times you take the medication on a scheduled day.",
                        onDismissRequest = { showTimesPerDayInfoDialog.value = false },
                        title = "Times Per Day"
                    )
                }
            }

            if (isMedicationTimesPerDayFieldInError) {
                Text(
                    text = "medication scheduled intake is needed ",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(start = 12.dp, top = 4.dp),
                    style = MaterialTheme.typography.bodySmall
                )
            } else {
                Text(
                    text = "*required",
                    modifier = Modifier.padding(start = 12.dp, top = 4.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }


            Row(verticalAlignment = Alignment.CenterVertically) {
                //Dose per intake
                OutlinedTextField(
                    singleLine = true,
                    isError = isMedicationDosePerIntakeInError,
                    onValueChange = {
                        updateMedicationDose(it)
                        if (medicationDosage.isNotEmpty()) setIsMedicationDosePerIntakeInErrorToFalse()
                    },
                    value = medicationDosage,
                    label = { Text("Dose per Intake*") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
                    placeholder = { Text("e.g. 1 x 10mg tablet") },
                    supportingText = { Text(text = if (isMedicationDosePerIntakeInError) "Dose per intake is needed" else "*required") }
                )

                //Dose per intake info popup
                IconButton(onClick = { showDosePerIntakeInfoDialog.value = true }) {
                    Icon(Icons.Default.Info, contentDescription = null)
                }

                if (showDosePerIntakeInfoDialog.value) {
                    InfoDialog(
                        info = "Dose per intake describes the amount of medication you take each time (e.g. 1 tablet or 10 mg).",
                        onDismissRequest = { showDosePerIntakeInfoDialog.value = false },
                        title = "Dose Per Intake"
                    )
                }
            }
        }
    }
}

//Dialog that lets users on which days of the week the medication is taken, e.g. only on Monday,Thursday and Sunday
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
    val showWarning = remember { mutableStateOf(false) }

    Dialog(onDismissRequest = { onDismiss(null); showWarning.value = false }) {
        Card(modifier = Modifier.fillMaxWidth().padding(16.dp), shape = RoundedCornerShape(16.dp),) {
            Column(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalAlignment = Alignment.Start,) {
                Text(text = "Select days")

                //Monday
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = mondayChecked, onCheckedChange = { mondayChecked = it })
                    Text("Monday")
                }

                //Tuesday
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = tuesdayChecked, onCheckedChange = { tuesdayChecked = it })
                    Text("Tuesday")
                }

                //Wednesday
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = wednesdayChecked, onCheckedChange = { wednesdayChecked = it })
                    Text("Wednesday")
                }

                //Thursday
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = thursdayChecked, onCheckedChange = { thursdayChecked = it })
                    Text("Thursday")
                }

                //Friday
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = fridayChecked, onCheckedChange = { fridayChecked = it })
                    Text("Friday")
                }

                //Saturday
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = saturdayChecked, onCheckedChange = { saturdayChecked = it })
                    Text("Saturday")
                }

                //Sunday
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = sundayChecked, onCheckedChange = { sundayChecked = it })
                    Text("Sunday")
                }

                Row(horizontalArrangement = Arrangement.Center) {
                    //Cancel button
                    Button(onClick = { onDismiss(null) }, modifier = Modifier.padding(4.dp)) {
                        Text("Cancel")
                    }

                    //Ok button
                    Button(
                        onClick = {
                            if (mondayChecked || tuesdayChecked || wednesdayChecked || thursdayChecked || fridayChecked || saturdayChecked || sundayChecked) {
                                listOf(
                                    mondayChecked to DayOfWeek.MONDAY,
                                    tuesdayChecked to DayOfWeek.TUESDAY,
                                    wednesdayChecked to DayOfWeek.WEDNESDAY,
                                    thursdayChecked to DayOfWeek.THURSDAY,
                                    fridayChecked to DayOfWeek.FRIDAY,
                                    saturdayChecked to DayOfWeek.SATURDAY,
                                    sundayChecked to DayOfWeek.SUNDAY
                                ).forEach { (isChecked, day) ->
                                    if (isChecked) { onDaySelected(day.name) }
                                    if (!isChecked) { onDayRemoved(day.name) }
                                }
                                onDismiss("Selected days only")
                            } else {
                                showWarning.value = true
                            }
                        },
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Text(text = "Ok")
                    }
                }
                if (showWarning.value) {
                    Text(
                        text = "You must select at least one day!",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
fun DurationCard(
    startDate: String,
    endDate: String,
    updateMedicationEndDate: (String) -> Unit,
){
    val radioButtons = listOf("Continuous", "Specified number of days", "Until a selected date")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioButtons[0]) }
    val showSelectSpecifiedNumberOfDaysDialog = remember { mutableStateOf(false) }
    val showDurationDatePicker = remember { mutableStateOf(false) }

    Card {
        Text(
            text = "Duration",
            modifier = Modifier.padding(start = 16.dp, top = 16.dp),
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Start date: " + formatToRegularDate(startDate),
            modifier = Modifier.padding(start = 16.dp)
        )

        Column(Modifier.selectableGroup()) {
            radioButtons.forEach { text ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .selectable(
                            selected = (text == selectedOption),
                            onClick = {
                                onOptionSelected(text)
                                when (text) {
                                    "Specified number of days" -> showSelectSpecifiedNumberOfDaysDialog.value = true
                                    "Until a selected date" -> showDurationDatePicker.value = true
                                }
                            },
                            role = Role.RadioButton
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = (text == selectedOption), onClick = {
                        onOptionSelected(text)
                        when (text) {
                            "Specified number of days" -> showSelectSpecifiedNumberOfDaysDialog.value = true
                            "Until a selected date" -> showDurationDatePicker.value = true
                        }
                    })
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
            when (selectedOption) {
                "Specified number of days" if endDate != "" -> { Text(text = "medicine recorded until " + formatToRegularDate(endDate), modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)) }
                "Until a selected date" if endDate != "" -> { Text(text = formatToRegularDate(endDate), modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)) }
                else -> { Text(text = "Medicine will be recorded indefinitely unless cancelled by the user", modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)) }
            }
        }

        when {
            showSelectSpecifiedNumberOfDaysDialog.value ->
                SelectSpecifiedNumberOfDaysDialog(
                    onDismissRequest = { showSelectSpecifiedNumberOfDaysDialog.value = false },
                    onNumOfDaysSelected = { if (it != "") updateMedicationEndDate(LocalDate.now().plusDays(it.toLong()).toString()) }
                )
        }

        when {
            showDurationDatePicker.value ->
                DurationDatePicker(
                    onDateSelected = { updateMedicationEndDate(convertMillisToDate(it)) },
                    onDismiss = { showDurationDatePicker.value = false }
                )
        }
    }
}

@Composable
fun SelectSpecifiedNumberOfDaysDialog(
    onDismissRequest: () -> Unit,
    onNumOfDaysSelected: (String) -> Unit
){
    var days by remember { mutableStateOf("") }
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(shape = RoundedCornerShape(16.dp),) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Enter the amount of days that the medicine will be taken for",
                    textAlign = TextAlign.Center
                )
                Row(horizontalArrangement = Arrangement.Center) {
                    OutlinedTextField(
                        onValueChange = { if (it.isDigitsOnly()) days = it },
                        value = days,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                        label = { Text(text = "Days") },
                        modifier = Modifier.width(96.dp),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(onClick = { onDismissRequest() }, modifier = Modifier.padding(8.dp)) {
                        Text(text = "Cancel")
                    }
                    Button(onClick = { onDismissRequest(); onNumOfDaysSelected(days) }) {
                        Text(text = "Confirm")
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationsCard(
    updateMedicationNotificationStatus: (Boolean) -> Unit,
    updateMedicationReminderTime: (value: String, reminder: Int) -> Unit,
    medicationSchedule: String,
    medicationSelectedDays: Set<String>,
    medicationTimesPerDay: Int,
    medicationReminderTimes: List<String>,
){
    val checked = remember { mutableStateOf(false) }
    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Notification Reminders",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(4.dp).weight(0.8f),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )

                Switch(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    checked = checked.value,
                    onCheckedChange = { checked.value = true }
                )
            }

            if (checked.value) {
                updateMedicationNotificationStatus(true)
                Row {
                    when (medicationSchedule) {
                        "" -> {
                            Text(
                                text = "Enter your medication schedule into the fields above to set reminders",
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(8.dp)
                            )
                        }

                        "Every single day" -> {
                            Text(
                                text = "You are scheduled to receive reminders every single day",
                                modifier = Modifier.padding(8.dp)
                            )
                        }

                        else -> {
                            Text(
                                text = "You are scheduled to receive reminders on the following days: $medicationSelectedDays",
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }

                Row {
                    if (medicationTimesPerDay == 0) {
                        Text(
                            text = "Enter how many times per scheduled day you take the medicine in the fields above before setting reminders",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(8.dp)
                        )
                    } else {
                        Text(
                            text = "On each scheduled day you will receive this much reminder(s): $medicationTimesPerDay",
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    IconButton(onClick = {}) {
                        Image(Icons.Filled.Edit, contentDescription = null)
                    }
                }

                Row {
                    if (medicationTimesPerDay > 0) {
                        Text(
                            text = "Enter reminder time(s) below",
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.padding(8.dp),
                        )
                    }
                }

                Column {
                    for (i in 1..medicationTimesPerDay) {
                        TextField(
                            value = medicationReminderTimes[i],
                            label = { Text("Reminder$i") },
                            placeholder = { Text("HH:MM") },
                            onValueChange = { if (it.length < 5 && it.isDigitsOnly()) updateMedicationReminderTime(it, i) },
                            trailingIcon = { Icon(painter = painterResource(R.drawable.ic_date), contentDescription = null) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = if (i != medicationTimesPerDay) ImeAction.Next else ImeAction.Done),
                            visualTransformation = VisualTransformation { text ->
                                var out = ""
                                for (i in text.indices) {
                                    out += if (i == 2) ":${text[i]}" else text[i]
                                }
                                TransformedText(
                                    text = AnnotatedString(out),
                                    offsetMapping = object : OffsetMapping {
                                        override fun originalToTransformed(offset: Int): Int {
                                            if (offset < 3) return offset
                                            if (offset == 3) return offset + 1
                                            if (offset == 4) return offset + 1
                                            return offset
                                        }

                                        override fun transformedToOriginal(offset: Int): Int {
                                            if (offset >= 4) return offset - 1
                                            return offset
                                        }
                                    }
                                )
                            }
                        )
                    }
                }
            } else {
                updateMedicationNotificationStatus(false)
            }
        }
    }
}