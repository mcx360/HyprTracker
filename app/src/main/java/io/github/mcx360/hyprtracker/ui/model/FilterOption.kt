package io.github.mcx360.hyprtracker.ui.model

import androidx.annotation.StringRes
import io.github.mcx360.hyprtracker.R

enum class FilterOption(@StringRes val labelRes: Int) {
    AllTime(R.string.All_Time),
    Month(R.string.Month),
    Week(R.string.Week)
}