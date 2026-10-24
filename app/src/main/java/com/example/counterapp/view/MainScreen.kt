package com.example.counterapp.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.counterapp.navigation.BottomNavigation
import com.example.counterapp.navigation.NavItem
import com.example.counterapp.view.counter.CounterScreen
import com.example.counterapp.view.setting.SettingsScreen

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    Scaffold(
        modifier = modifier,
        bottomBar = { BottomNavigation(navController) },
    ) { paddingValues ->
        NavHost(
            navController,
            startDestination = NavItem.Counter.route,
            modifier = Modifier.padding(paddingValues),
        ) {
            composable(NavItem.Counter.route) {
                CounterScreen()
            }
            composable(NavItem.Settings.route) {
                SettingsScreen()
            }
        }
    }
}
