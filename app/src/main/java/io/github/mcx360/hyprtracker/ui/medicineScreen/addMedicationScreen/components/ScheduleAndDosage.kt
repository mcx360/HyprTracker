package io.github.mcx360.hyprtracker.ui.medicineScreen.addMedicationScreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.github.mcx360.hyprtracker.ui.utils.InfoDialog
import java.time.DayOfWeek

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationScheduleAndDosageCard(
    modifier: Modifier = Modifier,
    showScheduleDropDownMenu: Boolean,
    showSelectedDaysPicker: Boolean,
    showTimesPerDayDropDownMenu: Boolean,
    showDosePerIntakeInfoDialog: Boolean,
    showTimesPerDayInfoDialog: Boolean,
    showScheduleInfoDialog: Boolean,
    changeScheduleDropDownMenuStatus: (Boolean) -> Unit,
    changeShowTimesPerDayDropDownMenuStatus: (Boolean) -> Unit,
    changeShowSelectedDaysPickerStatus: (Boolean) -> Unit,
    changeShowScheduleInfoDialogStatus: (Boolean) -> Unit,
    changeShowTimesPerDayInfoDialogStatus: (Boolean) -> Unit,
    changeShowDosePerIntakeDialogStatus: (Boolean) -> Unit,
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
    medicationDosage: String,
    ){
    Card {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            //title
            Text(
                text = "Medication Schedule & Dosage",
                style = MaterialTheme.typography.titleMedium,
                modifier = modifier.padding(4.dp),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )

            Row(verticalAlignment = Alignment.CenterVertically) {

                //Box for medication schedule
                ExposedDropdownMenuBox(
                    expanded = showScheduleDropDownMenu,
                    onExpandedChange = { changeScheduleDropDownMenuStatus(true) }) {

                    //Medication schedule field
                    OutlinedTextField(
                        isError = isMedicationScheduleFieldInError,
                        readOnly = true,
                        onValueChange = {},
                        value = medicationSchedule,
                        label = { Text("Schedule*") },
                        placeholder = { Text("e.g. Every day") },
                        trailingIcon = {
                                if (showScheduleDropDownMenu){
                                    Icon(Icons.Default.ArrowDropUp, null)
                                } else{
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                                }
                        },
                        maxLines = 1,
                        modifier = modifier.menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = true),
                    )


                    //Medication schedule menu
                    ExposedDropdownMenu(
                        expanded = showScheduleDropDownMenu,
                        onDismissRequest = { changeScheduleDropDownMenuStatus(false) }) {
                        DropdownMenuItem(
                            onClick = {
                                updateMedicationSchedule("Every single day")
                                setIsMedicationScheduleFieldInErrorToFalse()
                                for (day in DayOfWeek.entries){
                                    addSelectedDay(day.name)
                                }
                                changeScheduleDropDownMenuStatus(false)
                            },
                            text = { Text("Every single day") }
                        )
                        DropdownMenuItem(
                            onClick = {
                                setIsMedicationScheduleFieldInErrorToFalse()
                                changeShowSelectedDaysPickerStatus(true)
                            },
                            text = { Text("On selected days only") }
                        )
                    }
                }

                if (showSelectedDaysPicker){
                    SelectDaysForMedication(
                        onDismiss = {
                            changeShowSelectedDaysPickerStatus(false)
                            if (!it.isNullOrEmpty()) {
                                updateMedicationSchedule(it)
                            }
                        },
                        onDaySelected = { addSelectedDay(it) },
                        onDayRemoved = { removeSelectedDay(it) }
                    )
                    changeScheduleDropDownMenuStatus(false)
                }

                //Schedule info Dialog popup
                IconButton(onClick = {changeShowScheduleInfoDialogStatus(true)}) {
                    Icon(Icons.Default.Info, contentDescription = null)
                }
                if (showScheduleInfoDialog){
                    InfoDialog(
                        onDismissRequest = {changeShowScheduleInfoDialogStatus(false)},
                        info = "Schedule defines how often you take this medication (e.g. every day or on specific days).",
                        title = "Schedule"
                    )
                }
            }

            if (isMedicationScheduleFieldInError){ Text(text ="medication scheduled intake is needed ", color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(start = 12.dp, top = 4.dp), style = MaterialTheme.typography.bodySmall) } else{ Text(text = "*required", modifier = Modifier.padding(start = 12.dp, top = 4.dp), style = MaterialTheme.typography.bodySmall,color = MaterialTheme.colorScheme.onSurfaceVariant) }

            Row(verticalAlignment = Alignment.CenterVertically) {

                //Box for times per day
                ExposedDropdownMenuBox(
                    expanded = showTimesPerDayDropDownMenu,
                    onExpandedChange = {changeShowTimesPerDayDropDownMenuStatus(it)}
                ) {
                    //times per day field
                    OutlinedTextField(
                        isError = isMedicationTimesPerDayFieldInError,
                        readOnly = true,
                        onValueChange = {},
                        value = when(medicationTimesPerDay){
                            0 -> ""
                            1 -> "One time daily"
                            2 -> "Two times daily"
                            3 -> "Three times daily"
                            4 -> "Four times daily"
                            5 -> "Five times daily"
                            6 -> "Six times daily"
                            else -> ""
                        },
                        label = { Text("Times per day*") },
                        placeholder = {Text("e.g. Once daily")},
                        trailingIcon = {
                            if (showTimesPerDayDropDownMenu){
                                Icon(Icons.Default.ArrowDropUp, null)
                            }else{
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            }
                        },
                        modifier = modifier.menuAnchor(
                            type = MenuAnchorType.PrimaryNotEditable,
                            enabled = true
                        ),
                    )

                    //times per day menu
                    ExposedDropdownMenu(
                        expanded = showTimesPerDayDropDownMenu,
                        onDismissRequest = {changeShowTimesPerDayDropDownMenuStatus(false)}
                    ) {
                        //One times per day option
                        DropdownMenuItem(
                            text = {Text("One time daily")},
                            onClick = {
                                setIsMedicationTimesPerDayFieldInErrorToFalse()
                                updateMedicationTimesPerDay(1)
                                changeShowTimesPerDayDropDownMenuStatus(false)
                            }
                        )

                        //Two times per day option
                        DropdownMenuItem(
                            text = {Text("Two times daily")},
                            onClick = {
                                setIsMedicationTimesPerDayFieldInErrorToFalse()
                                updateMedicationTimesPerDay(2)
                                changeShowTimesPerDayDropDownMenuStatus(false)
                            }
                        )

                        //Three times per day option
                        DropdownMenuItem(
                            text = {Text("Three times daily")},
                            onClick = {
                                setIsMedicationTimesPerDayFieldInErrorToFalse()
                                updateMedicationTimesPerDay(3)
                                changeShowTimesPerDayDropDownMenuStatus(false)
                            }
                        )

                        //Four times per day option
                        DropdownMenuItem(
                            text = {Text("Four times daily")},
                            onClick = {
                                setIsMedicationTimesPerDayFieldInErrorToFalse()
                                updateMedicationTimesPerDay(4)
                                changeShowTimesPerDayDropDownMenuStatus(false)
                            }
                        )

                        //Five times per day option
                        DropdownMenuItem(
                            text = {Text("Five times daily")},
                            onClick = {
                                setIsMedicationTimesPerDayFieldInErrorToFalse()
                                updateMedicationTimesPerDay(5)
                                changeShowTimesPerDayDropDownMenuStatus(false)
                            }
                        )

                        //Six times per day option
                        DropdownMenuItem(
                            text = {Text("Six times daily")},
                            onClick = {
                                setIsMedicationTimesPerDayFieldInErrorToFalse()
                                updateMedicationTimesPerDay(6)
                                changeShowTimesPerDayDropDownMenuStatus(false)
                            }
                        )
                    }
                }

                //times per day info dialog popup
                IconButton(onClick = {changeShowTimesPerDayInfoDialogStatus(true)}) {
                    Icon(Icons.Default.Info, contentDescription = null)
                }
                if(showTimesPerDayInfoDialog){
                    InfoDialog(
                        info = "Times per day indicates how many times you take the medication on a scheduled day.",
                        onDismissRequest = {changeShowTimesPerDayInfoDialogStatus(false)},
                        title = "Times Per Day"
                    )
                }
            }

            if (isMedicationTimesPerDayFieldInError){ Text(text = "medication scheduled intake is needed ", color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(start = 12.dp, top = 4.dp), style = MaterialTheme.typography.bodySmall) } else{ Text(text = "*required", modifier = Modifier.padding(start = 12.dp, top = 4.dp), style = MaterialTheme.typography.bodySmall,color = MaterialTheme.colorScheme.onSurfaceVariant) }


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
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    placeholder = {Text("e.g. 1 x 10mg tablet")},
                    supportingText = { if (isMedicationDosePerIntakeInError) Text("Dose per intake is needed")  else Text("*required")  }
                )

                //Dose per intake info popup
                IconButton(onClick = {changeShowDosePerIntakeDialogStatus(true)}) {
                    Icon(Icons.Default.Info, contentDescription = null)
                }

                if (showDosePerIntakeInfoDialog){
                    InfoDialog(
                        info = "Dose per intake describes the amount of medication you take each time (e.g. 1 tablet or 10 mg).",
                        onDismissRequest = {changeShowDosePerIntakeDialogStatus(false) },
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

    Dialog(onDismissRequest = {
        onDismiss(null)
        showWarning.value = false
    }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
            ) {
                Text("Select days")

                //Monday
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = mondayChecked,
                        onCheckedChange = { mondayChecked = it }
                    )
                    Text("Monday")
                }

                //Tuesday
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = tuesdayChecked,
                        onCheckedChange = { tuesdayChecked = it }
                    )
                    Text("Tuesday")
                }

                //Wednesday
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = wednesdayChecked,
                        onCheckedChange = { wednesdayChecked = it }
                    )
                    Text("Wednesday")
                }

                //Thursday
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = thursdayChecked,
                        onCheckedChange = { thursdayChecked = it }
                    )
                    Text("Thursday")
                }

                //Friday
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = fridayChecked,
                        onCheckedChange = { fridayChecked = it }
                    )
                    Text("Friday")
                }

                //Saturday
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = saturdayChecked,
                        onCheckedChange = { saturdayChecked = it }
                    )
                    Text("Saturday")
                }

                //Sunday
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = sundayChecked,
                        onCheckedChange = { sundayChecked = it }
                    )
                    Text("Sunday")
                }

                Row(horizontalArrangement = Arrangement.Center) {
                    //Cancel button
                    Button(
                        onClick = {onDismiss(null) },
                        modifier = Modifier.padding(4.dp)) {
                        Text("Cancel")
                    }

                    //Ok button
                    Button(onClick = {
                        if (mondayChecked || tuesdayChecked || wednesdayChecked || thursdayChecked || fridayChecked || saturdayChecked || sundayChecked){
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
                        } else{ showWarning.value = true } },
                        modifier = Modifier.padding(4.dp)
                    ) {
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