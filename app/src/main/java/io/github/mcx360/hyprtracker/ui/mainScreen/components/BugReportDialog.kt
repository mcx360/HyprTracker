package io.github.mcx360.hyprtracker.ui.mainScreen.components

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.github.mcx360.hyprtracker.R

@Composable
fun BugReportDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier
){
    Dialog(onDismissRequest = {}){
        Card(
            modifier = modifier.padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = modifier.padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(painter = painterResource(R.drawable.ic_bug_report), contentDescription = null)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Bug reporting",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("If you found a bug please report using github or email. Please describe the bug in detail and the steps needed to reproduce the bug. Please also mention your android version and device model, thank you.")
                    }
                }

                Row {
                    val uriHandler = LocalUriHandler.current
                    val subject = Uri.encode("Bug report")
                    val body = Uri.encode("Describe issue here")
                    TextButton(onClick = {
                        uriHandler.openUri("https://github.com/mcx360/HyprTracker/issues/new")
                    }) {
                        Icon(painter = painterResource(R.drawable.outline_code_blocks_24), contentDescription = null)
                        Text("Github")
                    }
                    TextButton(onClick = {
                        uriHandler.openUri(
                            "mailto:support@app.com?subject=$subject&body=$body"
                        )

                    }) {
                        Icon(painter = painterResource(R.drawable.outline_mail_24), contentDescription = null)
                        Text("email")
                    }
                }

                HorizontalDivider()

                TextButton(onClick = {onDismissRequest()}, modifier = Modifier.align(Alignment.End)) {Text("Ok") }
            }
        }
    }
}