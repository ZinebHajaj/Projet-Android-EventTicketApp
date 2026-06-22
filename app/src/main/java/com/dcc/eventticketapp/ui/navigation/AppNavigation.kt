package com.dcc.eventticketapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dcc.eventticketapp.ui.auth.AuthIntent
import com.dcc.eventticketapp.ui.auth.AuthViewModel
import com.dcc.eventticketapp.ui.auth.screens.LoginScreen
import com.dcc.eventticketapp.ui.auth.screens.RegisterScreen
import com.dcc.eventticketapp.ui.booking.BookingViewModel
import com.dcc.eventticketapp.ui.booking.screens.BookingScreen
import com.dcc.eventticketapp.ui.booking.screens.ConfirmationScreen
import com.dcc.eventticketapp.ui.booking.screens.PayPalWebViewScreen
import com.dcc.eventticketapp.ui.booking.screens.PaymentMethodScreen
import com.dcc.eventticketapp.ui.booking.screens.PaymentScreen
import com.dcc.eventticketapp.ui.category.CategoryViewModel
import com.dcc.eventticketapp.ui.eventDetail.screens.EventDetailScreen
import com.dcc.eventticketapp.ui.home.HomeViewModel
import com.dcc.eventticketapp.ui.home.screens.HomeScreen
import com.dcc.eventticketapp.ui.profile.screens.ProfileScreen
import com.dcc.eventticketapp.ui.profile.ProfileDestination
import com.dcc.eventticketapp.ui.profile.screens.AboutScreen
import com.dcc.eventticketapp.ui.profile.screens.HelpScreen
import com.dcc.eventticketapp.ui.profile.screens.PersonalInfoScreen
import com.dcc.eventticketapp.ui.profile.screens.PrivacyScreen
import com.dcc.eventticketapp.ui.profile.screens.ReservationsScreen
import com.dcc.eventticketapp.ui.theme.AppPreferencesViewModel
import com.dcc.eventticketapp.ui.splash.SplashScreen
import com.dcc.eventticketapp.ui.events.EventsViewModel
import com.dcc.eventticketapp.ui.events.screens.EventsScreen
import com.dcc.eventticketapp.ui.favorites.FavoritesViewModel
import com.dcc.eventticketapp.ui.favorites.screens.FavoritesScreen
import com.dcc.eventticketapp.ui.organizer.OrganizerViewModel
import com.dcc.eventticketapp.ui.organizer.screens.CreateEventScreen
import com.dcc.eventticketapp.ui.organizer.screens.EditEventScreen
import com.dcc.eventticketapp.ui.organizer.screens.MyEventsScreen
import com.dcc.eventticketapp.ui.organizer.screens.OrganizerHomeScreen
import com.dcc.eventticketapp.ui.ticket.TicketViewModel
import com.dcc.eventticketapp.ui.ticket.screens.TicketScreen

