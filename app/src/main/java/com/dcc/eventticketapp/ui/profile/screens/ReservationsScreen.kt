package com.dcc.eventticketapp.ui.profile.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.ConfirmationNumber
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationsScreen(
    onBack        : () -> Unit,
    onExplore     : () -> Unit = {}
) {
    val bgColor     = MaterialTheme.colorScheme.background
    val textPrimary = MaterialTheme.colorScheme.onBackground
    val textSecond  = MaterialTheme.colorScheme.onSurfaceVariant

    Scaffold(
        containerColor = bgColor,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Mes réservations", fontSize = 20.sp,
                            fontWeight = FontWeight.Bold, color = textPrimary)
                        Text("0 billets achetés", fontSize = 13.sp, color = textSecond)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Outlined.ArrowBackIosNew, "Retour", tint = textPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = bgColor)
            )
        }
    ) { padding ->

        Box(
            modifier         = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier            = Modifier.padding(40.dp)
            ) {
                Surface(
                    shape    = CircleShape,
                    color    = OrangeMain.copy(alpha = 0.1f),
                    modifier = Modifier.size(100.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Outlined.ConfirmationNumber, null,
                            tint     = OrangeMain,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    "Aucune réservation",
                    fontSize   = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color      = textPrimary,
                    textAlign  = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "Vos billets achetés apparaîtront ici.\nExplorez les événements disponibles !",
                    fontSize  = 14.sp,
                    color     = textSecond,
                    textAlign = TextAlign.Center,
                    lineHeight = 21.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onExplore,
                    colors  = ButtonDefaults.buttonColors(containerColor = OrangeMain),
                    shape   = RoundedCornerShape(30.dp)
                ) {
                    Text("Explorer les événements",
                        color = Color.White, fontSize = 14.sp)
                }
            }
        }
    }
}