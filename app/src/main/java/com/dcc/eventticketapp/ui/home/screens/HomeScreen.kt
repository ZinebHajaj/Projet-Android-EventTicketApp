package com.dcc.eventticketapp.ui.home.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dcc.eventticketapp.ui.category.CategoryIntent
import com.dcc.eventticketapp.ui.category.CategoryViewModel
import com.dcc.eventticketapp.ui.home.HomeIntent
import com.dcc.eventticketapp.ui.home.HomeViewModel
import com.dcc.eventticketapp.ui.home.component.BottomNavBar
import com.dcc.eventticketapp.ui.category.component.CategoriesRow
import com.dcc.eventticketapp.ui.home.component.HeroBanner
import com.dcc.eventticketapp.ui.home.component.PopularEventCard
import com.dcc.eventticketapp.ui.home.component.SearchBarHome
import com.dcc.eventticketapp.ui.home.component.SectionHeader
import com.dcc.eventticketapp.ui.theme.ErrorLight
import com.dcc.eventticketapp.ui.theme.OrangeMain


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel      : HomeViewModel,
    categoryViewModel : CategoryViewModel,
    onEventClick   : (String) -> Unit,
    onProfileClick : () -> Unit = {},
    onEventsClick  : () -> Unit = {},
    onFavoritesClick  : () -> Unit = {},
    onTicketsClick : () -> Unit = {},
) {
    val state by viewModel.state.collectAsState()
    val categoryState by categoryViewModel.state.collectAsState()

    // Charge les events au démarrage
    LaunchedEffect(Unit) {
        viewModel.handleIntent(HomeIntent.LoadEvents)
    }

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
                            text       = "Découvrir",
                            fontSize   = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color      = textPrimary
                        )
                        Text(
                            text     = "Trouvez des événements incroyables",
                            fontSize = 13.sp,
                            color    = textSecond
                        )
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(containerColor = bgColor),

            )
        },
        bottomBar = {
            BottomNavBar(
                surfaceColor   = surfaceColor,
                textSecond     = textSecond,
                onProfileClick = onProfileClick,
                onEventsClick  = onEventsClick,
                onFavoritesClick = onFavoritesClick,
                onTicketsClick = onTicketsClick

            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            item { SearchBarHome(surfaceColor, textSecond) }
            item { HeroBanner(onExploreClick = onEventsClick) }
            item {
                SectionHeader("Catégories", textPrimary, textSecond, showSeeAll = false, onSeeAll = {})
                Spacer(modifier = Modifier.height(12.dp))
                CategoriesRow(
                    surfaceColor     = surfaceColor,
                    textSecond       = textSecond,
                    selectedCategory = categoryState.selectedCategory,
                    onCategoryClick = { category ->
                        // CategoryViewModel → gère la sélection visuelle
                        categoryViewModel.handleIntent(CategoryIntent.SelectCategory(category))

                        // HomeViewModel --> filtre les événements
                        viewModel.handleIntent(HomeIntent.FilterByCategory(category))
                    }
                )
                //CategoriesRow(surfaceColor, textSecond)
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                SectionHeader("Événements populaires", textPrimary, textSecond, showSeeAll = true, onSeeAll = onEventsClick)
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Gestion des états
            when {
                state.isLoading -> {
                    item {
                        Box(
                            modifier            = Modifier.fillMaxWidth(),
                            contentAlignment    = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = OrangeMain)
                        }
                    }
                }
                state.error != null -> {
                    item {
                        Text(
                            text  = "Erreur : ${state.error}",
                            color = ErrorLight,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                else -> {
                    items(state.upcomingEvents) { event ->
                        PopularEventCard(
                            event = event,
                            surfaceColor = surfaceColor,
                            textPrimary = textPrimary,
                            textSecond = textSecond,
                            onClick = { onEventClick(event.id) },
                            onFavoriteClick = {
                                viewModel.handleIntent(
                                    HomeIntent.ToggleFavorite(event.id)
                                )
                            }
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

        }
    }
}