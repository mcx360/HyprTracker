package io.github.mcx360.hyprtracker.ui.medicineScreen.addMedicationScreen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@Composable
fun DurationCard(
    modifier: Modifier = Modifier,
    formatToRegularDate: (String) -> String,
    startDate: String,
    endDate: String,
    showSelectSpecifiedNumberOfDaysDialog: Boolean,
    updateShowSelectSpecifiedNumberOfDaysDialog: (Boolean) -> Unit,
    showDurationDatePicker: Boolean,
    updateShowDurationDatePicker: (Boolean) -> Unit,
    updateMedicationEndDateLong: (Long?) -> Unit,
    updateMedicationEndDateString: (String) -> Unit,
){
    val radioButtons = listOf("Continuous", "Specified number of days", "Until a selected date")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioButtons[0]) }

    Card {
        Text(
            text = "Duration",
            modifier = modifier.padding(start = 16.dp, top = 16.dp),
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Text("Start date: "+ formatToRegularDate(startDate), modifier = modifier.padding(start = 16.dp))

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
                                    "Specified number of days" -> updateShowSelectSpecifiedNumberOfDaysDialog(true)
                                    "Until a selected date" -> updateShowDurationDatePicker(true)
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
                            "Specified number of days" -> updateShowSelectSpecifiedNumberOfDaysDialog(true)
                            "Until a selected date" -> updateShowDurationDatePicker(true)
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
                if (endDate != ""){
                    Text(
                        text = "medicine recorded until "+formatToRegularDate(endDate),
                        modifier = modifier.padding(start = 16.dp, bottom = 16.dp)
                    )
                }
            } else if(selectedOption == "Until a selected date"){
                if (endDate != ""){
                    Text(
                        text = ""+formatToRegularDate(endDate),
                        modifier = modifier.padding(start = 16.dp, bottom = 16.dp)
                    )
                }
            }else{
                Text(
                    text = "Medicine will be recorded indefinitely unless cancelled by the user",
                    modifier = modifier.padding(start = 16.dp, bottom = 16.dp)
                )
            }
        }

        if (showSelectSpecifiedNumberOfDaysDialog) {
            SelectSpecifiedNumberOfDaysDialog(
                onDismissRequest = { updateShowSelectSpecifiedNumberOfDaysDialog(false) },
                onNumOfDaysSelected = { if (it != "")  updateMedicationEndDateString(LocalDate.now().plusDays(it.toLong()).toString()) }
            )
        }

        if (showDurationDatePicker) {
            DurationDatePicker(
                onDateSelected = {updateMedicationEndDateLong(it)},
                onDismiss = {updateShowDurationDatePicker(false)})
        }
    }
}