package com.dcc.eventticketapp.ui.ticket.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.QrCode
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.dcc.eventticketapp.data.Entities.TicketModel
import com.dcc.eventticketapp.data.Entities.TicketStatus
import com.dcc.eventticketapp.ui.theme.OrangeLight
import com.dcc.eventticketapp.ui.theme.OrangeMain
import com.dcc.eventticketapp.ui.theme.SuccessColor
import com.dcc.eventticketapp.ui.theme.TextGrayMode

@Composable
fun TicketCard(
    ticket  : TicketModel,
    onClick : () -> Unit
) {
    Surface(
        modifier        = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { onClick() },
        shape           = RoundedCornerShape(16.dp),
        color           = MaterialTheme.colorScheme.surface,
        shadowElevation = 3.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Image + infos
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model              = ticket.eventImage,
                    contentDescription = ticket.eventTitle,
                    modifier           = Modifier
                        .size(70.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale       = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text       = ticket.eventTitle,
                        fontSize   = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color      = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.CalendarMonth, null,
                            modifier = Modifier.size(12.dp), tint = TextGrayMode)
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(ticket.eventDate, fontSize = 12.sp, color = TextGrayMode)
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.LocationOn, null,
                            modifier = Modifier.size(12.dp), tint = TextGrayMode)
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(ticket.eventCity, fontSize = 12.sp, color = TextGrayMode)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Prix + statut + bouton QR
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text(
                    text = com.dcc.eventticketapp.utils.CurrencyConverter.formatPrice(
                        ticket.price,
                        ticket.currency
                    ),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = OrangeMain
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Badge statut
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = when (ticket.status) {
                            TicketStatus.VALID   -> SuccessColor.copy(alpha = 0.15f)
                            TicketStatus.USED    -> TextGrayMode.copy(alpha = 0.15f)
                            TicketStatus.EXPIRED -> Color.Red.copy(alpha = 0.15f)
                        }
                    ) {
                        Text(
                            text = when (ticket.status) {
                                TicketStatus.VALID   -> "Valide"
                                TicketStatus.USED    -> "Utilisé"
                                TicketStatus.EXPIRED -> "Expiré"
                            },
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = when (ticket.status) {
                                TicketStatus.VALID   -> SuccessColor
                                TicketStatus.USED    -> TextGrayMode
                                TicketStatus.EXPIRED -> Color.Red
                            },
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Bouton QR
                    if (ticket.status == TicketStatus.VALID) {
                        Surface(
                            shape    = RoundedCornerShape(8.dp),
                            color    = OrangeLight,
                            modifier = Modifier.clickable { onClick() }
                        ) {
                            Icon(
                                Icons.Outlined.QrCode,
                                contentDescription = "QR Code",
                                tint               = OrangeMain,
                                modifier           = Modifier
                                    .padding(8.dp)
                                    .size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}