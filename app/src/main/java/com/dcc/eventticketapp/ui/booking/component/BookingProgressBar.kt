package com.dcc.eventticketapp.ui.booking.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dcc.eventticketapp.ui.booking.BookingStep
import com.dcc.eventticketapp.ui.theme.OrangeMain
import com.dcc.eventticketapp.ui.theme.TextGrayMode

@Composable
fun BookingProgressBar(currentStep: BookingStep) {
    val steps = listOf(
        "Résumé"   to BookingStep.SUMMARY,
        "Paiement" to BookingStep.PAYMENT,
        "Confirmé" to BookingStep.SUCCESS
    )

    Row(
        modifier              = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        steps.forEachIndexed { index, (label, step) ->
            val isActive    = step == currentStep
            val isCompleted = steps.indexOfFirst { it.second == currentStep } > index

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(
                            when {
                                isCompleted -> OrangeMain
                                isActive    -> OrangeMain
                                else        -> MaterialTheme.colorScheme.surfaceVariant
                            },
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text       = "${index + 1}",
                        fontSize   = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color      = if (isCompleted || isActive) Color.White else TextGrayMode
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text       = label,
                    fontSize   = 11.sp,
                    fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal,
                    color      = if (isActive) OrangeMain else TextGrayMode
                )
            }

            if (index < steps.lastIndex) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(2.dp)
                        .background(
                            if (isCompleted) OrangeMain
                            else MaterialTheme.colorScheme.surfaceVariant
                        )
                )
            }
        }
    }
}