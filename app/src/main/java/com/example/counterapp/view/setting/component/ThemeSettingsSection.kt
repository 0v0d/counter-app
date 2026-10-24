package com.example.counterapp.view.setting.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.counterapp.R
import com.example.counterapp.model.ThemeConfig

@Composable
fun ThemeSettingsSection(
    currentTheme: ThemeConfig,
    onChangeThemeConfig: (ThemeConfig) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(text = stringResource(id = R.string.Theme))
        Spacer(modifier = Modifier.padding(8.dp))
        SettingsRadioButton(
            text = R.string.System,
            selected = currentTheme == ThemeConfig.System,
            onClick = { onChangeThemeConfig(ThemeConfig.System) },
        )
        SettingsRadioButton(
            text = R.string.Light,
            selected = currentTheme == ThemeConfig.Light,
            onClick = { onChangeThemeConfig(ThemeConfig.Light) },
        )
        SettingsRadioButton(
            text = R.string.Dark,
            selected = currentTheme == ThemeConfig.Dark,
            onClick = { onChangeThemeConfig(ThemeConfig.Dark) },
        )
    }
}
