package io.github.mcx360.hyprtracker.ui.medicineScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.mcx360.hyprtracker.R
import io.github.mcx360.hyprtracker.ui.medicineScreen.addMedicationScreen.AddMedicationScreen
import io.github.mcx360.hyprtracker.ui.medicineScreen.components.EmptyMedicineList
import io.github.mcx360.hyprtracker.ui.utils.DotWithColour

@Composable
fun MedicineScreen(
    modifier: Modifier = Modifier,
    openAddMedicationScreen: MutableState<Boolean>,
    snackBarHostState: SnackbarHostState,
    medicineViewModel: MedicineViewModel
){
    val  scope = rememberCoroutineScope()
    val uiState = medicineViewModel.uiState.collectAsState()

    if (uiState.value.medicineList.isNotEmpty()){

        when{
            openAddMedicationScreen.value -> AddMedicationScreen(
                modifier = modifier,
                openAddMedicationScreen = openAddMedicationScreen,
                snackBarHostState = snackBarHostState,
                scope = scope,
                medicineViewModel = medicineViewModel
            )
        }

        //list of all medications recorded
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize()
                .padding()
        ){
            items(uiState.value.medicineList.size){ index ->
                val medication = uiState.value.medicineList[index]
                //individual medication card
                Card(modifier = Modifier.padding(bottom = 8.dp, top = 8.dp)) {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Column(horizontalAlignment = Alignment.Start) {
                                //Medication name and icon
                                Row {
                                    Text(
                                        medication.name,
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleLarge,
                                    )
                                    Icon(
                                        painter = painterResource(R.drawable.ic_medicine),
                                        contentDescription = null
                                    )
                                }

                                //Medication description
                                Text(
                                    medication.description,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            //extras menu
                            Column(
                                horizontalAlignment = Alignment.End,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                IconButton(onClick = {}) {
                                    Icon(
                                        Icons.Filled.MoreVert,
                                        contentDescription = null
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.padding(4.dp))

                        //Medication duration
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            if (medication.endDate.isEmpty()) {
                                Text("Started: ", fontWeight = FontWeight.Bold)
                                Text("${medicineViewModel.formatToRegularDate(medication.startDate)} • Continuous")
                            } else {
                                Text("Duration: ", fontWeight = FontWeight.Bold)
                                Text("${medicineViewModel.formatToRegularDate(medication.startDate)} ➩ ${medicineViewModel.formatToRegularDate(medication.endDate)}")
                            }
                        }

                        //medication dosage
                        Row(horizontalArrangement = Arrangement.Start,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Text("Dose: ", fontWeight = FontWeight.Bold)
                            Text(medication.dosePerIntake)
                        }

                        //notification times
                        if (medication.notificationsEnabled) {
                            Row(
                                horizontalArrangement = Arrangement.Start,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = "Times: ",
                                    fontWeight = FontWeight.Bold
                                )

                                var text = ""
                                medication.scheduledNotificationsTime.forEach {
                                    if (it.length > 4) {
                                        text += it.removePrefix(" ").replaceRange(2, 2, ":") + ", "
                                    }
                                }
                                Text(text.removeSuffix(", "))
                            }
                        }

                        //frequency of intake
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            DotWithColour(MaterialTheme.colorScheme.secondary)
                            Spacer(modifier = modifier.padding(4.dp))

                            if (medication.schedule == "Every single day"){
                                Text("Taken daily • ")
                                when(medication.timesPerDay){
                                    1 -> Text("Once")
                                    else -> Text("${medication.timesPerDay} times")
                                }
                            } else{
                                Text("Taken on selected days • ")
                                if (medication.timesPerDay == 1){
                                    Text("${medication.timesPerDay} time")
                                }
                                Text("${medication.timesPerDay} times")
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.padding(start = 8.dp, end =8.dp))
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.Start
                        ) {

                        }
                    }
                }
            }
        }
    }
    else {
        EmptyMedicineList()
        when{
            openAddMedicationScreen.value -> AddMedicationScreen(
                modifier = modifier,
                openAddMedicationScreen = openAddMedicationScreen,
                snackBarHostState = snackBarHostState,
                scope = scope,
                medicineViewModel = medicineViewModel
            )
        }
    }
}