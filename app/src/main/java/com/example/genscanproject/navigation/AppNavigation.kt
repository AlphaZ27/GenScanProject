package com.example.genscanproject.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
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
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val REGISTRATION = "registration"
    const val PROFILE = "profile"
    const val ADMIN_DASHBOARD = "admin_dashboard"
    const val PENDING_APPROVAL = "pending_approval"
    const val GENERATOR = "generator"
    const val SCANNER = "scanner"
    const val MAIN_HUB = "main_hub"
}

@Composable
fun AppNavigation(
    mainViewModel: MainViewModel = viewModel()
) { // MainViewModel should now be resolved
    val navController = rememberNavController()
    val authState by mainViewModel.authState.collectAsState() // authState & mainViewModel should be fine

    NavHost(navController = navController, startDestination = AppRoutes.SPLASH) {
        composable(AppRoutes.SPLASH) {
            SplashScreen()
        }

        composable(AppRoutes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    val latestState = mainViewModel.authState.value
                    if (latestState is UserAuthState.Authenticated) { // UserAuthState should be resolved
                        val user = latestState.user // user.role and user.status will depend on your com.example.domain.model.User
                        val route = when {
                            user.role == "admin" -> AppRoutes.ADMIN_DASHBOARD
                            user.status == "approved" -> AppRoutes.PROFILE
                            else -> AppRoutes.PENDING_APPROVAL
                        }
                        navController.navigate(route) {
                            popUpTo(AppRoutes.LOGIN) { inclusive = true }
                        }
                    }
                },
                onNavigateToRegistration = {
                    navController.navigate(AppRoutes.REGISTRATION)
                }
            )
        }

        composable(AppRoutes.MAIN_HUB) {
            MainScreen()
        }

        composable(AppRoutes.PROFILE) {
            ProfileScreen(
                onLogout = {
                    mainViewModel.logout() // logout should be resolved
                    navController.navigate(AppRoutes.LOGIN) {
                        popUpTo(AppRoutes.PROFILE) { inclusive = true }
                    }
                },
                onNavigateToScanner = { navController.navigate(AppRoutes.SCANNER) },
                onNavigateToGenerator = { navController.navigate(AppRoutes.GENERATOR) }
            )
        }

        composable(AppRoutes.GENERATOR) {
            GeneratorScreen()
        }
        composable(AppRoutes.SCANNER) {
            ScannerScreen()
        }

        composable(AppRoutes.ADMIN_DASHBOARD) {
            AdminDashboardScreen()
        }

        composable(AppRoutes.ADMIN_DASHBOARD) {
            // Updated ProfileScreen call for admin
            ProfileScreen(
                onLogout = {
                    mainViewModel.logout()
                    navController.navigate(AppRoutes.LOGIN) {
                        popUpTo(AppRoutes.ADMIN_DASHBOARD) { inclusive = true }
                    }
                },
                // Admins can also navigate to scanner/generator
                onNavigateToScanner = { navController.navigate(AppRoutes.SCANNER) },
                onNavigateToGenerator = { navController.navigate(AppRoutes.GENERATOR) }
            )
        }

        composable(AppRoutes.REGISTRATION) {
            RegistrationScreen(onRegistrationSuccess = {
                navController.navigate(AppRoutes.PENDING_APPROVAL) {
                    popUpTo(AppRoutes.LOGIN) { inclusive = true }
                }
            })
        }

        composable(AppRoutes.PENDING_APPROVAL) {
            PendingApprovalScreen(onLogout = {
                mainViewModel.logout() // Added logout here for consistency
                navController.navigate(AppRoutes.LOGIN) {
                    popUpTo(AppRoutes.PENDING_APPROVAL) { inclusive = true }
                }
            })
        }
    }

    when (val state = authState) { // authState & UserAuthState should be fine
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
            // Splash screen is shown
        }
        is UserAuthState.AuthError -> {
            navController.navigate(AppRoutes.LOGIN) { // Or show error on splash
                popUpTo(AppRoutes.SPLASH) { inclusive = true }
            }
        }
    }
}
