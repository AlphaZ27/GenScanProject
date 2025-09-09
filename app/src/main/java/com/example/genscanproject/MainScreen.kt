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
import com.example.history.HistoryScreen // Added import for HistoryScreen
import com.example.genscanproject.navigation.BottomNavItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(onLogout: () -> Unit) {

    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            // ... your NavigationBar code is correct
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(
                navController = navController,
                startDestination = BottomNavItem.Scanner.route
            ) {
                composable(BottomNavItem.Scanner.route) { ScannerScreen() }
                composable(BottomNavItem.Generator.route) { GeneratorScreen() }
                composable(BottomNavItem.History.route) { HistoryScreen() }
                composable(BottomNavItem.Profile.route) {
                    // ⬇️ PASS THE onLogout LAMBDA DOWN TO THE PROFILE SCREEN
                    ProfileScreen(onLoggedOut = onLogout)
                }
            }
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen(onLogout = {})
}