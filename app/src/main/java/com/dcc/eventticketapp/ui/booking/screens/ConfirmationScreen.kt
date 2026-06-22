package com.dcc.eventticketapp.ui.booking.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dcc.eventticketapp.ui.booking.BookingViewModel
import com.dcc.eventticketapp.ui.booking.component.BookingProgressBar
import com.dcc.eventticketapp.ui.theme.OrangeMain
import com.dcc.eventticketapp.ui.theme.SuccessColor
import com.dcc.eventticketapp.ui.theme.TextGrayMode
import com.dcc.eventticketapp.utils.CurrencyConverter
import com.dcc.eventticketapp.utils.generateQrCode

@Composable
fun ConfirmationScreen(
    viewModel    : BookingViewModel,
    onGoToTickets : () -> Unit,
    onGoHome      : () -> Unit
) {
    val state by viewModel.state.collectAsState()

    val qrContent = "TICKET-${state.ticketId}-${state.event?.id}"
    val qrBitmap = remember(qrContent) { generateQrCode(qrContent) }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.fillMaxSize()) {

            BookingProgressBar(currentStep = state.currentStep)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                // Icône succès
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .background(SuccessColor.copy(alpha = 0.15f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.CheckCircle,
                        contentDescription = null,
                        tint = SuccessColor,
                        modifier = Modifier.size(56.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Réservation confirmée !",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Votre billet a été généré avec succès",
                    fontSize = 14.sp,
                    color = TextGrayMode,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Montant payé : ${CurrencyConverter.formatPrice(state.totalPrice, state.currency)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = OrangeMain
                )

                Spacer(modifier = Modifier.height(28.dp))

                // QR Code
                if (qrBitmap != null) {
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .background(Color.White, RoundedCornerShape(16.dp))
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            bitmap = qrBitmap.asImageBitmap(),
                            contentDescription = "QR Code",
                            modifier = Modifier.size(176.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "ID : ${state.ticketId}",
                    fontSize = 13.sp,
                    color = TextGrayMode
                )

                Spacer(modifier = Modifier.height(40.dp))

                Button(
                    onClick = onGoToTickets,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = OrangeMain)
                ) {
                    Text(
                        "Voir mes billets", fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(onClick = onGoHome) {
                    Text("Retour à l'accueil", color = TextGrayMode)
                }
            }
        }
    }
}