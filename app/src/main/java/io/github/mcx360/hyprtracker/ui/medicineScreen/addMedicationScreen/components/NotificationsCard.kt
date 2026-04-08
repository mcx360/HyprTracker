package io.github.mcx360.hyprtracker.ui.medicineScreen.addMedicationScreen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import io.github.mcx360.hyprtracker.R

@Composable
fun NotificationsCard(
    modifier: Modifier = Modifier,
    checked: Boolean,
    updateCheckedStatus: (Boolean) -> Unit,
    updateMedicationNotificationStatus: (Boolean) -> Unit,
    updateMedicationReminderTime: (value: String,reminder: Int) -> Unit,
    medicationSchedule: String,
    medicationSelectedDays: Set<String>,
    medicationTimesPerDay: Int,
    medicationReminderTimes: List<String>,
){
    Card {
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
                        updateCheckedStatus(it)
                    }
                )
            }

            if (checked) {
                updateMedicationNotificationStatus(true)
                Row {
                    when (medicationSchedule) {
                        "" -> {
                            Text(
                                "Enter your medication schedule into the fields above to set reminders",
                                color = MaterialTheme.colorScheme.error,
                                modifier = modifier.padding(8.dp)
                            )
                        }
                        "Every single day" -> {
                            Text(
                                "You are scheduled to receive reminders every single day",
                                modifier = modifier.padding(8.dp)
                            )
                        }
                        else -> {
                            Text(
                                "You are scheduled to receive reminders on the following days: $medicationSelectedDays",
                                modifier = modifier.padding(8.dp)
                            )
                        }
                    }
                }
                Row {
                    if (medicationTimesPerDay == 0){
                        Text("Enter how many times per scheduled day you take the medicine in the fields above before setting reminders", color = MaterialTheme.colorScheme.error,
                            modifier = modifier.padding(8.dp)
                        )
                    } else{
                        Text(
                            "On each scheduled day you will receive this much reminder(s): $medicationTimesPerDay",
                            modifier = modifier.padding(8.dp)
                        )
                    }
                    IconButton(onClick = {}) {
                        Image(Icons.Filled.Edit, contentDescription = null)
                    }
                }
                Row {
                    if (medicationTimesPerDay > 0){
                        Text(
                            text = "Enter reminder time(s) below",
                            style = MaterialTheme.typography.titleSmall,
                            modifier = modifier.padding(8.dp),
                        )
                    }
                }

                Column {
                    for (i in 1..medicationTimesPerDay){

                        TextField(
                            value = medicationReminderTimes[i],
                            label = {Text("Reminder$i")},
                            placeholder = {Text("HH:MM")},
                            onValueChange = {if (it.length <5 && it.isDigitsOnly()) updateMedicationReminderTime(it, i)},
                            trailingIcon = {Icon(
                                painter = painterResource(R.drawable.ic_date),
                                contentDescription = null
                            )},
                            keyboardOptions =  KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = if (i != medicationTimesPerDay){
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
            }else{
                updateMedicationNotificationStatus(false)
            }
        }
    }
}