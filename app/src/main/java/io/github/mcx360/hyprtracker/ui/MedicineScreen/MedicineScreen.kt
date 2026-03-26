package io.github.mcx360.hyprtracker.ui.MedicineScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.mcx360.hyprtracker.R

@Composable
fun MedicineScreen(
    modifier: Modifier = Modifier,
    openAddMedicationScreen: MutableState<Boolean>,
    snackBarHostState: SnackbarHostState
){
    val scope = rememberCoroutineScope()
    if (openAddMedicationScreen.value){
        AddMedicationScreen(
            modifier = modifier,
            openAddMedicationScreen = openAddMedicationScreen,
            snackBarHostState = snackBarHostState,
            scope = scope
        )
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
