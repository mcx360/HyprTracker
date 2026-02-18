package io.github.mcx360.hyprtracker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import io.github.mcx360.hyprtracker.R

@Composable
fun LoggingScreen(modifier: Modifier = Modifier){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        NavTabRow()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavTabRow() {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
        ) {
        TabRow(selectedTabIndex = selectedTab) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text(text = stringResource(R.string.log_tab)) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text(stringResource(R.string.History_tab)) }
            )
        }


        when (selectedTab) {
            0 -> LogTab()
            1 -> HistoryTab()
        }
    }
}

@Composable
fun LogTab() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(modifier = Modifier) {
            var systolicValue by remember { mutableStateOf("") }
            var diastolicValue by remember { mutableStateOf("") }
            var pulseValue by remember { mutableStateOf("") }

            OutlinedTextField(
                value = systolicValue,
                onValueChange = { systolicValue = it },
                label = { Text("systolic") },
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)

            )
            OutlinedTextField(
                value = diastolicValue,
                onValueChange = { diastolicValue = it },
                label = { Text("diastolic") },
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)

            )
            OutlinedTextField(
                value = pulseValue,
                onValueChange = { pulseValue = it },
                label = { Text(" pulse") },
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            )
        }

        Row(modifier = Modifier) {
            Button(
                onClick = {},
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
            ) {
                Icon(painter = painterResource(R.drawable.ic_date), contentDescription = null)
                Text(" date")
            }
            Button(
                onClick = {},
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)

            ) {
                Icon(painter = painterResource(R.drawable.ic_time), contentDescription = null)
                Text(" Time")
            }
            Button(
                onClick = {},
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Icon(painter = painterResource(R.drawable.ic_notes), contentDescription = null)
                Text(" notes")
            }
        }

        Row(modifier = Modifier) {
            Button(
                onClick = {},
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Icon(painter = painterResource(R.drawable.ic_check), contentDescription = null)
                Text("confirm")
            }
        }
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text("Blood Pressure Stages", fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.CenterHorizontally))
        Column(modifier = Modifier.padding(8.dp)) {
            InfographicLine(Color.LightGray, "Category", "systolic", "", "diastolic")
            Spacer(modifier = Modifier.size(8.dp))
            InfographicLine(Color.Green, "Normal", "<120", "and", "<80")
            Spacer(modifier = Modifier.size(8.dp))
            InfographicLine(Color.Yellow, "Elevated", "120-129", "and", "<80")
            Spacer(modifier = Modifier.size(8.dp))
            InfographicLine(Color.hsl(30f, 1f, 0.5f), "Hypertension stagee 1", "130-139", "or", "80-89")
            Spacer(modifier = Modifier.size(8.dp))
            InfographicLine(Color.hsl(30f, 0.8f, 0.6f),"Hypertension stage 2", "140=<", "or", "90=<")
            Spacer(modifier = Modifier.size(8.dp))
            InfographicLine(Color.Red, "Hypertension crisis", ">180", "and/or", ">120")
        }
    }
}

@Composable
fun InfographicLine(color: Color, bloodPressureCategory: String, systolicValue: String, andOr : String, diastolicValue: String){
        Row(modifier = Modifier.background(color = color)) {
            Text(text = bloodPressureCategory, modifier = Modifier.weight(2f))
            Text(text = systolicValue, modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
            Text(text = andOr, modifier = Modifier.weight(1f))
            Text(text = diastolicValue, modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
        }
}

@Composable
fun HistoryTab(){
    Text("History Screen")
}