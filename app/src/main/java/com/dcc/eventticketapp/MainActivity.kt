package com.dcc.eventticketapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.dcc.eventticketapp.ui.auth.AuthViewModel
import com.dcc.eventticketapp.ui.auth.screens.LoginScreen
import com.dcc.eventticketapp.ui.home.HomeViewModel
import com.dcc.eventticketapp.ui.theme.EventTicketAppTheme
import com.dcc.mobile.ui.navigation.AppNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val authViewModel : AuthViewModel by viewModels()
    private val homeViewModel : HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EventTicketAppTheme {
                AppNavigation(
                    authViewModel = authViewModel,
                    homeViewModel = homeViewModel
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