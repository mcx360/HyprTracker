package io.github.mcx360.hyprtracker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
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
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
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
                label = { Text(text = stringResource(R.string.systolic)) },
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
                label = { Text(text = stringResource(R.string.diastolic)) },
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
                label = { Text(text = stringResource(R.string.pulse)) },
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
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
        ) {
        Column(
            modifier = Modifier.padding(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(stringResource(R.string.BP_stages_title),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            InfographicLine(MaterialTheme.colorScheme.surfaceContainerHighest, stringArrayResource(R.array.hypertension_subheading),)
            InfographicLine(Color(0xFF2E7D32), stringArrayResource(R.array.hypertension_stage_normal))
            InfographicLine(Color(0xFFF9A825), stringArrayResource(R.array.hypertension_stage_elevated))
            InfographicLine(Color(0xFFF57C00), stringArrayResource(R.array.hypertension_stage_one))
            InfographicLine(Color(0xFFD32F2F),stringArrayResource(R.array.hypertension_stage_two))
            InfographicLine(Color(0xFFB71C1C), stringArrayResource(R.array.hypertension_stage_crisis))
        }
    }
}

@Composable
fun InfographicLine(
    color: Color,
    stage: Array<String>
    ) {

    val textColor = if(color.luminance() > 0.5f) Color.Black else Color.White

        Row(modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 4.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(color = color),
            )
        {
            Text(
                text = stage[0],
                modifier = Modifier
                    .weight(2f)
                    .padding(4.dp),
                color = textColor
            )
            Text(text = stage[1],
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp),
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            Text(
                text = stage[2],
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp),
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }
}

@Composable
fun HistoryTab(){
    Text("History Screen")
}