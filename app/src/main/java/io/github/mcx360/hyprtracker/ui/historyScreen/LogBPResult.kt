package io.github.mcx360.hyprtracker.ui.historyScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import io.github.mcx360.hyprtracker.R
import io.github.mcx360.hyprtracker.ui.utils.TitleBarWithBackButton
import io.github.mcx360.hyprtracker.ui.utils.formatToRegularDate

@Composable
fun LogBPResult(
    onDismissRequest: () -> Unit
){
    Dialog(
        onDismissRequest = {onDismissRequest()},
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Card(
            modifier = Modifier.fillMaxSize().padding(bottom = 8.dp),
            colors = CardColors(
                contentColor = MaterialTheme.colorScheme.surfaceContainer,
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                disabledContentColor = MaterialTheme.colorScheme.surfaceContainer,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer
            ),
            shape = RectangleShape,
        ){
            TitleBarWithBackButton(title = "Log Blood Pressure") {onDismissRequest() }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Card(modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 8.dp)) {
                    Row(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)) {
                        Text(
                            text ="Blood Pressure",
                            color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.weight(1f))

                        Icon(
                            painter = painterResource(R.drawable.outline_blood_pressure_24),
                            contentDescription = null,
                            tint =MaterialTheme.colorScheme.secondary
                        )
                    }
                    Row(modifier = Modifier.padding(start = 8.dp, end = 8.dp)) {

                            //systolic value text field
                            OutlinedTextField(
                                singleLine = true,
                                value = "",
                                onValueChange = {

                                },
                                label = { Text(text = stringResource(R.string.systolic), modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
                                shape = RoundedCornerShape(16.dp),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Next
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 4.dp),
                                supportingText = {Text("mmhg", color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) }
                            )



                        //diastolic value text field
                        OutlinedTextField(
                            singleLine = true,
                            value = "",
                            onValueChange = {

                            },
                            label = { Text(text = stringResource(R.string.diastolic), modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
                            shape = RoundedCornerShape(16.dp),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 4.dp, end = 4.dp),
                            supportingText = {Text("mmhg", color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) }
                        )

                        //Pulse value text field
                        OutlinedTextField(
                            singleLine = true,
                            value = "",
                            onValueChange = {

                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                            label = { Text(text = stringResource(R.string.pulse), modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 4.dp),
                            supportingText = {Text("bpm", color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) }
                        )
                    }

                    Row() {
                        Text("make sure to measure on the same arm for consistent results.", modifier = Modifier.padding(start = 16.dp, bottom = 8.dp, top = 8.dp))
                    }
                }

                Card(modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 8.dp)) {
                    Row(modifier = Modifier.padding(top = 8.dp, end = 16.dp, start = 16.dp)) {
                        Text("Date and time", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            painter = painterResource(R.drawable.ic_date),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                    Row(modifier = Modifier.padding(8.dp)) {
                        OutlinedTextField(
                            value = "20/07/2026",
                            onValueChange = {},
                            //label = { Text(stringResource(R.string.Custom_Log_Date_TextField))},
                            readOnly = true,
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.ic_date),
                                    contentDescription = null
                                )
                            },
                            trailingIcon = {
                                IconButton(onClick = {}) {
                                    Icon(
                                        imageVector = Icons.Filled.Edit,
                                        contentDescription = null
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                    Row(modifier = Modifier.padding(8.dp)) {
                        OutlinedTextField(
                            value = "22:14",
                            onValueChange = {},
                            //label = {Text(stringResource(R.string.Custom_Log_Time_TextField))},
                            readOnly = true,
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.ic_analogue_clock),
                                    contentDescription = null
                                )
                            },
                            trailingIcon = {
                                IconButton(onClick = {}) {
                                    Icon(
                                        imageVector = Icons.Filled.Edit,
                                        contentDescription = null
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Text("Press the edit button to change date and time. By default current date and time is used.", modifier = Modifier.padding(start = 16.dp, bottom = 8.dp))
                }

                Card(modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 8.dp)) {
                    Row(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)) {
                        Text("Notes", color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            painter = painterResource(R.drawable.ic_notes),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        label = {Text(stringResource(R.string.Custom_Log_Note_TextField))},
                        minLines = 3,
                        maxLines = 3,
                        trailingIcon = {

                        },
                        modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        ),
                        placeholder = {Text(stringResource(R.string.Note_placeholder))}
                    )
                    Text("0/100", modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp), textAlign = TextAlign.End)
                    Text("Notes are optional, they can include things such as mood, what arm you used, whether you exercised that day etc.", modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom =8.dp))
                }

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(onClick = {onDismissRequest()}, modifier = Modifier.weight(1f).padding(16.dp)) {Text("Cancel") }
                    Button(onClick = {onDismissRequest()}, modifier = Modifier.weight(1f).padding(16.dp)) {Text("Save") }

                }
            }
        } }
}