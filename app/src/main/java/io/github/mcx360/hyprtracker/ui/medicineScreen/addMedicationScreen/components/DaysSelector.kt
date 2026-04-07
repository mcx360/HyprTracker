package io.github.mcx360.hyprtracker.ui.medicineScreen.addMedicationScreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.github.mcx360.hyprtracker.ui.utils.Days

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
                                mondayChecked to Days.Monday,
                                tuesdayChecked to Days.Tuesday,
                                wednesdayChecked to Days.Wednesday,
                                thursdayChecked to Days.Thursday,
                                fridayChecked to Days.Friday,
                                saturdayChecked to Days.Saturday,
                                sundayChecked to Days.Sunday
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