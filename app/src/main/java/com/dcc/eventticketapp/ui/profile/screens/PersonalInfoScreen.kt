package com.dcc.eventticketapp.ui.profile.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dcc.eventticketapp.ui.profile.ProfileIntent
import com.dcc.eventticketapp.ui.profile.ProfileViewModel
import com.dcc.eventticketapp.ui.theme.OrangeMain

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalInfoScreen(
    onBack    : () -> Unit,
    viewModel : ProfileViewModel = hiltViewModel()
) {
    val state        by viewModel.state.collectAsState()
    val bgColor       = MaterialTheme.colorScheme.background
    val surfaceColor  = MaterialTheme.colorScheme.surface
    val textPrimary   = MaterialTheme.colorScheme.onBackground
    val textSecond    = MaterialTheme.colorScheme.onSurfaceVariant

    // Mode édition activable via l'icône crayon de la top bar
    var isEditing by remember { mutableStateOf(false) }

    // Quand la sauvegarde réussit, on repasse en mode lecture
    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) isEditing = false
    }

    Scaffold(
        containerColor = bgColor,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Informations personnelles",
                        fontSize   = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color      = textPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Outlined.ArrowBackIosNew, "Retour",
                            tint = textPrimary)
                    }
                },
                actions = {
                    IconButton(onClick = { isEditing = !isEditing }) {
                        Icon(
                            if (isEditing) Icons.Outlined.Close else Icons.Outlined.Edit,
                            contentDescription = if (isEditing) "Annuler" else "Modifier",
                            tint = OrangeMain
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = bgColor
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // ── Avatar + nom ──────────────────────────────────────
            Surface(
                modifier        = Modifier.fillMaxWidth(),
                shape           = RoundedCornerShape(20.dp),
                color           = surfaceColor,
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier          = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(OrangeMain),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.user?.fullName
                                ?.split(" ")
                                ?.take(2)
                                ?.joinToString("") { it.first().uppercase() }
                                ?: "?",
                            fontSize   = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color      = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text       = state.user?.fullName ?: "Nom non défini",
                            fontSize   = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color      = textPrimary
                        )
                        Text(
                            text     = state.user?.email ?: "Email non défini",
                            fontSize = 14.sp,
                            color    = textSecond
                        )
                    }
                }
            }

            // ── Infos : lecture OU édition selon isEditing ─────────
            if (!isEditing) {

                Surface(
                    modifier        = Modifier.fillMaxWidth(),
                    shape           = RoundedCornerShape(20.dp),
                    color           = surfaceColor,
                    shadowElevation = 2.dp
                ) {
                    Column(modifier = Modifier.padding(4.dp)) {
                        InfoRow(
                            icon        = Icons.Outlined.Person,
                            label       = "Nom complet",
                            value       = state.user?.fullName ?: "Non défini",
                            textPrimary = textPrimary,
                            textSecond  = textSecond
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color    = Color(0xFFF0F0F0)
                        )
                        InfoRow(
                            icon        = Icons.Outlined.Email,
                            label       = "Adresse email",
                            value       = state.user?.email ?: "Non défini",
                            textPrimary = textPrimary,
                            textSecond  = textSecond
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color    = Color(0xFFF0F0F0)
                        )
                        InfoRow(
                            icon        = Icons.Outlined.Phone,
                            label       = "Téléphone",
                            value       = state.user?.phone?.ifEmpty { "Non défini" }
                                ?: "Non défini",
                            textPrimary = textPrimary,
                            textSecond  = textSecond
                        )
                    }
                }

                // ── Badge vérifié ─────────────────────────────────
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape    = RoundedCornerShape(20.dp),
                    color    = OrangeMain.copy(alpha = 0.1f)
                ) {
                    Row(
                        modifier          = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Outlined.VerifiedUser, null,
                            tint = OrangeMain, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Compte vérifié", fontSize = 14.sp,
                                fontWeight = FontWeight.Bold, color = OrangeMain)
                            Text("Vos informations sont sécurisées",
                                fontSize = 12.sp,
                                color    = OrangeMain.copy(alpha = 0.7f))
                        }
                    }
                }

            } else {

                // ── Formulaire d'édition ───────────────────────────
                Surface(
                    modifier        = Modifier.fillMaxWidth(),
                    shape           = RoundedCornerShape(20.dp),
                    color           = surfaceColor,
                    shadowElevation = 2.dp
                ) {
                    Column(
                        modifier            = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        OutlinedTextField(
                            value         = state.editName,
                            onValueChange = {
                                viewModel.handleIntent(ProfileIntent.NameChanged(it))
                            },
                            label = { Text("Nom complet") },
                            leadingIcon = {
                                Icon(Icons.Outlined.Person, null, tint = OrangeMain)
                            },
                            singleLine = true,
                            shape      = RoundedCornerShape(12.dp),
                            colors     = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = OrangeMain,
                                focusedLabelColor  = OrangeMain,
                                cursorColor        = OrangeMain
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value         = state.editEmail,
                            onValueChange = {},
                            readOnly      = true,
                            enabled       = false,
                            label = { Text("Email (non modifiable)") },
                            leadingIcon = {
                                Icon(Icons.Outlined.Email, null, tint = OrangeMain)
                            },
                            singleLine = true,
                            shape      = RoundedCornerShape(12.dp),
                            colors     = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = OrangeMain,
                                focusedLabelColor  = OrangeMain,
                                cursorColor        = OrangeMain
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value         = state.editPhone,
                            onValueChange = {
                                viewModel.handleIntent(ProfileIntent.PhoneChanged(it))
                            },
                            label = { Text("Téléphone") },
                            leadingIcon = {
                                Icon(Icons.Outlined.Phone, null, tint = OrangeMain)
                            },
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                keyboardType = KeyboardType.Phone
                            ),
                            singleLine = true,
                            shape      = RoundedCornerShape(12.dp),
                            colors     = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = OrangeMain,
                                focusedLabelColor  = OrangeMain,
                                cursorColor        = OrangeMain
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        state.error?.let {
                            Text(it, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
                        }

                        Button(
                            onClick  = {
                                viewModel.handleIntent(ProfileIntent.SubmitEditProfile)
                            },
                            enabled  = !state.isLoading,
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            shape    = RoundedCornerShape(12.dp),
                            colors   = ButtonDefaults.buttonColors(containerColor = OrangeMain)
                        ) {
                            if (state.isLoading) {
                                CircularProgressIndicator(
                                    modifier    = Modifier.size(20.dp),
                                    color       = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text("Enregistrer les modifications",
                                    fontSize = 15.sp, fontWeight = FontWeight.SemiBold,
                                    color = Color.White)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun InfoRow(
    icon        : androidx.compose.ui.graphics.vector.ImageVector,
    label       : String,
    value       : String,
    textPrimary : Color,
    textSecond  : Color
) {
    Row(
        modifier          = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(OrangeMain.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = OrangeMain,
                modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(label, fontSize = 12.sp, color = textSecond)
            Text(value, fontSize = 14.sp,
                fontWeight = FontWeight.Medium, color = textPrimary)
        }
    }
}