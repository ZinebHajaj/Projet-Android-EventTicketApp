package com.dcc.eventticketapp.ui.events.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dcc.eventticketapp.ui.events.EventsIntent
import com.dcc.eventticketapp.ui.events.EventsViewModel
import com.dcc.eventticketapp.ui.events.component.EventListItem
import com.dcc.eventticketapp.ui.events.component.EventsCategoriesFilter
import com.dcc.eventticketapp.ui.events.component.EventsSearchBar
import com.dcc.eventticketapp.ui.theme.OrangeMain

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(
    onEventClick : (String) -> Unit,
    viewModel    : EventsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    // Couleurs adaptatives dark/light
    val bgColor      = MaterialTheme.colorScheme.background
    val surfaceColor = MaterialTheme.colorScheme.surface
    val textPrimary  = MaterialTheme.colorScheme.onBackground
    val textSecond   = MaterialTheme.colorScheme.onSurfaceVariant

    Scaffold(
        containerColor = bgColor,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text       = "Événements",
                            fontSize   = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color      = textPrimary
                        )
                        Text(
                            text     = "${state.filteredEvents.size} événements trouvés",
                            fontSize = 13.sp,
                            color    = textSecond
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = bgColor
                )
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            // ── Barre de recherche ────────────────────────────────
            item {
                EventsSearchBar(
                    query         = state.searchQuery,
                    onQueryChange = {
                        viewModel.handleIntent(EventsIntent.SearchEvents(it))
                    },
                    surfaceColor  = surfaceColor,
                    textSecond    = textSecond
                )
            }

            // ── Filtres catégories ────────────────────────────────
            item {
                Spacer(modifier = Modifier.height(4.dp))
                EventsCategoriesFilter(
                    categories       = state.categories,
                    selectedCategory = state.selectedCategory,
                    onCategoryClick  = {
                        viewModel.handleIntent(EventsIntent.SelectCategory(it))
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // ── État loading ──────────────────────────────────────
            if (state.isLoading) {
                item {
                    Box(
                        modifier         = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = OrangeMain)
                    }
                }
            }

            // ── État erreur ───────────────────────────────────────
            state.error?.let { error ->
                item {
                    Text(
                        text     = "Erreur : $error",
                        color    = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            // ── Liste événements ──────────────────────────────────
            if (!state.isLoading && state.filteredEvents.isEmpty()) {
                item {
                    Box(
                        modifier         = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text  = "Aucun événement trouvé",
                            color = textSecond
                        )
                    }
                }
            } else {
                items(state.filteredEvents) { event ->
                    EventListItem(
                        event           = event,
                        surfaceColor    = surfaceColor,
                        textPrimary     = textPrimary,
                        textSecond      = textSecond,
                        onClick         = { onEventClick(event.id) },
                        onFavoriteClick = {
                            viewModel.handleIntent(
                                EventsIntent.ToggleFavorite(event.id)
                            )
                        }
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}