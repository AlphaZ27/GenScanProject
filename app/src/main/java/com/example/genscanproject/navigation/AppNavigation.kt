package com.example.genscanproject.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.genscanproject.feature_auth.presentation.LoginScreen
import com.example.genscanproject.feature_profile.presentation.ProfileScreen
import com.example.genscanproject.presentation.PendingApprovalScreen
import com.example.genscanproject.presentation.SplashScreen

// Defines the routes for navigation
object AppRoutes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val PROFILE = "profile"
    const val ADMIN_DASHBOARD = "admin_dashboard"
    const val PENDING_APPROVAL = "pending_approval"
    const val GENERATOR = "generator"
    const val SCANNER = "scanner"
}

@Composable
fun AppNavigation(mainViewModel: MainViewModel = viewModel()) {
    val navController = rememberNavController()
    val authState by mainViewModel.authState.collectAsState()

    // The NavHost defines the navigation graph
    NavHost(navController = navController, startDestination = AppRoutes.SPLASH) {
        // Splash screen shown while checking auth state
        composable(AppRoutes.SPLASH) {
            SplashScreen()
        }

        // Login screen for unauthenticated users
        composable(AppRoutes.LOGIN) {
            LoginScreen(onLoginSuccess = {
                // After login, navigate to the correct screen based on the updated auth state
                // This assumes the authState will be refreshed by the viewModel
                // A better approach might be to have the login success return the user role.
                // For simplicity, we'll re-evaluate here.
                val latestState = mainViewModel.authState.value
                if (latestState is UserAuthState.Authenticated) {
                    val user = latestState.user
                    val route = when {
                        user.role == "admin" -> AppRoutes.ADMIN_DASHBOARD
                        user.status == "approved" -> AppRoutes.PROFILE
                        else -> AppRoutes.PENDING_APPROVAL
                    }
                    navController.navigate(route) {
                        popUpTo(AppRoutes.LOGIN) { inclusive = true }
                    }
                }
            })
        }

        // Profile screen for approved, non-admin users
        composable(AppRoutes.PROFILE) {
            ProfileScreen(
                onLogout = {
                mainViewModel.logout()
                navController.navigate(AppRoutes.LOGIN) {
                    popUpTo(AppRoutes.PROFILE) { inclusive = true }
                }
            },
                arrayOf<>(navController.navigate(AppRoutes.SCANNER)).also {
                    onNavigateToScanner = it
                },
                var onNavigateToGenerator : kotlin.Any = kotlin.arrayOf <> (navController.navigate(
                AppRoutes.GENERATOR
                    /*
                    *  onNavigateToScanner = { navController.navigate(AppRoutes.SCANNER) },
                    onNavigateToGenerator = { navController.navigate(AppRoutes.GENERATOR) } */
            ))

        }

        // Generator screen for users who want to generate QR codes
        composable(AppRoutes.GENERATOR) {
            GeneratorScreen()
        }
        // Scanner screen for users who want to scan QR codes
        composable(AppRoutes.SCANNER) {
            ScannerScreen()
        }


        // Admin dashboard for admin users
        composable(AppRoutes.ADMIN_DASHBOARD) {
            // AdminDashboardScreen()
            // For now, let's add a placeholder with logout
            ProfileScreen(onLogout = { // Replace with AdminDashboardScreen when ready
                mainViewModel.logout()
                navController.navigate(AppRoutes.LOGIN) {
                    popUpTo(AppRoutes.ADMIN_DASHBOARD) { inclusive = true }
                }
            })
        }

        // Screen for users awaiting approval
        composable(AppRoutes.PENDING_APPROVAL) {
            PendingApprovalScreen(onLogout = {
                mainViewModel.logout()
                navController.navigate(AppRoutes.LOGIN) {
                    popUpTo(AppRoutes.PENDING_APPROVAL) { inclusive = true }
                }
            })
        }
    }

    // This block observes the auth state and performs the initial navigation
    // from the splash screen.
    when (val state = authState) {
        is UserAuthState.Authenticated -> {
            val startRoute = when {
                state.user.role == "admin" -> AppRoutes.ADMIN_DASHBOARD
                state.user.status == "approved" -> AppRoutes.PROFILE
                else -> AppRoutes.PENDING_APPROVAL
            }
            navController.navigate(startRoute) {
                popUpTo(AppRoutes.SPLASH) { inclusive = true }
            }
        }
        is UserAuthState.Unauthenticated -> {
            navController.navigate(AppRoutes.LOGIN) {
                popUpTo(AppRoutes.SPLASH) { inclusive = true }
            }
        }
        is UserAuthState.Loading -> {
            // Do nothing, stay on splash
        }
        is UserAuthState.AuthError -> {
            // Can navigate to login or show an error
            navController.navigate(AppRoutes.LOGIN) {
                popUpTo(AppRoutes.SPLASH) { inclusive = true }
            }
        }
    }
}