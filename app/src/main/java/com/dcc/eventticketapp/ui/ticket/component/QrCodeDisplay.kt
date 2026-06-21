package com.dcc.eventticketapp.ui.ticket.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dcc.eventticketapp.ui.theme.TextGrayMode
import com.dcc.eventticketapp.utils.generateQrCode

@Composable
fun QrCodeDisplay(
    qrContent : String,
    ticketId  : String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier            = Modifier.fillMaxWidth()
    ) {
        // QR Code généré
        val qrBitmap = remember(qrContent) {
            generateQrCode(qrContent)
        }

        if (qrBitmap != null) {
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    bitmap             = qrBitmap.asImageBitmap(),
                    contentDescription = "QR Code",
                    modifier           = Modifier.size(180.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text      = "ID : $ticketId",
            fontSize  = 13.sp,
            color     = TextGrayMode,
            textAlign = TextAlign.Center
        )
    }
}