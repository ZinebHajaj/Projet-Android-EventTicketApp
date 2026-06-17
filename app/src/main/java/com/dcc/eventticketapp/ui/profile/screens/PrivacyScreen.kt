package com.dcc.eventticketapp.ui.profile.screens


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dcc.eventticketapp.ui.theme.OrangeMain

private data class PrivacySection(
    val icon    : ImageVector,
    val title   : String,
    val content : String
)

private val sections = listOf(
    PrivacySection(
        Icons.Outlined.Description,
        "Données collectées",
        "Nous collectons les informations que vous fournissez lors de l'inscription : nom complet, adresse email, numéro de téléphone. Nous collectons également les données d'utilisation de l'application (événements consultés, billets achetés) afin d'améliorer votre expérience."
    ),
    PrivacySection(
        Icons.Outlined.Settings,
        "Utilisation des données",
        "Vos données sont utilisées pour gérer votre compte et vos réservations, vous envoyer des confirmations et rappels d'événements, améliorer nos services et personnaliser votre expérience, et respecter nos obligations légales."
    ),
    PrivacySection(
        Icons.Outlined.Share,
        "Partage des données",
        "Nous ne vendons jamais vos données personnelles à des tiers. Vos informations peuvent être partagées uniquement avec les organisateurs d'événements pour lesquels vous avez acheté un billet, et avec nos prestataires techniques sous contrat de confidentialité."
    ),
    PrivacySection(
        Icons.Outlined.Lock,
        "Sécurité",
        "Vos données sont protégées par des mesures de sécurité conformes aux standards du secteur : chiffrement SSL/TLS, authentification Firebase, accès restreint aux bases de données."
    ),
    PrivacySection(
        Icons.Outlined.Gavel,
        "Vos droits",
        "Conformément à la réglementation en vigueur, vous disposez d'un droit d'accès, de rectification et de suppression de vos données. Vous pouvez exercer ces droits à tout moment en nous contactant."
    ),
    PrivacySection(
        Icons.Outlined.History,
        "Conservation des données",
        "Vos données sont conservées tant que votre compte est actif. En cas de suppression de compte, vos informations personnelles sont supprimées dans un délai raisonnable, sauf obligation légale contraire."
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyScreen(onBack: () -> Unit) {

    val bgColor      = MaterialTheme.colorScheme.background
    val surfaceColor = MaterialTheme.colorScheme.surface
    val textPrimary  = MaterialTheme.colorScheme.onBackground
    val textSecond   = MaterialTheme.colorScheme.onSurfaceVariant

    Scaffold(
        containerColor = bgColor,
        topBar = {
            TopAppBar(
                title = {
                    Text("Confidentialité", fontSize = 20.sp,
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
                                Icon(Icons.Outlined.Shield, null,
                                    tint = Color.White, modifier = Modifier.size(24.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text("Politique de confidentialité", fontSize = 16.sp,
                                fontWeight = FontWeight.Bold, color = textPrimary)
                            Text("Dernière mise à jour : Juin 2026",
                                fontSize = 12.sp, color = textSecond)
                        }
                    }
                }
            }

            // ── Sections (icône + titre + contenu) ─────────────────
            items(sections.size) { i ->
                val section = sections[i]
                Surface(
                    modifier        = Modifier.fillMaxWidth(),
                    shape           = RoundedCornerShape(16.dp),
                    color           = surfaceColor,
                    shadowElevation = 2.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape    = RoundedCornerShape(10.dp),
                                color    = OrangeMain.copy(alpha = 0.12f),
                                modifier = Modifier.size(32.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(section.icon, null,
                                        tint = OrangeMain, modifier = Modifier.size(18.dp))
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text       = section.title,
                                fontSize   = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color      = textPrimary
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text       = section.content,
                            fontSize   = 13.sp,
                            color      = textSecond,
                            lineHeight = 21.sp
                        )
                    }
                }
            }

            // ── Contact (carte dédiée, sans emoji) ─────────────────
            item {
                Surface(
                    modifier        = Modifier.fillMaxWidth(),
                    shape           = RoundedCornerShape(16.dp),
                    color           = surfaceColor,
                    shadowElevation = 2.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Une question sur vos données ?",
                            fontSize   = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color      = textPrimary
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Outlined.Email, null,
                                tint = OrangeMain, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("privacy@ticketgo.ma", fontSize = 13.sp, color = textSecond)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Outlined.LocationOn, null,
                                tint = OrangeMain, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Larache, Maroc", fontSize = 13.sp, color = textSecond)
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}