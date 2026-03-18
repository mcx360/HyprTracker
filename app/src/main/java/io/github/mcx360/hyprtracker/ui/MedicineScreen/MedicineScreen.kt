package io.github.mcx360.hyprtracker.ui.MedicineScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.mcx360.hyprtracker.R

@Composable
fun MedicineScreen(
    modifier: Modifier = Modifier,
    openAddMedicationScreen: MutableState<Boolean>
){
    if (openAddMedicationScreen.value){
        Column(modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card() {
                Column(modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8 .dp)) {
                    Text("Add Medication", textAlign = TextAlign.Left)
                }
            }
            Button(onClick = {openAddMedicationScreen.value = !openAddMedicationScreen.value}) {
                Text("Add medication")
            }
        }

    } else{
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.empty_medicine_screen_image),
                contentDescription = null,
                modifier = modifier
                    .fillMaxWidth()
            )
            Text(
                text = stringResource(R.string.Empty_Medicine_Screen_Title),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
                modifier = modifier.padding(top = 16.dp)
            )
            Text(
                text = stringResource(R.string.Empty_Medicine_Screen_Text),
                textAlign = TextAlign.Center,
                modifier = modifier.padding(all = 16.dp))
        }
    }
}
