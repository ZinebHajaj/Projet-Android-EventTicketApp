package com.dcc.eventticketapp.ui.booking.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dcc.eventticketapp.data.Entities.SeatTier
import com.dcc.eventticketapp.ui.theme.ErrorLight
import com.dcc.eventticketapp.ui.theme.OrangeLight
import com.dcc.eventticketapp.ui.theme.OrangeMain
import com.dcc.eventticketapp.ui.theme.SuccessColor
import com.dcc.eventticketapp.ui.theme.TextGrayMode
import com.dcc.eventticketapp.ui.theme.WarningColor
import com.dcc.eventticketapp.utils.CurrencyConverter

@Composable
fun SeatTierCard(
    tier        : SeatTier,
    isSelected  : Boolean,
    currency    : String,
    onClick     : () -> Unit
) {
    val isLowStock = !tier.isSoldOut && tier.availableSeats <= (tier.totalSeats * 0.15).toInt()

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable(enabled = !tier.isSoldOut) { onClick() }
            .alpha(if (tier.isSoldOut) 0.5f else 1f),
        shape           = RoundedCornerShape(16.dp),
        color           = if (isSelected) OrangeLight else MaterialTheme.colorScheme.surface,
        border          = if (isSelected)
            androidx.compose.foundation.BorderStroke(2.dp, OrangeMain)
        else null,
        shadowElevation = if (isSelected) 0.dp else 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isSelected) Icons.Filled.CheckCircle
                        else Icons.Outlined.Circle,
                        contentDescription = null,
                        tint     = if (isSelected) OrangeMain else TextGrayMode,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text       = tier.name,
                        fontSize   = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color      = MaterialTheme.colorScheme.onBackground
                    )
                }

                Text(
                    text       = CurrencyConverter.formatPrice(tier.price, currency),
                    fontSize   = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color      = OrangeMain
                )
            }

            if (tier.benefits.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Column(modifier = Modifier.padding(start = 32.dp)) {
                    tier.benefits.forEach { benefit ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(4.dp)
                                    .background(TextGrayMode, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text     = benefit,
                                fontSize = 12.sp,
                                color    = TextGrayMode
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ── Badge de disponibilité ──────────────────────────
            Row(
                modifier = Modifier.padding(start = 32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                when {
                    tier.isSoldOut -> {
                        Badge(
                            text  = "Épuisé",
                            color = ErrorLight
                        )
                    }
                    isLowStock -> {
                        Badge(
                            text  = "Plus que ${tier.availableSeats} places !",
                            color = WarningColor
                        )
                    }
                    else -> {
                        Badge(
                            text  = "${tier.availableSeats} places disponibles",
                            color = SuccessColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Badge(text: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = color.copy(alpha = 0.15f)
    ) {
        Text(
            text       = text,
            fontSize   = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color      = color,
            modifier   = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}