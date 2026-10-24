package com.example.counterapp.view.setting.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.counterapp.R
import com.example.counterapp.model.ThemeConfig

@Composable
fun SettingsContent(
    themeConfig: ThemeConfig,
    onChangeThemeConfig: (ThemeConfig) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = stringResource(id = R.string.Settings),
            style = MaterialTheme.typography.titleMedium,
            fontStyle = MaterialTheme.typography.titleMedium.fontStyle,
        )
        Spacer(modifier = Modifier.padding(8.dp))

        ThemeSettingsSection(
            currentTheme = themeConfig,
            onChangeThemeConfig = onChangeThemeConfig,
        )

        Spacer(modifier = Modifier.padding(8.dp))

        LanguageSettingsSection()
    }
}
