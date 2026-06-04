package com.dcc.eventticketapp.ui.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dcc.eventticketapp.ui.theme.OrangeMain  // ← depuis Color.kt

@Composable
fun StatsSection(
    reservationsCount : Int,
    favoritesCount    : Int,
    eventsCount       : Int,
    surfaceColor      : Color,
    textSecond        : Color
) {
    Surface(
        modifier        = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape           = RoundedCornerShape(20.dp),
        color           = surfaceColor,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier              = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem(reservationsCount.toString(), "Réservations", textSecond)
            VerticalDiv()
            StatItem(favoritesCount.toString(),    "Favoris",      textSecond)
            VerticalDiv()
            StatItem(eventsCount.toString(),       "Événements",   textSecond)
        }
    }
}

@Composable
fun StatItem(value: String, label: String, textSecond: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontSize = 24.sp,
            fontWeight = FontWeight.Bold, color = OrangeMain)
        Text(label, fontSize = 12.sp, color = textSecond)
    }
}

@Composable
fun VerticalDiv() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(40.dp)
            .background(Color(0xFFEEEEEE))
    )
}