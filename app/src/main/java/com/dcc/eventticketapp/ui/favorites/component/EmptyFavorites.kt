package com.dcc.eventticketapp.ui.favorites.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dcc.eventticketapp.ui.theme.OrangeMain

@Composable
fun EmptyFavorites(
    textPrimary : Color,
    textSecond  : Color,
    onExplore   : () -> Unit
) {
    Column(
        modifier              = Modifier
            .fillMaxWidth()
            .padding(40.dp),
        horizontalAlignment   = Alignment.CenterHorizontally,
        verticalArrangement   = Arrangement.Center
    ) {
        // Icône grande
        Surface(
            shape = androidx.compose.foundation.shape.CircleShape,
            color = OrangeMain.copy(alpha = 0.1f),
            modifier = Modifier.size(100.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    Icons.Outlined.FavoriteBorder,
                    contentDescription = null,
                    tint     = OrangeMain,
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text       = "Aucun favori pour l'instant",
            fontSize   = 18.sp,
            fontWeight = FontWeight.Bold,
            color      = textPrimary,
            textAlign  = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text      = "Explorez des événements et ajoutez\nvos préférés ici !",
            fontSize  = 14.sp,
            color     = textSecond,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onExplore,
            colors  = ButtonDefaults.buttonColors(
                containerColor = OrangeMain
            ),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(30.dp)
        ) {
            Text(
                "Explorer les événements",
                color    = Color.White,
                fontSize = 14.sp
            )
        }
    }
}