package com.dcc.eventticketapp.ui.profile.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dcc.eventticketapp.ui.auth.components.AppLogo
import com.dcc.eventticketapp.ui.theme.OrangeMain

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(onBack: () -> Unit) {

    val bgColor      = MaterialTheme.colorScheme.background
    val surfaceColor = MaterialTheme.colorScheme.surface
    val textPrimary  = MaterialTheme.colorScheme.onBackground
    val textSecond   = MaterialTheme.colorScheme.onSurfaceVariant

    Scaffold(
        containerColor = bgColor,
        topBar = {
            TopAppBar(
                title = {
                    Text("À propos", fontSize = 20.sp,
                        fontWeight = FontWeight.Bold, color = textPrimary)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Outlined.ArrowBackIosNew, "Retour", tint = textPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = bgColor)
            )
        }
    ) { padding ->

        androidx.compose.foundation.lazy.LazyColumn(
            modifier            = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(16.dp)) }

            // ── Logo + nom app ──────────────────────────────────
            item {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AppLogo(boxSize = 88.dp, iconSize = 60.dp, cornerRadius = 22.dp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("TicketGo", fontSize = 28.sp,
                        fontWeight = FontWeight.Bold, color = textPrimary)
                    Text("Version 1.0.0", fontSize = 14.sp, color = textSecond)
                }
            }

            // ── Description ──────────────────────────────────────
            item {
                Surface(
                    modifier        = Modifier.fillMaxWidth(),
                    shape           = RoundedCornerShape(20.dp),
                    color           = surfaceColor,
                    shadowElevation = 2.dp
                ) {
                    Text(
                        text       = "TicketGo est une application de billetterie en ligne dédiée aux événements au Maroc. Concerts, sports, théâtre, ateliers... réservez vos billets en quelques clics et vivez des expériences inoubliables.",
                        fontSize   = 14.sp,
                        color      = textSecond,
                        lineHeight = 22.sp,
                        textAlign  = TextAlign.Center,
                        modifier   = Modifier.padding(20.dp)
                    )
                }
            }

            // ── Infos pratiques (sans Projet / Établissement) ──────
            item {
                Surface(
                    modifier        = Modifier.fillMaxWidth(),
                    shape           = RoundedCornerShape(20.dp),
                    color           = surfaceColor,
                    shadowElevation = 2.dp
                ) {
                    Column(modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        AboutRow(Icons.Outlined.Info,        "Version",       "1.0.0",        textPrimary, textSecond)
                        HorizontalDivider(color = Color(0xFFF0F0F0))
                        AboutRow(Icons.Outlined.Update,      "Dernière mise à jour", "Juin 2026", textPrimary, textSecond)
                        HorizontalDivider(color = Color(0xFFF0F0F0))
                        AboutRow(Icons.Outlined.Code,        "Développé par", "Manar & Zineb", textPrimary, textSecond)
                        HorizontalDivider(color = Color(0xFFF0F0F0))
                        AboutRow(Icons.Outlined.LocationOn,  "Pays",          "Maroc",         textPrimary, textSecond)
                    }
                }
            }

            // ── Contact & liens ──────────────────────────────────
            item {
                Surface(
                    modifier        = Modifier.fillMaxWidth(),
                    shape           = RoundedCornerShape(20.dp),
                    color           = surfaceColor,
                    shadowElevation = 2.dp
                ) {
                    Column(modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        AboutRow(Icons.Outlined.Email,      "Contact",          "support@ticketgo.ma", textPrimary, textSecond)
                        HorizontalDivider(color = Color(0xFFF0F0F0))
                        AboutRow(Icons.Outlined.Star,       "Noter l'app",      "Donnez votre avis",   textPrimary, textSecond)
                        HorizontalDivider(color = Color(0xFFF0F0F0))
                        AboutRow(Icons.Outlined.Share,      "Partager l'app",   "Avec vos amis",        textPrimary, textSecond)
                    }
                }
            }

            // ── Remerciement ──────────────────────────────────────
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape    = RoundedCornerShape(20.dp),
                    color    = OrangeMain.copy(alpha = 0.1f)
                ) {
                    Column(
                        modifier            = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            shape    = CircleShape,
                            color    = OrangeMain,
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Outlined.Favorite, null,
                                    tint     = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            "Merci de faire confiance à TicketGo",
                            fontSize   = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color      = textPrimary,
                            textAlign  = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Nous travaillons chaque jour pour vous offrir la meilleure expérience de réservation.",
                            fontSize   = 12.sp,
                            color      = textSecond,
                            textAlign  = TextAlign.Center,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Composable
private fun AboutRow(
    icon        : ImageVector,
    label       : String,
    value       : String,
    textPrimary : Color,
    textSecond  : Color
) {
    Row(
        modifier          = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon, null,
            tint     = OrangeMain,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(label, fontSize = 14.sp, color = textSecond,
            modifier = Modifier.weight(1f))
        Text(value, fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold, color = textPrimary)
    }
}