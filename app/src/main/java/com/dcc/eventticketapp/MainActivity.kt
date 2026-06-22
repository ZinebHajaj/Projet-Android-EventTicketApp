package com.dcc.eventticketapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.dcc.eventticketapp.ui.favorites.FavoritesViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.dcc.eventticketapp.ui.auth.AuthIntent
import com.dcc.eventticketapp.ui.auth.AuthViewModel
import com.dcc.eventticketapp.ui.category.CategoryViewModel
import com.dcc.eventticketapp.ui.events.EventsViewModel
import com.dcc.eventticketapp.ui.home.HomeViewModel
import com.dcc.eventticketapp.ui.navigation.AppNavigation
import com.dcc.eventticketapp.ui.theme.AppPreferencesViewModel
import com.dcc.eventticketapp.ui.theme.EventTicketAppTheme
import com.dcc.eventticketapp.ui.theme.ThemeViewModel
import com.dcc.eventticketapp.ui.ticket.TicketViewModel
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.AndroidEntryPoint
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.os.LocaleListCompat
import com.dcc.eventticketapp.utils.applyLocale



@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val authViewModel : AuthViewModel by viewModels()
    private val homeViewModel : HomeViewModel by viewModels()
    private val categoryViewModel : CategoryViewModel by viewModels()
    private val eventsViewModel : EventsViewModel by viewModels()

    private val favoritesViewModel: FavoritesViewModel by viewModels()
    private val ticketViewModel: TicketViewModel by viewModels()

    /* ----------- Google Sign-In ----------- */
    private val googleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("143574668833-aoklca9mmdvfcr2p20a26ec71pcnisfr.apps.googleusercontent.com")
            .requestEmail()
            .build()
        GoogleSignIn.getClient(this, gso)
    }

    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account.idToken
            if (idToken != null) {
                authViewModel.handleIntent(AuthIntent.GoogleSignInResult(idToken))
            } else {
                authViewModel.handleIntent(AuthIntent.GoogleSignInError)
            }
        } catch (e: ApiException) {
            authViewModel.handleIntent(AuthIntent.GoogleSignInError)
        }
    }

    fun launchGoogleSignIn() {
        googleSignInClient.signOut().addOnCompleteListener {
            googleSignInLauncher.launch(googleSignInClient.signInIntent)
        }
    }

    /* ----------- Facebook Sign-In ----------- */
    private val facebookCallbackManager by lazy {
        CallbackManager.Factory.create()
    }

    fun launchFacebookSignIn() {
        LoginManager.getInstance().registerCallback(
            facebookCallbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    authViewModel.handleIntent(
                        AuthIntent.FacebookSignInResult(result.accessToken)
                    )
                }
                override fun onCancel() {}
                override fun onError(error: FacebookException) {
                    authViewModel.handleIntent(AuthIntent.FacebookSignInError)
                }
            }
        )
        LoginManager.getInstance().logInWithReadPermissions(
            this,
            facebookCallbackManager,
            listOf("email", "public_profile")
        )
    }

    @Deprecated("Needed for Facebook SDK")
    override fun onActivityResult(
        requestCode : Int,
        resultCode  : Int,
        data        : Intent?
    ) {
        facebookCallbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }


    private val prefsViewModel     : AppPreferencesViewModel by viewModels()

    private fun initializeEventCounters() {
        // Tous les événements de ton events.json
        val events = listOf("1", "2", "3", "4", "5")

        val tiers = mapOf(
            "vip" to 50,
            "standard" to 200,
            "economy" to 300
        )

        events.forEach { eventId ->
            tiers.forEach { (tierId, totalSeats) ->
                com.google.firebase.firestore.FirebaseFirestore.getInstance()
                    .collection("events")
                    .document(eventId)
                    .collection("seatTiers")
                    .document(tierId)
                    .set(mapOf(
                        "totalSeats" to totalSeats,
                        "bookedSeats" to 0
                    ))
            }
        }
    }


    // Corps
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeEventCounters()

        enableEdgeToEdge()

        setContent {
            val prefsState by prefsViewModel.state.collectAsState()
                EventTicketAppTheme (darkTheme = prefsState.isDarkMode) {
                AppNavigation(

                    authViewModel = authViewModel,
                    homeViewModel = homeViewModel,
                    categoryViewModel = categoryViewModel,
                    eventsViewModel = eventsViewModel,
                    favoritesViewModel = favoritesViewModel,
                    prefsViewModel     = prefsViewModel,
                    ticketViewModel   = ticketViewModel,
                    onGoogleSignIn    = { launchGoogleSignIn() },
                    onFacebookSignIn  = { launchFacebookSignIn() }
                )
            }
        }
    }

}