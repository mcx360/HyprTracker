package io.github.mcx360.hyprtracker.ui.model

import io.github.mcx360.hyprtracker.ui.utils.getHyperTensionStage

data class HyprReading(
    val systolicValue: String,
    val diastolicValue: String,
    val pulseValue: String?,
    val date: String,
    val time: String,
    val notes: String?,
    val stage: String = getHyperTensionStage(systolicValue, diastolicValue)
)