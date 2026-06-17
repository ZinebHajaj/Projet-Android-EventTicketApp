package com.dcc.eventticketapp.ui.profile.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dcc.eventticketapp.ui.theme.OrangeMain

private val faqs = listOf(
    "Comment réserver un billet ?" to
            "Choisissez un événement, appuyez sur « Réserver maintenant » et suivez les étapes de paiement. Votre billet apparaîtra ensuite dans « Mes réservations ».",
    "Puis-je annuler une réservation ?" to
            "Les annulations sont possibles jusqu'à 48h avant l'événement. Contactez notre support via l'adresse support@ticketgo.ma pour toute demande.",
    "Comment accéder à mon billet ?" to
            "Rendez-vous dans l'onglet « Billets » de la barre de navigation. Votre QR code y est disponible hors ligne.",
    "L'app est-elle disponible hors ligne ?" to
            "Oui, vos billets déjà téléchargés restent accessibles sans connexion. La liste des événements nécessite Internet.",
    "Comment contacter le support ?" to
            "Envoyez un email à support@ticketgo.ma ou utilisez le formulaire de contact sur notre site web."
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpScreen(onBack: () -> Unit) {

    val context       = LocalContext.current
    val bgColor       = MaterialTheme.colorScheme.background
    val surfaceColor  = MaterialTheme.colorScheme.surface
    val textPrimary   = MaterialTheme.colorScheme.onBackground
    val textSecond    = MaterialTheme.colorScheme.onSurfaceVariant

    Scaffold(
        containerColor = bgColor,
        topBar = {
            TopAppBar(
                title = {
                    Text("Centre d'aide", fontSize = 20.sp,
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

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            // ── Header ────────────────────────────────────────────
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape    = RoundedCornerShape(20.dp),
                    color    = OrangeMain.copy(alpha = 0.1f)
                ) {
                    Row(
                        modifier          = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape    = CircleShape,
                            color    = OrangeMain,
                            modifier = Modifier.size(48.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.AutoMirrored.Outlined.HelpOutline, null,
                                    tint = Color.White, modifier = Modifier.size(24.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text("FAQ & Assistance", fontSize = 16.sp,
                                fontWeight = FontWeight.Bold, color = textPrimary)
                            Text("Trouvez des réponses à vos questions",
                                fontSize = 13.sp, color = textSecond)
                        }
                    }
                }
            }

            // ── FAQs accordéon ──────────────────────────────────────
            items(faqs.size) { i ->
                FaqItem(
                    question     = faqs[i].first,
                    answer       = faqs[i].second,
                    surfaceColor = surfaceColor,
                    textPrimary  = textPrimary,
                    textSecond   = textSecond
                )
            }

            // ── Contact (icônes cliquables, sans emoji) ─────────────
            item {
                Surface(
                    modifier        = Modifier.fillMaxWidth(),
                    shape           = RoundedCornerShape(20.dp),
                    color           = surfaceColor,
                    shadowElevation = 2.dp
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("Contacter le support", fontSize = 15.sp,
                            fontWeight = FontWeight.Bold, color = textPrimary)
                        Spacer(modifier = Modifier.height(14.dp))

                        // Email cliquable → ouvre une app mail
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                                        data = Uri.parse("mailto:support@ticketgo.ma")
                                    }
                                    context.startActivity(intent)
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Outlined.Email, null,
                                tint = OrangeMain, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("support@ticketgo.ma", fontSize = 14.sp, color = textSecond)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Outlined.Schedule, null,
                                tint = OrangeMain, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Lun–Ven : 9h–18h", fontSize = 14.sp, color = textSecond)
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Composable
private fun FaqItem(
    question     : String,
    answer       : String,
    surfaceColor : Color,
    textPrimary  : Color,
    textSecond   : Color
) {
    var expanded by remember { mutableStateOf(false) }

    Surface(
        modifier        = Modifier.fillMaxWidth(),
        shape           = RoundedCornerShape(16.dp),
        color           = surfaceColor,
        shadowElevation = 2.dp,
        onClick         = { expanded = !expanded }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier          = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Outlined.QuestionMark, null,
                    tint = OrangeMain, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text       = question,
                    fontSize   = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = textPrimary,
                    modifier   = Modifier.weight(1f)
                )
                Icon(
                    if (expanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore,
                    null,
                    tint = OrangeMain,
                    modifier = Modifier.size(20.dp)
                )
            }
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(color = OrangeMain.copy(alpha = 0.2f))
                Spacer(modifier = Modifier.height(8.dp))
                Text(answer, fontSize = 13.sp, color = textSecond,
                    lineHeight = 20.sp)
            }
        }
    }
}