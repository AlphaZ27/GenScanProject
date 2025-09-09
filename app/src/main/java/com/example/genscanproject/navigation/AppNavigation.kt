package com.example.genscanproject.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel // USE THE HILT IMPORT
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.feature_auth.presentation.LoginScreen
import com.example.feature_auth.presentation.RegistrationScreen
import com.example.feature_profile.presentation.ProfileScreen
import com.example.genscanproject.presentation.PendingApprovalScreen
import com.example.feature_admin.presentation.AdminDashboardScreen
import com.example.genscanproject.MainScreen
import com.example.genscanproject.presentation.SplashScreen
import com.example.scanner.presentation.ScannerScreen
import com.example.generator.presentation.GeneratorScreen
// Added imports for the classes we created:
import com.example.genscanproject.presentation.MainViewModel
import com.example.genscanproject.presentation.UserAuthState

// Defines the routes for navigation
object AppRoutes {
    const val LOGIN = "login"
    const val REGISTRATION = "registration"
    const val MAIN_HUB = "main_hub" // This is where approved users go after login
    const val PENDING_APPROVAL = "pending_approval"
    // We can simplify and remove others from here as they'll be part of the main hub's navigation
}

@Composable
fun AppNavigation(
    mainViewModel: MainViewModel = hiltViewModel() //  GET THE VIEWMODEL WITH HILT
) {

    val navController = rememberNavController()
    val authState by mainViewModel.authState.collectAsState()

    // This determines the starting point of the app based on auth state
    // It prevents the screen from flashing between states
    val startDestination = when (authState) {
        is UserAuthState.Authenticated -> {
            val user = (authState as UserAuthState.Authenticated).user
            when {
                // For now, let's send both admin and approved users to the main hub
                user.status == "approved" || user.role == "admin" -> AppRoutes.MAIN_HUB
                else -> AppRoutes.PENDING_APPROVAL
            }
        }
        is UserAuthState.Unauthenticated -> AppRoutes.LOGIN
        is UserAuthState.Loading -> "splash" // Show a temporary splash/loading route
    }


    NavHost(navController = navController, startDestination = startDestination) {
        composable("splash") {
            // You can use your SplashScreen here
        }

        composable(AppRoutes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    // After login, the authState will change, and the NavHost will automatically
                    // navigate to the correct startDestination (MAIN_HUB or PENDING_APPROVAL).
                    // We just need to pop the login screen off the back stack.
                    navController.navigate(startDestination) {
                        popUpTo(AppRoutes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(AppRoutes.REGISTRATION)
                }
            )
        }

        composable(AppRoutes.REGISTRATION) {
            RegistrationScreen(
                onRegisterSuccess = {
                    navController.navigate(AppRoutes.PENDING_APPROVAL) {
                        popUpTo(AppRoutes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(AppRoutes.MAIN_HUB) {
            // The MainScreen contains its own bottom navigation for Scanner, Generator, etc.
            MainScreen(onLogout = {
                mainViewModel.logout()
                // The authState change will automatically navigate back to the Login screen.
            })
        }

        composable(AppRoutes.PENDING_APPROVAL) {
            PendingApprovalScreen(onLogout = {
                mainViewModel.logout()
            })
        }
    }
}
