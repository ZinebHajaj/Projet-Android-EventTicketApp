package com.dcc.eventticketapp.ui.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dcc.eventticketapp.data.Entities.User
import com.dcc.eventticketapp.ui.theme.OrangeMain
@Composable
fun AvatarSection(
    user        : User?,
    textPrimary : Color,
    textSecond  : Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(OrangeMain)
                .border(3.dp, Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
                Text(
                    text = user?.fullName
                        ?.split(" ")
                        ?.take(2)
                        ?.joinToString("") { it.first().uppercase() }
                        ?: "?",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = user?.fullName ?: "Nom non défini",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = textPrimary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = user?.email ?: "email@exemple.com",
            fontSize = 14.sp,
            color = textSecond
        )
        Spacer(modifier = Modifier.height(8.dp))

        Surface(shape = RoundedCornerShape(20.dp), color = OrangeMain) {
            Text(
                "✓ Compte vérifié",
                fontSize = 12.sp, color = Color.White,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp))
        }
    }
}