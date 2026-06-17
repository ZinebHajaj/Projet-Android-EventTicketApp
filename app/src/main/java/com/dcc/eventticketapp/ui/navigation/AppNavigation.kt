package com.dcc.eventticketapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dcc.eventticketapp.ui.auth.AuthViewModel
import com.dcc.eventticketapp.ui.auth.screens.LoginScreen
import com.dcc.eventticketapp.ui.auth.screens.RegisterScreen
import com.dcc.eventticketapp.ui.category.CategoryViewModel
import com.dcc.eventticketapp.ui.eventDetail.screens.EventDetailScreen
import com.dcc.eventticketapp.ui.home.HomeViewModel
import com.dcc.eventticketapp.ui.home.screens.HomeScreen
import com.dcc.eventticketapp.ui.profile.screens.ProfileScreen
import com.dcc.eventticketapp.ui.splash.SplashScreen
import com.dcc.eventticketapp.ui.events.EventsViewModel
import com.dcc.eventticketapp.ui.events.screens.EventsScreen
import com.dcc.eventticketapp.ui.favorites.FavoritesViewModel
import com.dcc.eventticketapp.ui.favorites.screens.FavoritesScreen
import com.dcc.eventticketapp.ui.profile.ProfileDestination
import com.dcc.eventticketapp.ui.profile.screens.AboutScreen
import com.dcc.eventticketapp.ui.profile.screens.HelpScreen
import com.dcc.eventticketapp.ui.profile.screens.PersonalInfoScreen
import com.dcc.eventticketapp.ui.profile.screens.PrivacyScreen
import com.dcc.eventticketapp.ui.profile.screens.ReservationsScreen
import com.dcc.eventticketapp.ui.theme.AppPreferencesViewModel

@Composable
fun AppNavigation(
    authViewModel : AuthViewModel,
    homeViewModel : HomeViewModel,
    categoryViewModel : CategoryViewModel,
    eventsViewModel : EventsViewModel,
    favoritesViewModel : FavoritesViewModel,
    prefsViewModel     : AppPreferencesViewModel


) {
    val navController = rememberNavController()

    val prefsState by prefsViewModel.state.collectAsState()
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {

        // 1- Splash -> Home
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
                categoryViewModel = categoryViewModel,
                onEventClick   = { eventId ->
                    navController.navigate("eventDetail/$eventId")
                },
                onProfileClick = {
                    navController.navigate("profile")
                },
                onEventsClick  = {
                    navController.navigate("events")
                },
                onFavoritesClick = {
                    navController.navigate("favorites")
                }
            )
        }
        // Evenements
        composable("events") {
            EventsScreen(
                onEventClick = { eventId ->
                    navController.navigate("eventDetail/$eventId")
                }
            )
        }
        // 3- Profile
        composable("profile") {

            ProfileScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                onNavigateTo   = { destination ->
                    when (destination) {
                        is ProfileDestination.PersonalInfo ->
                            navController.navigate("profile/personal_info")
                        is ProfileDestination.Reservations ->
                            navController.navigate("profile/reservations")
                        is ProfileDestination.Favorites ->
                            navController.navigate("favorites")
                        is ProfileDestination.Help    ->
                            navController.navigate("profile/help")
                        is ProfileDestination.Privacy ->
                            navController.navigate("profile/privacy")
                        is ProfileDestination.About   ->
                            navController.navigate("profile/about")
                    }
                },
                // ── Préférences depuis DataStore ──────────────────────
                isDarkMode            = prefsState.isDarkMode,
                notificationsEnabled  = prefsState.notificationsEnabled,
                currentLanguage       = prefsState.language,
                onToggleDarkMode      = { prefsViewModel.toggleDarkMode() },
                onToggleNotifications = { prefsViewModel.toggleNotifications() },
                onLanguageChange      = { lang -> prefsViewModel.setLanguage(lang) }

            )
        }
        // ── Sous-routes Profile ── DANS LE NavHost ────────────
        composable("profile/personal_info") {
            PersonalInfoScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable("profile/reservations") {
            ReservationsScreen(
                onBack    = { navController.popBackStack() },
                onExplore = { navController.navigate("events") }
            )
        }

        composable("profile/help") {
            HelpScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable("profile/privacy") {
            PrivacyScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable("profile/about") {
            AboutScreen(
                onBack = { navController.popBackStack() }
            )
        }

        // Route favoris
        composable("favorites") {
            FavoritesScreen(
                onEventClick = { eventId ->
                    navController.navigate("eventDetail/$eventId")
                },
                onExplore = {
                    navController.navigate("events")
                }
            )
        }

        // 4- Login
        composable("login") {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
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
                viewModel = authViewModel,
                onRegisterSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        // 6- détails event
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


    }

}