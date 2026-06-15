package com.dcc.eventticketapp.ui.ticket.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dcc.eventticketapp.ui.ticket.TicketIntent
import com.dcc.eventticketapp.ui.ticket.TicketViewModel
import com.dcc.eventticketapp.ui.ticket.component.QrCodeDisplay
import com.dcc.eventticketapp.ui.ticket.component.TicketCard
import com.dcc.eventticketapp.ui.theme.OrangeMain
import com.dcc.eventticketapp.ui.theme.TextGrayMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketScreen(
    viewModel : TicketViewModel
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.handleIntent(TicketIntent.LoadTickets)
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text       = "Mes Billets",
                            fontSize   = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color      = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text     = "Vos réservations",
                            fontSize = 13.sp,
                            color    = TextGrayMode
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->

        when {
            state.isLoading -> {
                Box(
                    modifier         = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = OrangeMain)
                }
            }

            state.error != null -> {
                Box(
                    modifier         = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text  = "Erreur : ${state.error}",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            state.tickets.isEmpty() -> {
                Box(
                    modifier         = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text     = "🎫",
                            fontSize = 60.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text      = "Aucun billet pour le moment",
                            fontSize  = 16.sp,
                            color     = TextGrayMode
                        )
                    }
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    items(state.tickets) { ticket ->
                        TicketCard(
                            ticket  = ticket,
                            onClick = {
                                viewModel.handleIntent(
                                    TicketIntent.SelectTicket(ticket.ticketId)
                                )
                            }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
            }
        }

        // Dialog QR Code
        if (state.selectedTicket != null) {
            AlertDialog(
                onDismissRequest = {
                    viewModel.handleIntent(TicketIntent.DismissTicket)
                },
                shape = RoundedCornerShape(20.dp),
                title = {
                    Text(
                        text       = state.selectedTicket!!.eventTitle,
                        fontSize   = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier            = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text     = "Présentez ce QR code à l'entrée",
                            fontSize = 13.sp,
                            color    = TextGrayMode
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        QrCodeDisplay(
                            qrContent = state.selectedTicket!!.qrCode,
                            ticketId  = state.selectedTicket!!.ticketId
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = { viewModel.handleIntent(TicketIntent.DismissTicket) }
                    ) {
                        Text("Fermer", color = OrangeMain)
                    }
                }
            )
        }
    }
}