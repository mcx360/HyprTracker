package io.github.mcx360.hyprtracker.ui.medicineScreen.addMedicationScreen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun medicationInfoCard(
    modifier : Modifier = Modifier,
    medicationName: String,
    medicationDescription: String,
    isMedicationNameFieldInError: Boolean,
    isMedicationDescriptionFieldInError: Boolean,
    updateMedicationName : (String) -> Unit,
    updateMedicationDescription : (String) -> Unit,
    setMedicationNameErrorStatusTrue : () -> Unit,
    setMedicationDescriptionErrorStatusTrue : () -> Unit
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
                isError = isMedicationNameFieldInError,
                onValueChange = {
                    updateMedicationName(it)
                    if (medicationName.isNotEmpty()) setMedicationNameErrorStatusTrue
                },
                value = medicationName,
                label = { Text("Medication name*") },
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                placeholder = {Text("e.g. Lisinopril")},
                supportingText = {
                    if (isMedicationNameFieldInError){
                        Text("Medication name needed!", color = MaterialTheme.colorScheme.error)}
                    else{
                        Text("*required")
                    }
                }
            )

            OutlinedTextField(
                isError = isMedicationDescriptionFieldInError,
                onValueChange = {
                    updateMedicationDescription(it)
                    if (medicationDescription.isNotEmpty()) setMedicationDescriptionErrorStatusTrue
                },
                value = medicationDescription,
                label = { Text("Medication description*") },
                maxLines = 1,
                placeholder = {Text("e.g. Lowers high blood pressure")},
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                supportingText = {
                    if (isMedicationDescriptionFieldInError){
                        Text("Medication description needed!", color = MaterialTheme.colorScheme.error)}
                    else{
                        Text("*required")
                    }
                }
            )
        }
    }
}