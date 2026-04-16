package io.github.mcx360.hyprtracker.ui.graphScreen

import android.view.Menu
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.mcx360.hyprtracker.R
import io.github.mcx360.hyprtracker.ui.HyprTrackerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GraphScreen(modifier: Modifier = Modifier, hyprTrackerViewModel: HyprTrackerViewModel){
    val uiState by hyprTrackerViewModel.uiState.collectAsState()

    if (uiState.readings.isEmpty()) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.undraw_key_insights),
                contentDescription = null,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
            )
            Text(
                text = stringResource(R.string.Empty_Graph_Screen_Title),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
                modifier = modifier.padding(top = 16.dp)
            )
            Text(
                text = stringResource(R.string.Empty_Graph_Screen_Text),
                textAlign = TextAlign.Center,
                modifier = modifier.padding(16.dp)
            )
        }
    }
    else{
        Column(
            modifier = modifier.fillMaxSize().padding(8.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            val showFilterByDropDownMenu = remember { mutableStateOf(false) }
            Text("Filter by")
            ExposedDropdownMenuBox(
                expanded = showFilterByDropDownMenu.value,
                onExpandedChange = {},
            ) {
                OutlinedTextField(
                    value = "week",
                    readOnly = true,
                    onValueChange = {},
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                showFilterByDropDownMenu.value = !showFilterByDropDownMenu.value
                            }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                    }
                )
                ExposedDropdownMenu(
                    expanded = showFilterByDropDownMenu.value,
                    onDismissRequest = {showFilterByDropDownMenu.value = false},
                ) {
                    DropdownMenuItem(
                        text = {Text("Week")},
                        onClick = {},
                    )
                    DropdownMenuItem(
                        text = {Text("Month")},
                        onClick = {}
                    )
                    DropdownMenuItem(
                        text = {Text("All time")},
                        onClick = {}
                    )
                }
            }
            }
        }
    }

