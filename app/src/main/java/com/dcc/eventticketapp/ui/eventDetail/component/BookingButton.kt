package com.dcc.eventticketapp.ui.eventDetail.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dcc.eventticketapp.ui.theme.OrangeMain
import com.dcc.eventticketapp.ui.theme.SuccessColor

@Composable
fun BookingButton(
    isBooked : Boolean,
    onClick  : () -> Unit
) {
    Button(
        onClick  = onClick,
        enabled  = !isBooked,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .height(52.dp),
        shape  = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor         = if (isBooked) SuccessColor else OrangeMain,
            contentColor           = Color.White,
            disabledContainerColor = SuccessColor,
            disabledContentColor   = Color.White
        )
    ) {
        Text(
            text       = if (isBooked) "Réservé !" else "Réserver maintenant",
            fontSize   = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}