package com.example.genscanproject

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.generator.presentation.GeneratorScreen
import com.example.feature_profile.presentation.ProfileScreen
import com.example.scanner.presentation.ScannerScreen
import com.example.history.presentation.HistoryScreen // Added import for HistoryScreen
import com.example.genscanproject.navigation.BottomNavItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                val items = listOf(
                    BottomNavItem.Scanner,
                    BottomNavItem.Generator,
                    BottomNavItem.History, // Added History to the list
                    BottomNavItem.Profile
                )
                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                        label = { Text(text = item.title) },
                        alwaysShowLabel = true,
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(
                navController = navController,
                startDestination = BottomNavItem.Scanner.route
            ) {
                composable(BottomNavItem.Scanner.route) { ScannerScreen() }
                composable(BottomNavItem.Generator.route) { GeneratorScreen() }
                composable(BottomNavItem.History.route) { HistoryScreen() } // Added route for History
                composable(BottomNavItem.Profile.route) {
                    ProfileScreen(
                        onLogout = {
                            navController.navigate(BottomNavItem.Scanner.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        },
                        onNavigateToScanner = {
                            navController.navigate(BottomNavItem.Scanner.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        onNavigateToGenerator = {
                            navController.navigate(BottomNavItem.Generator.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen()
}