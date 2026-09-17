package io.github.mcx360.hyprtracker.ui.medicineScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.mcx360.hyprtracker.R
import io.github.mcx360.hyprtracker.ui.medicineScreen.addMedicationScreen.AddMedicationScreen
import io.github.mcx360.hyprtracker.ui.utils.DeletionDialog
import io.github.mcx360.hyprtracker.ui.utils.Dot
import io.github.mcx360.hyprtracker.ui.utils.EmptyScreen
import io.github.mcx360.hyprtracker.ui.utils.formatToRegularDate
import kotlinx.coroutines.launch

@Composable
fun MedicineScreen(
    modifier: Modifier = Modifier,
    openAddMedicationScreen: MutableState<Boolean>,
    snackBarHostState: SnackbarHostState,
    medicineViewModel: MedicineViewModel
){
    val  scope = rememberCoroutineScope()
    val uiState = medicineViewModel.uiState.collectAsState()
    val showDeleteConfirmationDialog = remember { mutableStateOf(false) }

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
                .background(color = MaterialTheme.colorScheme.surface)
        ){
            items(uiState.value.medicineList.size){ index ->
                val medication = uiState.value.medicineList[index]
                //individual medication card
                OutlinedCard(modifier = Modifier.padding(bottom = 8.dp, top = 8.dp)) {

                    val showExtrasMenu = remember { mutableStateOf(false) }

                    when{
                        //Dialog to confirm deletion of Medicine
                        showDeleteConfirmationDialog.value -> {
                            DeletionDialog(
                                onDismissRequest = {showDeleteConfirmationDialog.value = false},
                                onDeleteRequest = {showDeleteConfirmationDialog.value = false
                                    scope.launch {
                                        medicineViewModel.removeMedication(medication)
                                        snackBarHostState.showSnackbar("Medicine removed")
                                    }},
                                deletionText = "Are you sure you want to delete this medication?"
                            )
                        }
                    }

                    Column(modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainerHigh)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Column(horizontalAlignment = Alignment.Start, modifier = Modifier.weight(0.8f)) {
                                //Medication name and icon
                                Row {
                                    Text(
                                        medication.name,
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleLarge,
                                    )
                                    Icon(
                                        painter = painterResource(R.drawable.ic_medicine),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.secondary
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
                                modifier = Modifier.fillMaxWidth().weight(0.2f)
                            ) {
                                Box {
                                FilledTonalIconButton(onClick = { showExtrasMenu.value = !showExtrasMenu.value }) {
                                    Icon(
                                        Icons.Filled.MoreHoriz,
                                        contentDescription = null
                                    )
                                }
                                    DropdownMenu(
                                        expanded = showExtrasMenu.value,
                                        onDismissRequest = {showExtrasMenu.value = false}
                                    ) {
                                        DropdownMenuItem(
                                            text = { Text("Delete") },
                                            onClick = {
                                                showDeleteConfirmationDialog.value = true
                                                showExtrasMenu.value = false

                                                      },
                                            leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null) },
                                        )
                                    }
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
                                Text("${formatToRegularDate(medication.startDate)} • Continuous")
                            } else {
                                Text("Duration: ", fontWeight = FontWeight.Bold)
                                Text("${formatToRegularDate(medication.startDate)} ➩ ${formatToRegularDate(medication.endDate)}")
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
                                    text = "Taken at: ",
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
                            Dot(MaterialTheme.colorScheme.secondary)
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
                                    Text("Once")
                                } else {
                                    Text("${medication.timesPerDay} times")
                                }
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
        EmptyScreen(
            painter = painterResource(R.drawable.empty_medicine_screen_image),
            heading = stringResource(R.string.Empty_Medicine_Screen_Title),
            subHeading = stringResource(R.string.Empty_Medicine_Screen_Text)
        )
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