package com.dcc.eventticketapp.ui.favorites.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dcc.eventticketapp.ui.favorites.FavoritesIntent
import com.dcc.eventticketapp.ui.favorites.FavoritesViewModel
import com.dcc.eventticketapp.ui.favorites.component.EmptyFavorites
import com.dcc.eventticketapp.ui.favorites.component.FavoriteCard
import com.dcc.eventticketapp.ui.theme.OrangeMain

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    onEventClick  : (String) -> Unit,
    onExplore     : () -> Unit = {},
    viewModel     : FavoritesViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

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
                            text       = "Mes Favoris",
                            fontSize   = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color      = textPrimary
                        )
                        Text(
                            text     = "${state.filteredFavorites.size} événements sauvegardés",
                            fontSize = 13.sp,
                            color    = textSecond
                        )
                    }
                },
                actions = {
                    if (state.allFavorites.isNotEmpty()) {
                        IconButton(onClick = { viewModel.showClearDialog() }) {
                            Icon(
                                Icons.Outlined.DeleteOutline,
                                contentDescription = "Tout supprimer",
                                tint = OrangeMain
                            )
                        }
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

            // ── Filtres catégories ────────────────────────────────
            if (state.allFavorites.isNotEmpty()) {
                item {
                    LazyRow(
                        modifier              = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.filters) { filter ->
                            FilterChip(
                                selected = state.selectedFilter == filter,
                                onClick  = {
                                    viewModel.handleIntent(
                                        FavoritesIntent.SelectFilter(filter)
                                    )
                                },
                                label  = { Text(filter, fontSize = 13.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = OrangeMain,
                                    selectedLabelColor     = Color.White
                                )
                            )
                        }
                    }
                }
            }

            // ── État loading ──────────────────────────────────────
            if (state.isLoading) {
                item {
                    Box(
                        modifier         = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) { CircularProgressIndicator(color = OrangeMain) }
                }
            }

            // ── État vide ─────────────────────────────────────────
            if (!state.isLoading && state.filteredFavorites.isEmpty()) {
                item {
                    EmptyFavorites(
                        textPrimary = textPrimary,
                        textSecond  = textSecond,
                        onExplore   = onExplore
                    )
                }
            }

            // ── Grille 2 colonnes ─────────────────────────────────
            val chunkedList = state.filteredFavorites.chunked(2)
            items(chunkedList) { rowItems ->
                Row(
                    modifier              = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowItems.forEach { event ->
                        FavoriteCard(
                            event         = event,
                            onClick       = { onEventClick(event.id) },
                            onRemoveClick = {
                                viewModel.handleIntent(
                                    FavoritesIntent.RemoveFavorite(event.id)
                                )
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    // Remplir l'espace si nombre impair
                    if (rowItems.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }

    // ── Dialog supprimer tout ─────────────────────────────────────
    if (state.showClearDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.hideClearDialog() },
            icon = {
                Icon(
                    Icons.Outlined.DeleteOutline, null,
                    tint     = OrangeMain,
                    modifier = Modifier.size(32.dp)
                )
            },
            title = {
                Text(
                    "Vider les favoris ?",
                    fontWeight = FontWeight.Bold,
                    color      = textPrimary
                )
            },
            text = {
                Text(
                    "Tous vos favoris seront supprimés.",
                    color = textSecond
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.handleIntent(FavoritesIntent.ClearAllFavorites)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = OrangeMain
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) { Text("Supprimer tout", color = Color.White) }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.hideClearDialog() }) {
                    Text("Annuler", color = textSecond)
                }
            },
            containerColor = surfaceColor,
            shape          = RoundedCornerShape(20.dp)
        )
    }
}