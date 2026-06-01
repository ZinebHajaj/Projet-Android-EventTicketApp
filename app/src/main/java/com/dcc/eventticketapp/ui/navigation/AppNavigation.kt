package com.dcc.mobile.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dcc.eventticketapp.ui.auth.AuthViewModel
import com.dcc.eventticketapp.ui.auth.screens.LoginScreen
import com.dcc.eventticketapp.ui.auth.screens.RegisterScreen

@Composable
fun AppNavigation(
    authViewModel : AuthViewModel
) {
    val navController = rememberNavController()


    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        // 1- Login
        composable("login") {
            LoginScreen(
                viewModel = authViewModel,
                /*onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },*/
                onNavigateToRegister = { navController.navigate("register") }
            )
        }


        // 2- Register
        composable("register") {
            RegisterScreen(
                viewModel = authViewModel,
                /*
                onRegisterSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                */
                onNavigateToLogin = { navController.popBackStack() }
            )
        }


    }
}