package com.dcc.eventticketapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()
    private val categoryViewModel: CategoryViewModel by viewModels()
    private val eventsViewModel: EventsViewModel by viewModels()
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
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        facebookCallbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    private val prefsViewModel: AppPreferencesViewModel by viewModels()

    // ========== VERSION SYNCHRONE AVEC COROUTINES ==========

    private fun initializeTestEventsSync() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val auth = com.google.firebase.auth.FirebaseAuth.getInstance()
                val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()

                // 1. Se connecter (attendre le résultat)
                Log.d("INIT", "Logging in as organizer...")
                val result = auth.signInWithEmailAndPassword(
                    "organisateur@ticketgo.com",
                    "Organisateur123!"
                ).await()

                val organizerId = result.user?.uid
                if (organizerId == null) {
                    Log.e("INIT", "Login failed: no UID")
                    return@launch
                }
                Log.d("INIT", "Logged in, UID: $organizerId")

                // 2. Vérifier si des événements existent
                Log.d("INIT", "Checking existing events...")
                val snapshot = db.collection("events").get().await()

                if (!snapshot.isEmpty) {
                    Log.d("INIT", "Events already exist (${snapshot.size()}), skipping")
                    snapshot.documents.forEach { doc ->
                        Log.d("INIT", "   - ${doc.id}: ${doc.getString("title")}")
                    }
                    return@launch
                }

                Log.d("INIT", "No events found, creating test events...")

                // 3. Créer les événements
                val now = System.currentTimeMillis()

                val events = listOf(
                    createEventMap("event_001", "Concert Jazz Night", "Casablanca", "15 Juillet 2025", "jazznight", 150.0, "Concerts", organizerId, now),
                    createEventMap("event_002", "Match Raja vs Wydad", "Casablanca", "20 Juillet 2025", "derby", 100.0, "Sports", organizerId, now + 1),
                    createEventMap("event_003", "Théâtre: Le Malade Imaginaire", "Rabat", "25 Juillet 2025", "theatre", 80.0, "Théâtre", organizerId, now + 2),
                    createEventMap("event_004", "Atelier Cuisine Marocaine", "Marrakech", "30 Juillet 2025", "cuisine", 200.0, "Ateliers", organizerId, now + 3),
                    createEventMap("event_005", "Festival Gnaoua", "Essaouira", "5 Août 2025", "gnaoua", 120.0, "Concerts", organizerId, now + 4)
                )

                events.forEachIndexed { index, event ->
                    try {
                        val eventId = event["id"] as String

                        // Créer l'événement
                        db.collection("events").document(eventId).set(event).await()
                        Log.d("INIT", "Event ${index + 1}/${events.size} created: ${event["title"]}")

                        // Créer les compteurs de sièges
                        val tiers = mapOf("vip" to 50, "standard" to 200, "economy" to 300)
                        tiers.forEach { (tierId, total) ->
                            db.collection("events")
                                .document(eventId)
                                .collection("seatTiers")
                                .document(tierId)
                                .set(mapOf("totalSeats" to total, "bookedSeats" to 0))
                                .await()
                        }
                        Log.d("INIT", "   Seat tiers created")

                    } catch (e: Exception) {
                        Log.e("INIT", "Error creating event ${event["id"]}: ${e.message}")
                    }
                }

                Log.d("INIT", "All events created successfully!")

            } catch (e: Exception) {
                Log.e("INIT", "Fatal error: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    private fun createEventMap(
        id: String,
        title: String,
        city: String,
        date: String,
        imageSeed: String,
        price: Double,
        category: String,
        organizerId: String,
        timestamp: Long
    ): HashMap<String, Any> {
        return hashMapOf(
            "id" to id,
            "title" to title,
            "city" to city,
            "date" to date,
            "imageUrl" to "https://picsum.photos/seed/$imageSeed/400/300",
            "priceStandard" to price,
            "category" to category,
            "organizerId" to organizerId,
            "createdAt" to timestamp,
            "updatedAt" to timestamp,
            "isFavorite" to false
        )
    }

    // ========== CORPS DE L'ACTIVITÉ ==========

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // === CRÉER LES ÉVÉNEMENTS (exécuter une fois, puis commenter) ===
        //initializeTestEventsSync()

        enableEdgeToEdge()

        setContent {
            val prefsState by prefsViewModel.state.collectAsState()
            EventTicketAppTheme(darkTheme = prefsState.isDarkMode) {
                AppNavigation(
                    authViewModel = authViewModel,
                    homeViewModel = homeViewModel,
                    categoryViewModel = categoryViewModel,
                    eventsViewModel = eventsViewModel,
                    favoritesViewModel = favoritesViewModel,
                    prefsViewModel = prefsViewModel,
                    ticketViewModel = ticketViewModel,
                    onGoogleSignIn = { launchGoogleSignIn() },
                    onFacebookSignIn = { launchFacebookSignIn() }
                )
            }
        }
    }
}

/*
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

*/