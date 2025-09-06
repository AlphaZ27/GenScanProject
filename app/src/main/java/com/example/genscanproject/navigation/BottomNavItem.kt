package com.example.genscanproject.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.History // Added import for History icon
import androidx.compose.material.icons.filled.QrCodeGenerator
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Scanner : BottomNavItem("scanner", "Scanner", Icons.Default.QrCodeScanner)
    object Generator : BottomNavItem("generator", "Generator", Icons.Default.QrCodeGenerator)
    object Profile : BottomNavItem("profile", "Profile", Icons.Default.AccountCircle)
    object History : BottomNavItem("history", "History", Icons.Filled.History) // Added History item
}