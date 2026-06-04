package com.dcc.mobile.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dcc.eventticketapp.ui.auth.AuthViewModel
import com.dcc.eventticketapp.ui.auth.screens.LoginScreen
import com.dcc.eventticketapp.ui.auth.screens.RegisterScreen
import com.dcc.eventticketapp.ui.home.HomeViewModel
import com.dcc.eventticketapp.ui.home.screens.HomeScreen
import com.dcc.eventticketapp.ui.profile.screens.ProfileScreen  // ← ajouter
import com.dcc.mobile.ui.splash.SplashScreen

@Composable
fun AppNavigation(
    authViewModel : AuthViewModel,
    homeViewModel : HomeViewModel
) {
    val navController = rememberNavController()

    NavHost(
        navController    = navController,
        startDestination = "splash"
    ) {

        // 1- Splash
        composable("splash") {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate("home") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        // 2- Home
        composable("home") {
            HomeScreen(
                viewModel      = homeViewModel,
                onEventClick   = { eventId ->
                    navController.navigate("eventDetail/$eventId")
                },
                onProfileClick = {
                    navController.navigate("profile")  // ← corrigé
                }
            )
        }

        // 3- Profile ← nouveau
        composable("profile") {
            ProfileScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }

        // 4- Login
        composable("login") {
            LoginScreen(
                viewModel            = authViewModel,
                onLoginSuccess       = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate("register") }
            )
        }

        // 5- Register
        composable("register") {
            RegisterScreen(
                viewModel         = authViewModel,
                onRegisterSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }
    }
}