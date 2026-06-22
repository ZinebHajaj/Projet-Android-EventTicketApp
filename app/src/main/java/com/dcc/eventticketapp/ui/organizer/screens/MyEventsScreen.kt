package com.dcc.eventticketapp.ui.organizer.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.dcc.eventticketapp.data.Entities.EventModel
import com.dcc.eventticketapp.ui.organizer.OrganizerIntent
import com.dcc.eventticketapp.ui.organizer.OrganizerViewModel
import com.dcc.eventticketapp.ui.theme.OrangeMain
import com.dcc.eventticketapp.ui.theme.TextGrayMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyEventsScreen(
    onBack: () -> Unit,
    onEditEvent: (EventModel) -> Unit,
    viewModel: OrganizerViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    var showDeleteDialog by remember { mutableStateOf(false) }
    var eventToDelete by remember { mutableStateOf<EventModel?>(null) }

    LaunchedEffect(Unit) {
        viewModel.handleIntent(OrganizerIntent.LoadMyEvents)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mes événements") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Outlined.ArrowBack, "Retour")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = OrangeMain
                    )
                }
                state.error != null -> {
                    Text(
                        text = "Erreur: ${state.error}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                state.myEvents.isEmpty() -> {
                    EmptyEventsMessage()
                }
                else -> {
                    LazyColumn {
                        items(state.myEvents) { event ->
                            OrganizerEventCard(
                                event = event,
                                onEdit = { onEditEvent(event) },
                                onDelete = {
                                    eventToDelete = event
                                    showDeleteDialog = true
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // Dialogue de confirmation suppression
    if (showDeleteDialog && eventToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Supprimer l'événement") },
            text = { Text("Voulez-vous vraiment supprimer '${eventToDelete!!.title}' ? Cette action est irréversible.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.handleIntent(OrganizerIntent.DeleteEvent(eventToDelete!!.id))
                        showDeleteDialog = false
                        eventToDelete = null
                    }
                ) {
                    Text("Supprimer", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Annuler")
                }
            }
        )
    }
}

@Composable
private fun EmptyEventsMessage() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Outlined.EventBusy,
            null,
            modifier = Modifier.size(64.dp),
            tint = TextGrayMode
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Aucun événement créé",
            color = TextGrayMode,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Cliquez sur 'Créer' pour ajouter un événement",
            color = TextGrayMode,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun OrganizerEventCard(
    event: EventModel,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column {
            // Image avec placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(Color(0xFFE0E0E0)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = event.imageUrl,
                    contentDescription = event.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Placeholder si pas d'image
                if (event.imageUrl.isBlank()) {
                    Icon(
                        Icons.Outlined.Image,
                        null,
                        modifier = Modifier.size(48.dp),
                        tint = TextGrayMode
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        event.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        "${event.city} • ${event.date}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextGrayMode
                    )
                    Text(
                        "${event.priceStandard.toInt()} MAD",
                        color = OrangeMain,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                // Bouton Modifier
                IconButton(onClick = onEdit) {
                    Icon(Icons.Outlined.Edit, "Modifier", tint = OrangeMain)
                }

                // Bouton Supprimer
                IconButton(onClick = onDelete) {
                    Icon(Icons.Outlined.Delete, "Supprimer", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}