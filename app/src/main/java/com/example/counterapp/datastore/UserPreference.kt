package com.example.counterapp.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.counterapp.model.ThemeConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreference @Inject constructor(private val dataStore: DataStore<Preferences>) {
    val userData: Flow<ThemeConfig> = dataStore.data.map {
        ThemeConfig.valueOf(it[THEME_KEY] ?: ThemeConfig.DEFAULT.name)
    }

    suspend fun setThemeConfig(themeConfig: ThemeConfig) {
        dataStore.edit {
            it[THEME_KEY] = themeConfig.name
        }
    }

    private companion object {
        private val THEME_KEY = stringPreferencesKey("theme")
    }
}
