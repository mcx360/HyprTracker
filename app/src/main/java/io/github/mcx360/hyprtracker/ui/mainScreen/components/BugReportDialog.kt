package io.github.mcx360.hyprtracker.ui.mainScreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

//Note to self: Report bug functionality will have a link to a page where you can report the bug so that the app itself does not make any internet connection as this is a strictly offline app
@Composable
fun BugReportDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier
){
    Dialog(onDismissRequest = {}){
        Card(
            modifier = modifier
                .fillMaxWidth()
                .height(375.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text("Please Describe bug report")
                TextField(
                    value = "toDO",
                    onValueChange = {},
                    modifier = modifier.padding(16.dp)
                )
                Button(onClick = {
                    onDismissRequest()
                }) {
                    Text("Confirm")
                }
            }
        }
    }
}