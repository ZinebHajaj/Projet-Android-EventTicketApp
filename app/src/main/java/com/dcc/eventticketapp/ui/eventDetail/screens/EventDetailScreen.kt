package com.dcc.eventticketapp.ui.eventDetail.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dcc.eventticketapp.ui.eventDetail.EventDetailIntent
import com.dcc.eventticketapp.ui.eventDetail.EventDetailViewModel
import com.dcc.eventticketapp.ui.eventDetail.component.BookingButton
import com.dcc.eventticketapp.ui.eventDetail.component.EventDetailHeader
import com.dcc.eventticketapp.ui.eventDetail.component.EventDetailInfo
import com.dcc.eventticketapp.ui.theme.ErrorLight
import com.dcc.eventticketapp.ui.theme.OrangeMain
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun EventDetailScreen(
    eventId   : String,
    onBack    : () -> Unit,
    viewModel : EventDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(eventId) {
        viewModel.handleIntent(EventDetailIntent.LoadEvent(eventId))
    }

    when {
        state.isLoading -> {
            Box(
                modifier         = Modifier.fillMaxSize(),
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
                Text(text = "Erreur : ${state.error}", color = ErrorLight)
            }
        }

        state.event != null -> {
            Box(modifier = Modifier.fillMaxSize()) {

                // Contenu scrollable
                androidx.compose.foundation.lazy.LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Header grand image
                    item {
                        EventDetailHeader(
                            imageUrl   = state.event!!.imageUrl,
                            title      = state.event!!.title,
                            category   = state.event!!.category,
                            isFavorite = state.event!!.isFavorite,
                            onBack     = onBack,
                            onFavorite = {
                                viewModel.handleIntent(EventDetailIntent.ToggleFavorite)
                            }
                        )
                    }

                    // Infos
                    item {
                        EventDetailInfo(event = state.event!!)
                    }

                    // Espace pour le bouton en bas
                    item {
                        Spacer(modifier = Modifier.height(100.dp))
                    }
                }

                // Bouton réserver fixe en bas
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        //.padding(horizontal = 20.dp, vertical = 12.dp)
                        .padding(
                            bottom = 35.dp
                        )

                ) {
                    BookingButton(
                        isBooked = state.isBooked,
                        onClick  = {
                            viewModel.handleIntent(EventDetailIntent.BookEvent)
                        }
                    )
                }
            }
        }
    }
}

/*
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun EventDetailScreenPreview() {
    EventTicketAppTheme {
        EventDetailScreen(
            eventId = "1",
            onBack = {}
        )
    }
}
*/

