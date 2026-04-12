package io.github.mcx360.hyprtracker.ui.mainScreen.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.R

//Note to self: Report bug functionality will have a link to a page where you can report the bug so that the app itself does not make any internet connection as this is a strictly offline app
@Composable
fun BugReportDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier
){
    val ctx = LocalContext.current
    Dialog(onDismissRequest = {}){
        Card(
            modifier = modifier
                .padding(16.dp)
                .height(500.dp)
            ,
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Bug reporting",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp)
                ,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    modifier = Modifier
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val uriHandler = LocalUriHandler.current

                    val subject = Uri.encode("Bug Report")
                    val body = Uri.encode("Describe your issue here")

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("If you found a bug please report using github or email. Please describe the bug in detail and the steps needed to reproduce the bug. Please also mention your android version and device model, thank you.")

                        Spacer(modifier = Modifier.height(8.dp))

                        LinkRow(text = "Report on GitHub") {
                            uriHandler.openUri("https://github.com/mcx360/HyprTracker/issues/new")
                        }

                        LinkRow(text = "Or email") {
                            uriHandler.openUri(
                                "mailto:support@app.com?subject=$subject&body=$body"
                            )
                        }
                    }
                }

                Button(onClick = {onDismissRequest()}) {Text("Ok") }
            }
        }
    }
}

@Composable
fun LinkRow(
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.primary,
            textDecoration = TextDecoration.Underline
        )

        Spacer(modifier = Modifier.width(4.dp))

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
        )
    }
}