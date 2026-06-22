package com.dcc.eventticketapp.ui.booking.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.dcc.eventticketapp.data.Entities.EventModel
import com.dcc.eventticketapp.ui.theme.OrangeMain
import com.dcc.eventticketapp.ui.theme.TextGrayMode
import com.dcc.eventticketapp.utils.CurrencyConverter

@Composable
fun BookingSummaryCard(
    event    : EventModel,
    currency : String = "MAD"
) {
    Surface(
        modifier        = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape           = RoundedCornerShape(16.dp),
        color           = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier          = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model              = event.imageUrl,
                contentDescription = event.title,
                modifier           = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale       = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text       = event.title,
                    fontSize   = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color      = MaterialTheme.colorScheme.onBackground,
                    maxLines   = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.CalendarMonth, null,
                        modifier = Modifier.size(12.dp), tint = TextGrayMode)
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(event.date, fontSize = 12.sp, color = TextGrayMode)
                }
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.LocationOn, null,
                        modifier = Modifier.size(12.dp), tint = TextGrayMode)
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(event.city, fontSize = 12.sp, color = TextGrayMode)
                }
            }
            Text(
                text       = CurrencyConverter.formatPrice(event.priceStandard, currency),
                fontSize   = 16.sp,
                fontWeight = FontWeight.Bold,
                color      = OrangeMain
            )
        }
    }
}