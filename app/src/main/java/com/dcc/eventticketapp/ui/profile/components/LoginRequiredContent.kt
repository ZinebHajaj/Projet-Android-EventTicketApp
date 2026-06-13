package com.dcc.eventticketapp.ui.profile.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoginRequiredContent(
    onLoginClick: () -> Unit
) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Vous n'êtes pas connecté"
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onLoginClick
            ) {
                Text("Se connecter")
            }
        }
    }
}