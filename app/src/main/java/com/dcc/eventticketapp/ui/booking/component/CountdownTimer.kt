package com.dcc.eventticketapp.ui.booking.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dcc.eventticketapp.ui.theme.OrangeLight
import com.dcc.eventticketapp.ui.theme.OrangeMain
import kotlinx.coroutines.delay

@Composable
fun CountdownTimer(expiryMillis: Long, onExpired: () -> Unit) {
    var remaining by remember { mutableLongStateOf(expiryMillis - System.currentTimeMillis()) }

    LaunchedEffect(expiryMillis) {
        while (remaining > 0) {
            delay(1000)
            remaining = expiryMillis - System.currentTimeMillis()
        }
        onExpired()
    }

    val hours   = (remaining / 1000 / 3600).coerceAtLeast(0)
    val minutes = (remaining / 1000 % 3600 / 60).coerceAtLeast(0)
    val seconds = (remaining / 1000 % 60).coerceAtLeast(0)

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = OrangeLight
    ) {
        Row(
            modifier          = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text     = "Réservation expire dans ",
                fontSize = 13.sp,
                color    = OrangeMain
            )
            Text(
                text       = "%02d:%02d:%02d".format(hours, minutes, seconds),
                fontSize   = 14.sp,
                fontWeight = FontWeight.Bold,
                color      = OrangeMain
            )
        }
    }
}