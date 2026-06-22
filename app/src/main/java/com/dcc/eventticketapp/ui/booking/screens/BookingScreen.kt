package com.dcc.eventticketapp.ui.booking.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dcc.eventticketapp.ui.booking.BookingIntent
import com.dcc.eventticketapp.ui.booking.BookingViewModel
import com.dcc.eventticketapp.ui.booking.component.BookingProgressBar
import com.dcc.eventticketapp.ui.booking.component.BookingSummaryCard
import com.dcc.eventticketapp.ui.booking.component.CurrencySelector
import com.dcc.eventticketapp.ui.booking.component.PriceBreakdownCard
import com.dcc.eventticketapp.ui.booking.component.SeatTierCard
import com.dcc.eventticketapp.ui.booking.component.TimeSlotCard
import com.dcc.eventticketapp.ui.theme.OrangeMain
import com.dcc.eventticketapp.ui.theme.TextGrayMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    eventId   : String,
    onBack    : () -> Unit,
    onProceed : () -> Unit,
    viewModel : BookingViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var showExitDialog by remember { mutableStateOf(false) }

    LaunchedEffect(eventId) {
        viewModel.handleIntent(BookingIntent.LoadEvent(eventId))
    }

    val hasSelection = state.selectedTierId != null ||
            state.selectedSlotId != null ||
            state.quantity > 1

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text("Réservation", fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground)
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (hasSelection) showExitDialog = true else onBack()
                    }) {
                        Icon(Icons.Outlined.ArrowBack, "Retour",
                            tint = MaterialTheme.colorScheme.onBackground)
                    }
                },
                actions = {
                    CurrencySelector(
                        selectedCurrency = state.currency,
                        onCurrencyChange = {
                            viewModel.handleIntent(BookingIntent.ChangeCurrency(it))
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->

        // ── État loading ────────────────────────────────────────
        if (state.isLoading) {
            Box(
                modifier         = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator(color = OrangeMain) }
            return@Scaffold
        }

        // ── État non connecté ───────────────────────────────────
        if (state.requiresAuth) {
            Box(
                modifier         = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text       = "Connexion requise",
                        fontSize   = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color      = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text     = "Connectez-vous pour réserver vos billets",
                        fontSize = 14.sp,
                        color    = TextGrayMode
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = onBack,
                        colors  = ButtonDefaults.buttonColors(containerColor = OrangeMain),
                        shape   = RoundedCornerShape(12.dp)
                    ) { Text("Retour") }
                }
            }
            return@Scaffold
        }

        // ── État erreur réseau ──────────────────────────────────
        if (state.error != null && state.event == null) {
            Box(
                modifier         = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text     = "Erreur : ${state.error}",
                        color    = MaterialTheme.colorScheme.error,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.handleIntent(BookingIntent.LoadEvent(eventId)) },
                        colors  = ButtonDefaults.buttonColors(containerColor = OrangeMain),
                        shape   = RoundedCornerShape(12.dp)
                    ) { Text("Réessayer") }
                }
            }
            return@Scaffold
        }

        if (state.event == null) return@Scaffold

        val event = state.event!!

        val maxAvailable = state.selectedTier?.availableSeats
            ?: state.selectedSlot?.available
            ?: 6
        val isMaxReached = state.quantity >= maxAvailable

        Column(modifier = Modifier.fillMaxSize().padding(padding)) {

            BookingProgressBar(currentStep = state.currentStep)

            LazyColumn(modifier = Modifier.weight(1f)) {

                item {
                    BookingSummaryCard(event = event, currency = state.currency)
                    Spacer(modifier = Modifier.height(20.dp))
                }

                if (event.requiresSeatSelection) {

                    item {
                        Text(
                            text       = "Choisissez votre catégorie",
                            fontSize   = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color      = MaterialTheme.colorScheme.onBackground,
                            modifier   = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    items(state.seatTiers) { tier ->
                        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                            SeatTierCard(
                                tier       = tier,
                                isSelected = state.selectedTierId == tier.tierId,
                                currency   = state.currency,
                                onClick    = {
                                    viewModel.handleIntent(BookingIntent.SelectTier(tier.tierId))
                                }
                            )
                        }
                    }

                } else {

                    item {
                        Text(
                            text       = "Choisissez un créneau",
                            fontSize   = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color      = MaterialTheme.colorScheme.onBackground,
                            modifier   = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        LazyRow(
                            modifier              = Modifier.padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(state.timeSlots) { slot ->
                                TimeSlotCard(
                                    slot       = slot,
                                    isSelected = state.selectedSlotId == slot.slotId,
                                    onClick    = {
                                        viewModel.handleIntent(BookingIntent.SelectSlot(slot.slotId))
                                    }
                                )
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(20.dp)) }

                item {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape    = RoundedCornerShape(16.dp),
                        color    = MaterialTheme.colorScheme.surface,
                        shadowElevation = 2.dp
                    ) {
                        Column {
                            Row(
                                modifier              = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment     = Alignment.CenterVertically
                            ) {
                                Text(
                                    text       = "Nombre de billets",
                                    fontSize   = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color      = MaterialTheme.colorScheme.onBackground
                                )

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = {
                                            viewModel.handleIntent(BookingIntent.DecreaseQuantity)
                                        },
                                        modifier = Modifier
                                            .size(36.dp)
                                            .background(
                                                MaterialTheme.colorScheme.surfaceVariant,
                                                CircleShape
                                            )
                                    ) {
                                        Icon(Icons.Outlined.Remove, "Diminuer",
                                            tint = MaterialTheme.colorScheme.onBackground,
                                            modifier = Modifier.size(18.dp))
                                    }

                                    Text(
                                        text       = "${state.quantity}",
                                        fontSize   = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color      = MaterialTheme.colorScheme.onBackground,
                                        modifier   = Modifier.padding(horizontal = 20.dp)
                                    )

                                    IconButton(
                                        onClick = {
                                            viewModel.handleIntent(BookingIntent.IncreaseQuantity())
                                        },
                                        enabled  = !isMaxReached,
                                        modifier = Modifier
                                            .size(36.dp)
                                            .background(
                                                if (isMaxReached) MaterialTheme.colorScheme.surfaceVariant else OrangeMain,
                                                CircleShape
                                            )
                                    ) {
                                        Icon(Icons.Outlined.Add, "Augmenter",
                                            tint = if (isMaxReached) TextGrayMode else Color.White,
                                            modifier = Modifier.size(18.dp))
                                    }
                                }
                            }

                            if (isMaxReached) {
                                Text(
                                    text     = "Stock maximum atteint pour cette catégorie",
                                    fontSize = 11.sp,
                                    color    = TextGrayMode,
                                    modifier = Modifier.padding(start = 20.dp, bottom = 12.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }

                item {
                    PriceBreakdownCard(state = state, viewModel = viewModel)
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            Surface(
                modifier        = Modifier.fillMaxWidth(),
                color           = MaterialTheme.colorScheme.surface,
                shadowElevation = 8.dp
            ) {
                Button(
                    onClick = {
                        viewModel.handleIntent(BookingIntent.ProceedToPayment)
                        onProceed()
                    },
                    enabled  = state.canProceed,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .height(52.dp),
                    shape  = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor         = OrangeMain,
                        disabledContainerColor = OrangeMain.copy(alpha = 0.4f)
                    )
                ) {
                    Text("Continuer vers le paiement", fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = {
                Text("Quitter la réservation ?", fontWeight = FontWeight.Bold)
            },
            text = {
                Text("Votre sélection sera perdue si vous quittez maintenant.")
            },
            confirmButton = {
                TextButton(onClick = {
                    showExitDialog = false
                    onBack()
                }) { Text("Quitter", color = OrangeMain) }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text("Continuer ma réservation")
                }
            }
        )
    }
}