@Composable
fun AppNavigation(
    authViewModel : AuthViewModel,
    homeViewModel : HomeViewModel,
    categoryViewModel : CategoryViewModel,
    eventsViewModel : EventsViewModel,
    favoritesViewModel : FavoritesViewModel,
    prefsViewModel     : AppPreferencesViewModel,
    ticketViewModel    : TicketViewModel,
    onGoogleSignIn    : () -> Unit,
    onFacebookSignIn  : () -> Unit
) {
    val navController = rememberNavController()

    val prefsState by prefsViewModel.state.collectAsState()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {

        // 1- Splash -> Home
        composable("splash") {
            val authState by authViewModel.state.collectAsState()

            LaunchedEffect(Unit) {
                authViewModel.handleIntent(AuthIntent.CheckSession)
            }

            SplashScreen(
                onSplashFinished = {
                    if (authState.isAuthenticated && authState.userRole == "organisateur") {
                        navController.navigate("organizerHome") {
                            popUpTo("splash") { inclusive = true }
                        }
                    } else {
                        navController.navigate("home") {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                }
            )
        }

        // 2- Home
        composable("home") {
            HomeScreen(
                viewModel = homeViewModel,
                categoryViewModel = categoryViewModel,
                onEventClick = { eventId ->
                    navController.navigate("eventDetail/$eventId")
                },
                onProfileClick = {
                    navController.navigate("profile")
                },
                onEventsClick = {
                    navController.navigate("events")
                },
                onFavoritesClick = {
                    navController.navigate("favorites")
                },
                onTicketsClick = {
                    navController.navigate("tickets")
                }
            )
        }
        // 3- Evenements
        composable("events") {
            EventsScreen(
                onEventClick = { eventId ->
                    navController.navigate("eventDetail/$eventId")
                }
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
                },
                onNavigateTo = { destination ->
                    when (destination) {
                        is ProfileDestination.PersonalInfo ->
                            navController.navigate("profile/personal_info")
                        is ProfileDestination.Reservations ->
                            navController.navigate("profile/reservations")
                        is ProfileDestination.Favorites ->
                            navController.navigate("favorites")
                        is ProfileDestination.Help ->
                            navController.navigate("profile/help")
                        is ProfileDestination.Privacy ->
                            navController.navigate("profile/privacy")
                        is ProfileDestination.About ->
                            navController.navigate("profile/about")
                    }
                },
                isDarkMode = prefsState.isDarkMode,
                onToggleDarkMode = { prefsViewModel.toggleDarkMode() },
            )
        }

        // ── Sous-routes Profile ──
        composable("profile/personal_info") {
            PersonalInfoScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable("profile/reservations") {
            ReservationsScreen(
                onBack = { navController.popBackStack() },
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

        // 5- Favoris
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

        // 6- Login
        composable("login") {
            val authState by authViewModel.state.collectAsState()

            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    // Laisser LaunchedEffect ci-dessous gérer la navigation
                },
                onGoogleSignInClick = onGoogleSignIn,
                onFacebookSignInClick = onFacebookSignIn,
                onNavigateToRegister = { navController.navigate("register") }
            )

            // Redirection selon le rôle
            LaunchedEffect(authState.isSuccess, authState.userRole) {
                if (authState.isSuccess && authState.userRole.isNotBlank()) {
                    if (authState.userRole == "organisateur") {
                        navController.navigate("organizerHome") {
                            popUpTo("login") { inclusive = true }
                        }
                    } else {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                    authViewModel.handleIntent(AuthIntent.ResetSuccess)
                }
            }
        }

        // 7- Register
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

        // 8- détails event
        composable(
            route = "eventDetail/{eventId}",
            arguments = listOf(navArgument("eventId") { type = NavType.StringType })
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId") ?: ""
            EventDetailScreen(
                eventId = eventId,
                onBack = { navController.popBackStack() },
                onNavigateToBooking = { id ->
                    navController.navigate("booking/$id")
                }
            )
        }

        // 9- Tickets
        composable("tickets") {
            TicketScreen(
                viewModel = ticketViewModel
            )
        }

        // 10- Booking
        composable(
            route = "booking/{eventId}",
            arguments = listOf(navArgument("eventId") { type = NavType.StringType })
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId") ?: ""
            val bookingViewModel: BookingViewModel = hiltViewModel()

            BookingScreen(
                eventId = eventId,
                viewModel = bookingViewModel,
                onBack = { navController.popBackStack() },
                onProceed = { navController.navigate("paymentMethod") }
            )
        }

        composable("paymentMethod") {
            val parentEntry = remember(it) {
                navController.getBackStackEntry("booking/{eventId}")
            }
            val bookingViewModel: BookingViewModel = hiltViewModel(parentEntry)

            PaymentMethodScreen(
                viewModel = bookingViewModel,
                onBack = { navController.popBackStack() },
                onContinue = { navController.navigate("payment") },
                onExpired = {
                    navController.navigate("home") { popUpTo("home") { inclusive = true } }
                }
            )
        }

        composable("payment") {
            val parentEntry = remember(it) {
                navController.getBackStackEntry("booking/{eventId}")
            }
            val bookingViewModel: BookingViewModel = hiltViewModel(parentEntry)

            PaymentScreen(
                viewModel = bookingViewModel,
                onBack = { navController.popBackStack() },
                onPaymentSuccess = { navController.navigate("confirmation") },
                onPayPalClick = { navController.navigate("paypalWebView") }
            )
        }

        // PayPal WebView
        composable("paypalWebView") {
            val parentEntry = remember(it) {
                navController.getBackStackEntry("booking/{eventId}")
            }
            val bookingViewModel: BookingViewModel = hiltViewModel(parentEntry)
            val state by bookingViewModel.state.collectAsState()

            PayPalWebViewScreen(
                orderId = state.payPalOrderId,
                clientId = "AX2a5PCwd-BoHbIzU4tuEDH_hWmksGmux0cba2S5ZNR74uiBtNSFR7m-Y9WegyroEXCll5bbootkdo66",
                amount = state.totalPrice * 0.092,
                onSuccess = { payerId ->
                    bookingViewModel.capturePayPalOrder(state.payPalOrderId)
                    navController.navigate("confirmation") {
                        popUpTo("paypalWebView") { inclusive = true }
                    }
                },
                onCancel = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }

        composable("confirmation") {
            val parentEntry = remember(it) {
                navController.getBackStackEntry("booking/{eventId}")
            }
            val bookingViewModel: BookingViewModel = hiltViewModel(parentEntry)

            ConfirmationScreen(
                viewModel = bookingViewModel,
                onGoToTickets = {
                    navController.navigate("tickets") { popUpTo("home") { inclusive = false } }
                },
                onGoHome = {
                    navController.navigate("home") { popUpTo("home") { inclusive = true } }
                }
            )
        }

        // 11- les routes organisateur :
        composable("organizerHome") {
            val organizerViewModel: OrganizerViewModel = hiltViewModel()

            OrganizerHomeScreen(
                onCreateEvent = { navController.navigate("createEvent") },
                onMyEvents = { navController.navigate("myEvents") },
                onLogout = {
                    authViewModel.handleIntent(AuthIntent.Logout)
                    navController.navigate("login") {
                        popUpTo("organizerHome") { inclusive = true }
                    }
                }
            )
        }

        // NOUVEAU : CreateEvent
        composable("createEvent") {
            val parentEntry = remember(it) {
                navController.getBackStackEntry("organizerHome")
            }
            val organizerViewModel: OrganizerViewModel = hiltViewModel(parentEntry)

            CreateEventScreen(
                onBack = { navController.popBackStack() },
                viewModel = organizerViewModel
            )
        }

        // NOUVEAU : MyEvents
        composable("myEvents") {
            val parentEntry = remember(it) {
                navController.getBackStackEntry("organizerHome")
            }
            val organizerViewModel: OrganizerViewModel = hiltViewModel(parentEntry)

            MyEventsScreen(
                onBack = { navController.popBackStack() },
                onEditEvent = { event ->
                    organizerViewModel.handleIntent(
                        com.dcc.eventticketapp.ui.organizer.OrganizerIntent.SelectEvent(event)
                    )
                    navController.navigate("editEvent")
                },
                viewModel = organizerViewModel
            )
        }

        // NOUVEAU : EditEvent
        composable("editEvent") {
            val parentEntry = remember(it) {
                navController.getBackStackEntry("organizerHome")
            }
            val organizerViewModel: OrganizerViewModel = hiltViewModel(parentEntry)
            val state by organizerViewModel.state.collectAsState()

            state.selectedEvent?.let { event ->
                EditEventScreen(
                    event = event,
                    onBack = { navController.popBackStack() },
                    viewModel = organizerViewModel
                )
            } ?: run {
                LaunchedEffect(Unit) {
                    navController.popBackStack()
                }
            }
        }
    }
}