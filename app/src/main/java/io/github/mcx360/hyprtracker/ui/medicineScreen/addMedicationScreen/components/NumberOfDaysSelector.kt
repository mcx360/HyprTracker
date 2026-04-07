package io.github.mcx360.hyprtracker.ui.medicineScreen.addMedicationScreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.text.isDigitsOnly

// Dialog that chooses the number of days a medication will be taken for e.g. 30 days
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

                Button(
                    onClick = {
                        onDismissRequest()
                    }
                ) {
                    Text("Cancel")
                }
            }
        }
    }
}