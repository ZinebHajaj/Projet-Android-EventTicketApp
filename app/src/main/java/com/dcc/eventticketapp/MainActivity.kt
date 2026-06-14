package com.dcc.eventticketapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.dcc.eventticketapp.ui.auth.AuthViewModel
import com.dcc.eventticketapp.ui.category.CategoryViewModel
import com.dcc.eventticketapp.ui.events.EventsViewModel
import com.dcc.eventticketapp.ui.favorites.FavoritesViewModel
import com.dcc.eventticketapp.ui.home.HomeViewModel
import com.dcc.eventticketapp.ui.theme.EventTicketAppTheme
import com.dcc.eventticketapp.ui.navigation.AppNavigation
import com.dcc.eventticketapp.ui.ticket.TicketViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val authViewModel : AuthViewModel by viewModels()
    private val homeViewModel : HomeViewModel by viewModels()
    private val categoryViewModel : CategoryViewModel by viewModels()

    private val eventsViewModel : EventsViewModel by viewModels()
    private val favoritesViewModel : FavoritesViewModel by viewModels()

    private val ticketViewModel   : TicketViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EventTicketAppTheme {
                AppNavigation(
                    authViewModel = authViewModel,
                    homeViewModel = homeViewModel,
                    categoryViewModel = categoryViewModel,
                    eventsViewModel = eventsViewModel,
                    favoritesViewModel = favoritesViewModel,
                    ticketViewModel    = ticketViewModel
                )
            }
        }
    }
}

/*
@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun GreetingPreview() {
    EventTicketAppTheme {
        LoginScreen()
        //RegisterScreen()
    }
}
*/