package com.dcc.eventticketapp.ui.booking.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.stripe.android.paymentsheet.rememberPaymentSheet
import com.dcc.eventticketapp.ui.booking.BookingIntent
import com.dcc.eventticketapp.ui.booking.BookingViewModel
import com.dcc.eventticketapp.ui.booking.PaymentMethod
import com.dcc.eventticketapp.ui.theme.OrangeMain
import com.dcc.eventticketapp.ui.theme.TextGrayMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    onBack           : () -> Unit,
    onPaymentSuccess : () -> Unit,
    onPayPalClick    : () -> Unit,
    viewModel        : BookingViewModel
) {
    val state   by viewModel.state.collectAsState()
    val context = LocalContext.current

    // ── Stripe PaymentSheet ──────────────────────────────────────
    val paymentSheet = rememberPaymentSheet { result ->
        when (result) {
            is PaymentSheetResult.Completed -> {
                viewModel.handleIntent(BookingIntent.ConfirmPayment)
            }
            is PaymentSheetResult.Failed -> {
                viewModel.handleIntent(
                    BookingIntent.StripeError(result.error.message ?: "Erreur paiement")
                )
            }
            is PaymentSheetResult.Canceled -> { /* rien */ }
        }
    }

    // Naviguer vers confirmation si succès
    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) onPaymentSuccess()
    }

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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Badge sécurité
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.Lock, null,
                    tint = OrangeMain, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Paiement 100% sécurisé", fontSize = 13.sp, color = TextGrayMode)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Total
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape    = RoundedCornerShape(16.dp),
                color    = OrangeMain.copy(alpha = 0.1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Montant total", fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Text(
                        text = com.dcc.eventticketapp.utils.CurrencyConverter.formatPrice(
                            state.totalPrice,
                            state.currency
                        ),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = OrangeMain
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (state.error != null) {
                Text(state.error!!, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(16.dp))
            }

            // ── Bouton selon le mode de paiement ────────────────
            Button(
                onClick = {
                    when (state.selectedPaymentMethod) {
                        PaymentMethod.CARD -> {
                            viewModel.createPaymentIntent { clientSecret ->
                                val configuration = PaymentSheet.Configuration(
                                    merchantDisplayName = "TicketGo"
                                )
                                paymentSheet.presentWithPaymentIntent(
                                    clientSecret,
                                    configuration
                                )
                            }
                        }
                        PaymentMethod.PAYPAL -> {
                            viewModel.createPayPalOrder {
                                onPayPalClick()
                            }
                        }
                    }
                },
                enabled  = !state.isLoading,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape    = RoundedCornerShape(12.dp),
                colors   = ButtonDefaults.buttonColors(
                    containerColor         = OrangeMain,
                    disabledContainerColor = OrangeMain.copy(alpha = 0.4f)
                )
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier    = Modifier.size(22.dp),
                        color       = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = when (state.selectedPaymentMethod) {
                            PaymentMethod.CARD   -> "Payer par carte"
                            PaymentMethod.PAYPAL -> "Payer avec PayPal"
                        },
                        fontSize   = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}


/*
package com.dcc.eventticketapp.ui.booking.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dcc.eventticketapp.ui.booking.BookingIntent
import com.dcc.eventticketapp.ui.booking.BookingViewModel
import com.dcc.eventticketapp.ui.booking.component.BookingProgressBar
import com.dcc.eventticketapp.ui.theme.ErrorLight
import com.dcc.eventticketapp.ui.theme.OrangeMain
import com.dcc.eventticketapp.ui.theme.TextGrayMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    onBack         : () -> Unit,
    onPaymentSuccess : () -> Unit,
    viewModel      : BookingViewModel
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onPaymentSuccess()
        }
    }

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

            Column(modifier = Modifier.padding(20.dp)) {

                // Badge sécurité
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Outlined.Lock, null,
                        tint = OrangeMain, modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Paiement 100% sécurisé",
                        fontSize = 13.sp,
                        color = TextGrayMode
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Total à payer
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = OrangeMain.copy(alpha = 0.1f)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Montant total",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "${state.totalPrice.toInt()} MAD",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = OrangeMain
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Numéro de carte
                OutlinedTextField(
                    value = state.cardNumber,
                    onValueChange = {
                        viewModel.handleIntent(BookingIntent.CardNumberChanged(it))
                    },
                    label = { Text("Numéro de carte") },
                    placeholder = { Text("1234 5678 9012 3456") },
                    leadingIcon = {
                        Icon(Icons.Outlined.CreditCard, null, tint = OrangeMain)
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = OrangeMain,
                        focusedLabelColor = OrangeMain,
                        cursorColor = OrangeMain
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Nom du titulaire
                OutlinedTextField(
                    value = state.cardHolder,
                    onValueChange = {
                        viewModel.handleIntent(BookingIntent.CardHolderChanged(it))
                    },
                    label = { Text("Nom du titulaire") },
                    leadingIcon = {
                        Icon(Icons.Outlined.Person, null, tint = OrangeMain)
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = OrangeMain,
                        focusedLabelColor = OrangeMain,
                        cursorColor = OrangeMain
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Expiration + CVV
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = state.expiry,
                        onValueChange = {
                            viewModel.handleIntent(BookingIntent.ExpiryChanged(it))
                        },
                        label = { Text("MM/AA") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = OrangeMain,
                            focusedLabelColor = OrangeMain,
                            cursorColor = OrangeMain
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    OutlinedTextField(
                        value = state.cvv,
                        onValueChange = {
                            viewModel.handleIntent(BookingIntent.CvvChanged(it))
                        },
                        label = { Text("CVV") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = OrangeMain,
                            focusedLabelColor = OrangeMain,
                            cursorColor = OrangeMain
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }

                if (state.error != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = state.error!!,
                        color = ErrorLight,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Bouton payer
                Button(
                    onClick = {
                        viewModel.handleIntent(BookingIntent.ConfirmPayment)
                    },
                    enabled = state.isPaymentValid && !state.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = OrangeMain,
                        disabledContainerColor = OrangeMain.copy(alpha = 0.4f)
                    )
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Payer ${state.totalPrice.toInt()} MAD",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

    }
}
*/