package com.dcc.eventticketapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.dcc.eventticketapp.ui.auth.AuthIntent
import com.dcc.eventticketapp.ui.auth.AuthViewModel
import com.dcc.eventticketapp.ui.category.CategoryViewModel
import com.dcc.eventticketapp.ui.home.HomeViewModel
import com.dcc.eventticketapp.ui.theme.EventTicketAppTheme
import com.dcc.eventticketapp.ui.navigation.AppNavigation
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

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val authViewModel  : AuthViewModel  by viewModels()
    private val homeViewModel  : HomeViewModel  by viewModels()
    private val categoryViewModel : CategoryViewModel by viewModels()
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

    private fun initializeOrganizerAccount() {
        val auth = com.google.firebase.auth.FirebaseAuth.getInstance()
        val email = "organisateur@ticketgo.com"
        val password = "Organisateur123!"  // Mot de passe fort

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val userId = it.user?.uid ?: return@addOnSuccessListener

                // Créer le profil dans Firestore
                val organizer = hashMapOf(
                    "userId" to userId,
                    "fullName" to "Organisateur TicketGo",
                    "email" to email,
                    "phone" to "",
                    "role" to "organisateur"
                )

                com.google.firebase.firestore.FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(userId)
                    .set(organizer)
            }
            .addOnFailureListener {
                // L'utilisateur existe déjà, c'est normal
                Log.d("INIT", "Organisateur existe déjà")
            }
    }

    // Corps
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeEventCounters()
        //initializeOrganizerAccount()

        enableEdgeToEdge()
        setContent {
            EventTicketAppTheme {
                AppNavigation(
                    authViewModel     = authViewModel,
                    homeViewModel     = homeViewModel,
                    categoryViewModel = categoryViewModel,
                    ticketViewModel   = ticketViewModel,
                    onGoogleSignIn    = { launchGoogleSignIn() },
                    onFacebookSignIn  = { launchFacebookSignIn() }
                )
            }
        }
    }
}