package com.dcc.eventticketapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dcc.eventticketapp.ui.auth.AuthViewModel
import com.dcc.eventticketapp.ui.auth.screens.LoginScreen
import com.dcc.eventticketapp.ui.auth.screens.RegisterScreen
import com.dcc.eventticketapp.ui.eventDetail.screens.EventDetailScreen
import com.dcc.eventticketapp.ui.home.HomeViewModel
import com.dcc.eventticketapp.ui.home.screens.HomeScreen
import com.dcc.eventticketapp.ui.profile.screens.ProfileScreen
import com.dcc.eventticketapp.ui.splash.SplashScreen

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
                    navController.navigate("profile")
                }
            )
        }

        // 3- détails event
        composable(
            route     = "eventDetail/{eventId}",
            arguments = listOf(navArgument("eventId") { type = NavType.StringType })
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId") ?: ""
            EventDetailScreen(
                eventId = eventId,
                onBack  = { navController.popBackStack() }
            )
        }

        // 4- Profile
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

        // 5- Login
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

        // 6- Register
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