package io.github.mcx360.hyprtracker.ui.medicineScreen.addMedicationScreen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Info
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import io.github.mcx360.hyprtracker.ui.utils.Days
import io.github.mcx360.hyprtracker.ui.utils.InfoDialog

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
                    onExpandedChange = { changeScheduleDropDownMenuStatus(true) }) {
                    OutlinedTextField(
                        isError = isMedicationScheduleFieldInError,
                        readOnly = true,
                        onValueChange = {},
                        value = medicationSchedule,
                        label = { Text("Schedule*") },
                        placeholder = { Text("e.g. Every day") },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    changeScheduleDropDownMenuStatus(true)
                                }) {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            }
                        },
                        maxLines = 1,
                        modifier = modifier.menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true),
                        supportingText = {
                            if (isMedicationScheduleFieldInError){
                                Text("medication scheduled intake is needed ", color = MaterialTheme.colorScheme.error)
                            } else{
                                Text("*required")
                            }
                        }
                    )
                    ExposedDropdownMenu(
                        expanded = showScheduleDropDownMenu,
                        onDismissRequest = { changeScheduleDropDownMenuStatus(false) }) {
                        DropdownMenuItem(
                            onClick = {
                                updateMedicationSchedule("Every single day")
                                setIsMedicationScheduleFieldInErrorToFalse()
                                for (day in Days.entries){
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
                    SelectDaysForMedication(onDismiss = {
                        changeShowSelectedDaysPickerStatus(false)
                        if (!it.isNullOrEmpty()) {
                            updateMedicationSchedule(it)
                        } },
                        onDaySelected = {
                            addSelectedDay(it)
                        },
                        onDayRemoved = {
                            removeSelectedDay(it)
                        }
                    )
                    changeScheduleDropDownMenuStatus(false)
                }
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

            Row(verticalAlignment = Alignment.CenterVertically) {

                ExposedDropdownMenuBox(
                    expanded = showTimesPerDayDropDownMenu,
                    onExpandedChange = {changeShowTimesPerDayDropDownMenuStatus(it)}
                ) {
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
                            IconButton(
                                onClick = {
                                    changeShowTimesPerDayDropDownMenuStatus(true)
                                }) {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            }
                        },
                        modifier = modifier.menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true),
                        supportingText = {
                            if (isMedicationTimesPerDayFieldInError){
                                Text("Enter amount of time per day!")
                            } else{
                                Text("*required")
                            }
                        }
                    )
                    ExposedDropdownMenu(
                        expanded = showTimesPerDayDropDownMenu,
                        onDismissRequest = {changeShowTimesPerDayDropDownMenuStatus(false)}
                    ) {
                        DropdownMenuItem(
                            text = {Text("One time daily")},
                            onClick = {
                                setIsMedicationTimesPerDayFieldInErrorToFalse()
                                updateMedicationTimesPerDay(1)
                                changeShowTimesPerDayDropDownMenuStatus(false)
                            }
                        )
                        DropdownMenuItem(
                            text = {Text("Two times daily")},
                            onClick = {
                                setIsMedicationTimesPerDayFieldInErrorToFalse()
                                updateMedicationTimesPerDay(2)
                                changeShowTimesPerDayDropDownMenuStatus(false)
                            }
                        )
                        DropdownMenuItem(
                            text = {Text("Three times daily")},
                            onClick = {
                                setIsMedicationTimesPerDayFieldInErrorToFalse()
                                updateMedicationTimesPerDay(3)
                                changeShowTimesPerDayDropDownMenuStatus(false)
                            }
                        )
                        DropdownMenuItem(
                            text = {Text("Four times daily")},
                            onClick = {
                                setIsMedicationTimesPerDayFieldInErrorToFalse()
                                updateMedicationTimesPerDay(4)
                                changeShowTimesPerDayDropDownMenuStatus(false)
                            }
                        )
                        DropdownMenuItem(
                            text = {Text("Five times daily")},
                            onClick = {
                                setIsMedicationTimesPerDayFieldInErrorToFalse()
                                updateMedicationTimesPerDay(5)
                                changeShowTimesPerDayDropDownMenuStatus(false)
                            }
                        )
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

            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    isError = isMedicationDosePerIntakeInError,
                    onValueChange = {
                        updateMedicationDose(it)
                        if (medicationDosage.isNotEmpty()) setIsMedicationDosePerIntakeInErrorToFalse()
                    },
                    value = medicationDosage,
                    label = { Text("Dose per Intake") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    placeholder = {Text("e.g. 1 x 10mg tablet")},
                    supportingText = {
                        if (isMedicationDosePerIntakeInError){
                            Text("Dose per intake is needed")
                        } else{
                            Text("*required")
                        }
                    }
                )

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