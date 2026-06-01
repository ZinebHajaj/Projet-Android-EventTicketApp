package com.dcc.eventticketapp.ui.auth.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dcc.eventticketapp.ui.theme.TextDarkMode
import com.dcc.eventticketapp.ui.theme.TextGrayMode

@Composable
fun AuthHeader(
    title: String,
    subtitle: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        // Logo
        AppLogo(
            boxSize  = 72.dp,   // ← taille auth
            iconSize = 50.dp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Titre
        Text(
            text = title,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = TextDarkMode
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Sous-titre
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = TextGrayMode,
            textAlign = TextAlign.Center
        )
    }
}