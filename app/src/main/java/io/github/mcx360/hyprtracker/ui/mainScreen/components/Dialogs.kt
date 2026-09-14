package io.github.mcx360.hyprtracker.ui.mainScreen.components

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.github.mcx360.hyprtracker.R

@Composable
fun AboutDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            val uriHandler = LocalUriHandler.current

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    painter = painterResource(R.drawable.ic_about),
                    contentDescription = null,
                    modifier = modifier.padding(top = 8.dp)
                )

                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = " About HyprTracker",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "HyprTracker is an app designed to help you keep a journal of your blood pressure readings.",
                    modifier = Modifier.padding(horizontal = 16.dp),
                )

                Text(
                    text = "Version: 0.5.0",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 4.dp, bottom = 4.dp),
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = { uriHandler.openUri("https://github.com/mcx360/HyprTracker") }) {
                        Icon(painter = painterResource(R.drawable.outline_code_xml_24), contentDescription = null)
                        Text("Source code")
                    }
                    Text(
                        text = "•",
                        color = MaterialTheme.colorScheme.primary
                    )
                    TextButton(onClick = {
                        val subject = Uri.encode("HyprTracker App Feedback")
                        uriHandler.openUri("mailto:support@app.com?subject=$subject") }) {
                        Icon(painter = painterResource(R.drawable.outline_mail_24), contentDescription = null)
                        Text("Contact")
                    }
                }
                HorizontalDivider(modifier = modifier.padding(horizontal = 8.dp))

                TextButton(
                    onClick = onDismissRequest,
                    modifier = modifier.align(Alignment.End).padding(8.dp)
                ) {
                    Text(text = "OK", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun BugReportDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
){
    Dialog(onDismissRequest = {}){
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
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
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Bug reporting",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("If you found a bug please report using github or email. Please describe the bug in detail and the steps needed to reproduce the bug. Please also mention your android version and device model, thank you.")
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val uriHandler = LocalUriHandler.current
                    val subject = Uri.encode("Bug report")
                    val body = Uri.encode("Describe issue here")

                    TextButton(onClick = { uriHandler.openUri("https://github.com/mcx360/HyprTracker/issues/new") }) {
                        Icon(painter = painterResource(R.drawable.outline_code_blocks_24), contentDescription = null)
                        Text(text = "Github")
                    }

                    Text(
                        text = "•",
                        color = MaterialTheme.colorScheme.primary
                    )

                    TextButton(onClick = { uriHandler.openUri("mailto:support@app.com?subject=$subject&body=$body") }) {
                        Icon(painter = painterResource(R.drawable.outline_mail_24), contentDescription = null)
                        Text("email")
                    }
                }

                HorizontalDivider()

                TextButton(
                    onClick = {onDismissRequest()},
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Ok")
                }
            }
        }
    }
}