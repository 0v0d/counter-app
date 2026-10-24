package com.example.counterapp

import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.counterapp.ui.theme.CounterAppTheme
import com.example.counterapp.ui.theme.shouldUseDarkTheme
import com.example.counterapp.view.MainScreen
import com.example.counterapp.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        setContent {
            val themeConfig by viewModel.userConfig.collectAsState()
            val initialLoadCompleted by viewModel.initialLoadCompleted.collectAsState()

            val isDarkTheme = shouldUseDarkTheme(themeConfig)
            val lightScrim = Color.WHITE
            val darkScrim = if (isDarkTheme) {
                MaterialTheme.colorScheme.surface.toArgb()
            } else {
                Color.TRANSPARENT
            }

            enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.auto(
                    Color.TRANSPARENT,
                    Color.TRANSPARENT,
                ) { isDarkTheme },
                navigationBarStyle = SystemBarStyle.auto(
                    lightScrim,
                    darkScrim,
                ) { isDarkTheme },
            )
            CounterAppTheme(themeConfig = themeConfig) {
                MainScreen()
            }
            splashScreen.setKeepOnScreenCondition { initialLoadCompleted }
        }
    }
}
