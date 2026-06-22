package com.dcc.eventticketapp.ui.booking.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dcc.eventticketapp.ui.booking.BookingIntent
import com.dcc.eventticketapp.ui.booking.BookingViewModel
import com.dcc.eventticketapp.ui.booking.PaymentMethod
import com.dcc.eventticketapp.ui.booking.component.BookingProgressBar
import com.dcc.eventticketapp.ui.booking.component.CountdownTimer
import com.dcc.eventticketapp.ui.theme.OrangeMain
import com.dcc.eventticketapp.ui.theme.TextGrayMode
import com.dcc.eventticketapp.utils.CurrencyConverter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentMethodScreen(
    viewModel    : BookingViewModel,
    onBack       : () -> Unit,
    onContinue   : () -> Unit,
    onExpired    : () -> Unit
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text("Paiement", fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Outlined.ArrowBack, "Retour",
                            tint = MaterialTheme.colorScheme.onBackground)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->

        Column(modifier = Modifier.fillMaxSize().padding(padding)) {

            BookingProgressBar(currentStep = state.currentStep)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // ── Compte à rebours ─────────────────────────────
                if (state.reservationExpiryMillis > 0) {
                    CountdownTimer(
                        expiryMillis = state.reservationExpiryMillis,
                        onExpired    = onExpired
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }

                // ── Montant total ────────────────────────────────
                Text(
                    text       = CurrencyConverter.formatPrice(state.totalPrice, state.currency),
                    fontSize   = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color      = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text     = "${state.quantity} billet(s) — ${state.event?.title ?: ""}",
                    fontSize = 13.sp,
                    color    = TextGrayMode
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── Modes de paiement ─────────────────────────────────
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape           = RoundedCornerShape(16.dp),
                color           = MaterialTheme.colorScheme.surface,
                shadowElevation = 2.dp
            ) {
                Column {

                    PaymentMethodRow(
                        icon       = "💳",
                        title      = "Carte bancaire",
                        subtitle   = "Visa, Mastercard",
                        isSelected = state.selectedPaymentMethod == PaymentMethod.CARD,
                        onClick    = {
                            viewModel.handleIntent(
                                BookingIntent.SelectPaymentMethod(PaymentMethod.CARD)
                            )
                        }
                    )

                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                    PaymentMethodRow(
                        icon       = "🅿️",
                        title      = "PayPal",
                        subtitle   = "Des frais supplémentaires peuvent s'appliquer",
                        isSelected = state.selectedPaymentMethod == PaymentMethod.PAYPAL,
                        onClick    = {
                            viewModel.handleIntent(
                                BookingIntent.SelectPaymentMethod(PaymentMethod.PAYPAL)
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // ── Conditions ────────────────────────────────────────
            Text(
                text      = "En continuant, vous acceptez nos Conditions d'utilisation et notre Politique de confidentialité",
                fontSize  = 11.sp,
                color     = TextGrayMode,
                modifier  = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
            )

            // ── Bouton continuer ──────────────────────────────────
            Surface(
                modifier        = Modifier.fillMaxWidth(),
                color           = MaterialTheme.colorScheme.surface,
                shadowElevation = 8.dp
            ) {
                Button(
                    onClick  = onContinue,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .height(52.dp),
                    shape  = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = OrangeMain)
                ) {
                    Text("Continuer", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun PaymentMethodRow(
    icon       : String,
    title      : String,
    subtitle   : String,
    isSelected : Boolean,
    onClick    : () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = icon, fontSize = 24.sp)
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text       = title,
                fontSize   = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color      = MaterialTheme.colorScheme.onBackground
            )
            Text(text = subtitle, fontSize = 12.sp, color = TextGrayMode)
        }
        Box(
            modifier = Modifier
                .size(20.dp)
                .background(
                    if (isSelected) OrangeMain else Color.Transparent,
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (!isSelected) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(Color.Transparent, CircleShape)
                )
            }
        }
    }
}