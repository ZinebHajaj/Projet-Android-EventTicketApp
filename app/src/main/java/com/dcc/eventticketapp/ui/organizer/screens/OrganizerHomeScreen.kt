package com.dcc.eventticketapp.ui.organizer.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dcc.eventticketapp.ui.theme.OrangeMain

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrganizerHomeScreen(
    onCreateEvent: () -> Unit,
    onMyEvents: () -> Unit,
    onLogout: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Espace Organisateur") },
                actions = {
                    TextButton(onClick = onLogout) {
                        Text("Déconnexion", color = OrangeMain)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Bienvenue, Organisateur",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = onCreateEvent,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = OrangeMain)
            ) {
                Icon(Icons.Outlined.Add, null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Créer un événement")
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onMyEvents,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Icon(Icons.Outlined.List, null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Mes événements")
            }
        }
    }
}