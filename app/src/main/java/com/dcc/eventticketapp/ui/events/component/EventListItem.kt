package com.dcc.eventticketapp.ui.events.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
fun EventListItem(
    event           : EventModel,
    surfaceColor    : Color,
    textPrimary     : Color,
    textSecond      : Color,
    onClick         : () -> Unit,
    onFavoriteClick : () -> Unit
) {
    Surface(
        modifier        = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 5.dp)
            .clickable { onClick() },
        shape           = RoundedCornerShape(16.dp),
        color           = surfaceColor,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier          = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image
            AsyncImage(
                model              = event.imageUrl,
                contentDescription = event.title,
                modifier           = Modifier
                    .size(85.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale       = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(14.dp))

            // Infos
            Column(modifier = Modifier.weight(1f)) {

                // Badge catégorie
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = OrangeMain.copy(alpha = 0.15f)
                ) {
                    Text(
                        text     = event.category,
                        fontSize = 10.sp,
                        color    = OrangeMain,
                        modifier = Modifier.padding(
                            horizontal = 8.dp, vertical = 2.dp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text       = event.title,
                    fontSize   = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color      = textPrimary,
                    maxLines   = 2,
                    overflow   = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.LocationOn, null,
                        modifier = Modifier.size(12.dp), tint = textSecond)
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(event.city, fontSize = 12.sp, color = textSecond)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.CalendarMonth, null,
                        modifier = Modifier.size(12.dp), tint = textSecond)
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(event.date, fontSize = 12.sp, color = textSecond)
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text       = "Depuis ${event.priceStandard.toInt()} MAD",
                    fontSize   = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color      = OrangeMain
                )
            }

            // Favori
            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = if (event.isFavorite)
                        Icons.Filled.Favorite
                    else
                        Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favori",
                    tint = if (event.isFavorite) OrangeMain else textSecond,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}