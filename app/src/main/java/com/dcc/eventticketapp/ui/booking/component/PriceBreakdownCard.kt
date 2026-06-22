package com.dcc.eventticketapp.ui.booking.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dcc.eventticketapp.ui.booking.BookingIntent
import com.dcc.eventticketapp.ui.booking.BookingViewModel
import com.dcc.eventticketapp.ui.booking.BookingViewState
import com.dcc.eventticketapp.ui.theme.ErrorLight
import com.dcc.eventticketapp.ui.theme.OrangeMain
import com.dcc.eventticketapp.ui.theme.SuccessColor
import com.dcc.eventticketapp.ui.theme.TextGrayMode
import com.dcc.eventticketapp.utils.CurrencyConverter

@Composable
fun PriceBreakdownCard(
    state    : BookingViewState,
    viewModel : BookingViewModel
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape           = RoundedCornerShape(16.dp),
        color           = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // ── Code promo ──────────────────────────────────────
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value         = state.promoCode,
                    onValueChange = {
                        viewModel.handleIntent(BookingIntent.PromoCodeChanged(it))
                    },
                    label       = { Text("Code promo") },
                    placeholder = { Text("WELCOME10") },
                    leadingIcon = {
                        Icon(Icons.Outlined.LocalOffer, null, tint = OrangeMain)
                    },
                    singleLine = true,
                    enabled    = !state.promoApplied,
                    isError    = state.promoError != null,
                    shape      = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = OrangeMain,
                        focusedLabelColor  = OrangeMain,
                        cursorColor        = OrangeMain
                    ),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        viewModel.handleIntent(BookingIntent.ApplyPromoCode)
                    },
                    enabled = state.promoCode.isNotBlank() && !state.promoApplied,
                    colors  = ButtonDefaults.buttonColors(containerColor = OrangeMain),
                    shape   = RoundedCornerShape(12.dp)
                ) {
                    Text(if (state.promoApplied) "✓" else "OK")
                }
            }

            if (state.promoError != null) {
                Text(
                    text     = state.promoError,
                    color    = ErrorLight,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            if (state.promoApplied) {
                Text(
                    text     = "Code promo appliqué (-${(state.promoDiscount * 100).toInt()}%)",
                    color    = SuccessColor,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(12.dp))

            // ── Détail du prix ──────────────────────────────────
            PriceRow(
                label    = "Sous-total (${state.quantity}x)",
                amount   = state.subtotal,
                currency = state.currency
            )
            Spacer(modifier = Modifier.height(8.dp))
            PriceRow(
                label    = "Frais de service",
                amount   = state.serviceFee,
                currency = state.currency
            )

            if (state.promoApplied) {
                Spacer(modifier = Modifier.height(8.dp))
                PriceRow(
                    label      = "Réduction",
                    amount     = -state.discountAmount,
                    currency   = state.currency,
                    isDiscount = true
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text       = "Total",
                    fontSize   = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color      = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text       = CurrencyConverter.formatPrice(state.totalPrice, state.currency),
                    fontSize   = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color      = OrangeMain
                )
            }
        }
    }
}

@Composable
private fun PriceRow(
    label      : String,
    amount     : Double,
    currency   : String,
    isDiscount : Boolean = false
) {
    Row(
        modifier              = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 14.sp, color = TextGrayMode)
        Text(
            text     = CurrencyConverter.formatPrice(amount, currency),
            fontSize = 14.sp,
            color    = if (isDiscount) SuccessColor else TextGrayMode
        )
    }
}