package com.dcc.eventticketapp.ui.profile.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dcc.eventticketapp.ui.profile.ProfileIntent
import com.dcc.eventticketapp.ui.profile.ProfileViewModel
import com.dcc.eventticketapp.ui.profile.components.*
import com.dcc.eventticketapp.ui.theme.OrangeMain

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack : () -> Unit = {},
    onLogout       : () -> Unit = {},
    viewModel      : ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    // Couleurs adaptatives dark/light
    val bgColor      = MaterialTheme.colorScheme.background
    val surfaceColor = MaterialTheme.colorScheme.surface
    val textPrimary  = MaterialTheme.colorScheme.onBackground
    val textSecond   = MaterialTheme.colorScheme.onSurfaceVariant
    val orange = OrangeMain

    // Navigation si déconnecté
    LaunchedEffect(state.isLoggedOut) {
        if (state.isLoggedOut) onLogout()
    }

    Scaffold(
        containerColor = bgColor,
        topBar = {
            TopAppBar(
                title = {
                    Text("Mon Profil", fontSize = 20.sp,
                        fontWeight = FontWeight.Bold, color = textPrimary)
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Outlined.ArrowBackIosNew, "Retour",
                            tint = textPrimary)
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.showEditDialog() }) {
                        Icon(Icons.Outlined.Edit, "Modifier", tint = orange)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = bgColor
                )
            )
        }
    ) { padding ->

        if (state.isLoading) {
            Box(
                modifier         = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator(color = orange) }
            return@Scaffold
        }

        LazyColumn(
            modifier              = Modifier.fillMaxSize().padding(padding),
            horizontalAlignment   = Alignment.CenterHorizontally
        ) {

            // ── Avatar ────────────────────────────────────────────
            item {
                Spacer(modifier = Modifier.height(16.dp))
                AvatarSection(
                    user        = state.user,
                    textPrimary = textPrimary,
                    textSecond  = textSecond
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            // ── Stats ─────────────────────────────────────────────
            item {
                StatsSection(
                    reservationsCount = state.reservationsCount,
                    favoritesCount    = state.favoritesCount,
                    eventsCount       = state.eventsCount,
                    surfaceColor      = surfaceColor,
                    textSecond        = textSecond
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            // ── Mon Compte ────────────────────────────────────────
            item {
                MenuSection(
                    title        = "Mon Compte",
                    surfaceColor = surfaceColor,
                    textPrimary  = textPrimary,
                    textSecond   = textSecond,
                    items = listOf(
                        ProfileMenuItem(
                            icon     = Icons.Outlined.Person,
                            label    = "Informations personnelles",
                            subtitle = state.user?.phone ?: "Téléphone non défini"
                        ),
                        ProfileMenuItem(
                            icon     = Icons.Outlined.ConfirmationNumber,
                            label    = "Mes réservations",
                            subtitle = "${state.reservationsCount} billets achetés"
                        ),
                        ProfileMenuItem(
                            icon     = Icons.Outlined.Favorite,
                            label    = "Mes favoris",
                            subtitle = "${state.favoritesCount} événements sauvegardés"
                        ),
                        ProfileMenuItem(
                            icon     = Icons.Outlined.CreditCard,
                            label    = "Moyens de paiement",
                            subtitle = "Cartes et portefeuille"
                        ),
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // ── Préférences ───────────────────────────────────────
            item {
                MenuSection(
                    title        = "Préférences",
                    surfaceColor = surfaceColor,
                    textPrimary  = textPrimary,
                    textSecond   = textSecond,
                    items = listOf(
                        ProfileMenuItem(
                            icon      = Icons.Outlined.Notifications,
                            label     = "Notifications",
                            subtitle  = if (state.notificationsEnabled) "Activées" else "Désactivées",
                            hasToggle = true,
                            isToggled = state.notificationsEnabled,
                            onToggle  = {
                                viewModel.handleIntent(ProfileIntent.ToggleNotifications)
                            }
                        ),
                        ProfileMenuItem(
                            icon     = Icons.Outlined.Language,
                            label    = "Langue",
                            subtitle = "Français"
                        ),
                        ProfileMenuItem(
                            icon      = if (state.isDarkMode) Icons.Outlined.LightMode
                            else Icons.Outlined.DarkMode,
                            label     = "Mode sombre",
                            subtitle  = if (state.isDarkMode) "Activé" else "Désactivé",
                            hasToggle = true,
                            isToggled = state.isDarkMode,
                            onToggle  = {
                                viewModel.handleIntent(ProfileIntent.ToggleDarkMode)
                            }
                        ),
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // ── Support ───────────────────────────────────────────
            item {
                MenuSection(
                    title        = "Support",
                    surfaceColor = surfaceColor,
                    textPrimary  = textPrimary,
                    textSecond   = textSecond,
                    items = listOf(
                        ProfileMenuItem(
                            icon     = Icons.AutoMirrored.Outlined.HelpOutline,
                            label    = "Centre d'aide",
                            subtitle = "FAQ et assistance"
                        ),
                        ProfileMenuItem(
                            icon     = Icons.Outlined.Shield,
                            label    = "Confidentialité",
                            subtitle = "Politique de données"
                        ),
                        ProfileMenuItem(
                            icon     = Icons.Outlined.Info,
                            label    = "À propos",
                            subtitle = "Version 1.0.0"
                        ),
                    )
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            // ── Déconnexion ───────────────────────────────────────
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = orange.copy(alpha = 0.12f),
                    onClick = { viewModel.showLogoutDialog() }
                ) {
                    Row(
                        modifier              = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment     = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.AutoMirrored.Outlined.Logout, null,
                            tint = orange, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Se déconnecter", fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold, color = orange)
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }

    // ── Dialog Déconnexion ────────────────────────────────────────
    if (state.showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.hideLogoutDialog() },
            icon = {
                Icon(Icons.AutoMirrored.Outlined.Logout, null,
                    tint = orange, modifier = Modifier.size(32.dp))
            },
            title = {
                Text("Se déconnecter ?", fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground)
            },
            text = {
                Text("Vous serez redirigé vers la page de connexion.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.hideLogoutDialog()
                        viewModel.handleIntent(ProfileIntent.Logout)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = orange),
                    shape  = RoundedCornerShape(12.dp)
                ) { Text("Se déconnecter", color = Color.White) }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.hideLogoutDialog() }) {
                    Text("Annuler",
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            shape          = RoundedCornerShape(20.dp)
        )
    }

    // ── Dialog Modifier le profil ─────────────────────────────────
    if (state.showEditDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.hideEditDialog() },
            title = {
                Text("Modifier le profil", fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value         = state.editName,
                        onValueChange = {
                            viewModel.handleIntent(ProfileIntent.NameChanged(it))
                        },
                        label      = { Text("Nom complet") },
                        singleLine = true,
                        shape      = RoundedCornerShape(12.dp),
                        colors     = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = orange,
                            focusedLabelColor  = orange,
                            cursorColor        = orange
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value         = state.editEmail,
                        onValueChange = {
                            viewModel.handleIntent(ProfileIntent.EmailChanged(it))
                        },
                        label      = { Text("Email") },
                        singleLine = true,
                        shape      = RoundedCornerShape(12.dp),
                        colors     = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = orange,
                            focusedLabelColor  = orange,
                            cursorColor        = orange
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value         = state.editPhone,
                        onValueChange = {
                            viewModel.handleIntent(ProfileIntent.PhoneChanged(it))
                        },
                        label      = { Text("Téléphone") },
                        singleLine = true,
                        shape      = RoundedCornerShape(12.dp),
                        colors     = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = orange,
                            focusedLabelColor  = orange,
                            cursorColor        = orange
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.handleIntent(ProfileIntent.SubmitEditProfile)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = orange),
                    shape  = RoundedCornerShape(12.dp)
                ) { Text("Sauvegarder", color = Color.White) }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.hideEditDialog() }) {
                    Text("Annuler",
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            shape          = RoundedCornerShape(20.dp)
        )
    }
}