package com.dcc.eventticketapp.ui.eventDetail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dcc.eventticketapp.data.Entities.EventModel
import com.dcc.eventticketapp.ui.theme.DividerColor
import com.dcc.eventticketapp.ui.theme.OrangeLight
import com.dcc.eventticketapp.ui.theme.OrangeMain
import com.dcc.eventticketapp.ui.theme.TextGrayMode

@Composable
fun EventDetailInfo(event: EventModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {

        // ── 3 infos en cartes ───────────────────────────
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Date
            InfoCard(
                icon  = Icons.Outlined.CalendarMonth,
                label = "Date",
                value = event.date,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(10.dp))
            // Ville
            InfoCard(
                icon  = Icons.Outlined.LocationOn,
                label = "Lieu",
                value = event.city,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(10.dp))
            // Organisateur
            InfoCard(
                icon  = Icons.Outlined.Person,
                label = "Organisateur",
                value = "Officiel",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
        HorizontalDivider(color = DividerColor)
        Spacer(modifier = Modifier.height(20.dp))

        // ── Description ─────────────────────────────────
        Text(
            text       = "À propos de l'événement",
            fontSize   = 18.sp,
            fontWeight = FontWeight.Bold,
            color      = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text       = "Rejoignez-nous pour un événement inoubliable. " +
                    "Une expérience unique qui vous fera vivre des moments " +
                    "exceptionnels avec les meilleurs artistes de la région.",
            fontSize   = 14.sp,
            color      = TextGrayMode,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider(color = DividerColor)
        Spacer(modifier = Modifier.height(24.dp))

        // ── Prix ────────────────────────────────────────
        Row(
            modifier          = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text     = "Prix du billet",
                    fontSize = 13.sp,
                    color    = TextGrayMode
                )
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text       = "${event.priceStandard.toInt()}",
                        fontSize   = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color      = OrangeMain
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text     = "MAD",
                        fontSize = 14.sp,
                        color    = OrangeMain,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }
            // Badge dispo
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = OrangeLight
            ) {
                Text(
                    text     = "Places disponibles",
                    fontSize = 12.sp,
                    color    = OrangeMain,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }

    }
}

// ── Carte info réutilisable ──────────────────────────────────────
@Composable
private fun InfoCard(
    icon     : androidx.compose.ui.graphics.vector.ImageVector,
    label    : String,
    value    : String,
    modifier : Modifier = Modifier
) {
    Surface(
        modifier        = modifier,
        shape           = RoundedCornerShape(12.dp),
        color           = OrangeLight,
        shadowElevation = 0.dp
    ) {
        Column(
            modifier            = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector        = icon,
                contentDescription = null,
                tint               = OrangeMain,
                modifier           = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text     = label,
                fontSize = 10.sp,
                color    = TextGrayMode
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text       = value,
                fontSize   = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color      = MaterialTheme.colorScheme.onBackground,
                maxLines   = 1
            )
        }
    }
}