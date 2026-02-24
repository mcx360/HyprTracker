//fake data class to be used for simulating the data layer of the app while I focus on creating the UI layer first, file to be removed
package io.github.mcx360.hyprtracker.data

data class HyprReading(
    val systolicValue: String,
    val diastolicValue: String,
    val pulseValue: String,
    val date: String,
    val time: String,
    val notes: String
)

object FakeData {
    fun getInitialReadings(): List<HyprReading> {
        return listOf(
            HyprReading("120", "80", "72", "2026-02-20", "08:30", "Morning reading"),
            HyprReading("135", "85", "75", "2026-02-21", "14:15", "After lunch"),
            HyprReading("140", "90", "78", "2026-02-22", "21:00", "Before bed")
        )
    }
}

