package io.github.mcx360.hyprtracker.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
    val tabs = listOf("Log", "History")
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    Column() {
        TabRow(selectedTabIndex = selectedTab) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("log") }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("history") }
            )
        }


        when (selectedTab) {
            0 -> Tab1Screen()
            1 -> Tab2Screen()
        }
    }
}


@Composable
fun Tab1Screen(){
    Text("Log Screen")
}
@Composable
fun Tab2Screen(){
    Text("History Screen")
}