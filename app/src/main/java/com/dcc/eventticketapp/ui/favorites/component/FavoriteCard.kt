package com.dcc.eventticketapp.ui.favorites.component
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.dcc.eventticketapp.data.Entities.EventModel
import com.dcc.eventticketapp.ui.theme.OrangeMain

@Composable
fun FavoriteCard(
    event           : EventModel,
    onClick         : () -> Unit,
    onRemoveClick   : () -> Unit,
    modifier        : Modifier = Modifier
) {
    Card(
        modifier  = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape     = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.height(220.dp)) {

            // ── Image plein format ────────────────────────────────
            AsyncImage(
                model              = event.imageUrl,
                contentDescription = event.title,
                modifier           = Modifier.fillMaxSize(),
                contentScale       = ContentScale.Crop
            )

            // ── Dégradé sombre en bas ─────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color(0xCC000000)
                            ),
                            startY = 100f
                        )
                    )
            )

            // ── Badge catégorie en haut à gauche ─────────────────
            Surface(
                modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.TopStart),
                shape    = RoundedCornerShape(20.dp),
                color    = OrangeMain
            ) {
                Text(
                    text     = event.category,
                    fontSize = 10.sp,
                    color    = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(
                        horizontal = 10.dp, vertical = 4.dp
                    )
                )
            }

            // ── Bouton supprimer favori en haut à droite ──────────
            IconButton(
                onClick  = onRemoveClick,
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = Color(0x99000000)
                ) {
                    Icon(
                        Icons.Filled.Favorite,
                        contentDescription = "Retirer des favoris",
                        tint     = OrangeMain,
                        modifier = Modifier
                            .padding(6.dp)
                            .size(18.dp)
                    )
                }
            }

            // ── Infos en bas ──────────────────────────────────────
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            ) {
                Text(
                    text       = event.title,
                    fontSize   = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color      = Color.White,
                    maxLines   = 2,
                    overflow   = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.LocationOn, null,
                        modifier = Modifier.size(11.dp),
                        tint     = Color.White.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text     = event.city,
                        fontSize = 11.sp,
                        color    = Color.White.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        Icons.Outlined.CalendarMonth, null,
                        modifier = Modifier.size(11.dp),
                        tint     = Color.White.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text     = event.date,
                        fontSize = 11.sp,
                        color    = Color.White.copy(alpha = 0.8f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Prix avec fond orange
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = OrangeMain
                ) {
                    Text(
                        text       = "Depuis ${event.priceStandard.toInt()} MAD",
                        fontSize   = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color      = Color.White,
                        modifier   = Modifier.padding(
                            horizontal = 10.dp, vertical = 4.dp
                        )
                    )
                }
            }
        }
    }
}