package com.example.counterapp.navigation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.counterapp.R

@Composable
fun BottomNavigation(navController: NavHostController, modifier: Modifier = Modifier) {
    val views = remember {
        listOf(
            NavItem.Counter,
            NavItem.Settings,
        )
    }
    NavigationBar(modifier = modifier.fillMaxWidth()) {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        views.forEach {
            NavigationBarItem(
                icon = { Icon(it.icon, contentDescription = stringResource(id = it.title)) },
                label = { Text(stringResource(id = it.title)) },
                selected = currentRoute == it.route,
                onClick = {
                    navController.navigate(it.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        }
    }
}

sealed class NavItem(val route: String, val icon: ImageVector, @StringRes val title: Int) {
    data object Counter : NavItem("Counter", Icons.Default.AddCircle, R.string.Counter)
    data object Settings : NavItem("settings", Icons.Default.Settings, R.string.Settings)
}
