package io.github.mcx360.hyprtracker.data.Source.Local.theme

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import io.github.mcx360.hyprtracker.ui.theme.ThemeMode
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class ThemeManager(private val context: Context) {

    companion object {
        private val THEME_KEY = stringPreferencesKey("theme_mode")
    }

    val themeFlow = context.dataStore.data.map { preferences ->
        ThemeMode.valueOf(
            preferences[THEME_KEY] ?: ThemeMode.SYSTEM.name
        )
    }

    suspend fun saveTheme(themeMode: ThemeMode) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = themeMode.name
        }
    }
}