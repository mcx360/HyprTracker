package io.github.mcx360.hyprtracker.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import io.github.mcx360.hyprtracker.R

@Composable
fun Dot(
    hyperTensionStage : String
) {
    val colour = when(hyperTensionStage){
        "Normal" -> colorResource(R.color.Hypertension_Normal_Stage_Colour)
        "High Normal" -> colorResource(R.color.Hypertension_High_Normal_Stage_Colour)
        "Grade 1 Hypertension" -> colorResource(R.color.Hypertension_Grade1_Colour)
        "Grade 2 Hypertension" -> colorResource(R.color.Hypertension_Grade2_Colour)
        else -> Color.Gray
    }

    Box(
        modifier = Modifier
            .size(16.dp)
            .background(colour, shape = CircleShape)
    )
}

@Composable
fun DotWithColour(
    colour: Color
) {
    Box(
        modifier = Modifier
            .size(16.dp)
            .background(colour, shape = CircleShape)
    )
}