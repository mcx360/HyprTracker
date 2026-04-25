package io.github.mcx360.hyprtracker.ui.model

import androidx.annotation.StringRes
import io.github.mcx360.hyprtracker.R

enum class MinMaxAvg(@StringRes val labelRes: Int) {
    Min(R.string.Minimum),
    Max(R.string.Maximum),
    Average(R.string.Average)
}