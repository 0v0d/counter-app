package com.example.counterapp.view.setting.component

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import com.example.counterapp.R

@Composable
fun LanguageSettingsSection(modifier: Modifier = Modifier) {
    val currentLanguageCode =
        AppCompatDelegate.getApplicationLocales().toLanguageTags()

    Column(modifier = modifier) {
        Text(text = stringResource(id = R.string.Language))
        Spacer(modifier = Modifier.padding(8.dp))
        SettingsRadioButton(
            text = R.string.English,
            selected = currentLanguageCode == LanguageConfig.English.languageCode,
            onClick = {
                updateLanguage(LanguageConfig.English.languageCode)
            },
        )
        SettingsRadioButton(
            text = R.string.Japanese,
            selected = currentLanguageCode == LanguageConfig.Japanese.languageCode,
            onClick = {
                updateLanguage(LanguageConfig.Japanese.languageCode)
            },
        )
    }
}

private fun updateLanguage(languageTag: String) {
    val appLocale = LocaleListCompat.forLanguageTags(languageTag)
    AppCompatDelegate.setApplicationLocales(appLocale)
}

private enum class LanguageConfig(val languageCode: String) {
    English("en"),
    Japanese("ja"),
}
