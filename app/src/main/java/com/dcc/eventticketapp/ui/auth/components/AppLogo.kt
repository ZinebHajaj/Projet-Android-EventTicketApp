package com.dcc.eventticketapp.ui.auth.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dcc.eventticketapp.ui.theme.OrangeDark
import com.dcc.eventticketapp.ui.theme.OrangeMain

@Composable
fun AppLogo(
    boxSize   : Dp  = 72.dp,
    iconSize  : Dp  = 50.dp,
    cornerRadius: Dp = 18.dp,
    scale     : Float = 1f
) {
    Box(
        modifier = Modifier
            .scale(scale)
            .size(boxSize)
            .clip(RoundedCornerShape(cornerRadius))
            .background(
                Brush.verticalGradient(
                    colors = listOf(OrangeMain, OrangeDark)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector        = Icons.Outlined.ConfirmationNumber,
            contentDescription = "Logo TicketGo",
            tint               = Color.White,
            modifier           = Modifier.size(iconSize)
        )
    }
}