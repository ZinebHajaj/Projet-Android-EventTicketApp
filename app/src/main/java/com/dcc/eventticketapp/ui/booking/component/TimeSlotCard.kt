package com.dcc.eventticketapp.ui.booking.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dcc.eventticketapp.data.Entities.TimeSlot
import com.dcc.eventticketapp.ui.theme.OrangeMain
import com.dcc.eventticketapp.ui.theme.TextGrayMode

@Composable
fun TimeSlotCard(
    slot       : TimeSlot,
    isSelected : Boolean,
    onClick    : () -> Unit
) {
    Surface(
        modifier = Modifier
            .clickable(enabled = !slot.isFull) { onClick() }
            .alpha(if (slot.isFull) 0.4f else 1f),
        shape  = RoundedCornerShape(14.dp),
        color  = if (isSelected) OrangeMain else MaterialTheme.colorScheme.surface,
        border = if (!isSelected)
            androidx.compose.foundation.BorderStroke(1.dp, TextGrayMode.copy(alpha = 0.3f))
        else null
    ) {
        Column(
            modifier            = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Outlined.AccessTime,
                contentDescription = null,
                tint     = if (isSelected) androidx.compose.ui.graphics.Color.White else OrangeMain,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text       = slot.time,
                fontSize   = 14.sp,
                fontWeight = FontWeight.Bold,
                color      = if (isSelected) androidx.compose.ui.graphics.Color.White
                else MaterialTheme.colorScheme.onBackground
            )
            Text(
                text     = if (slot.isFull) "Complet" else "${slot.available} places",
                fontSize = 10.sp,
                color    = if (isSelected) androidx.compose.ui.graphics.Color.White.copy(alpha = 0.8f)
                else TextGrayMode
            )
        }
    }
}