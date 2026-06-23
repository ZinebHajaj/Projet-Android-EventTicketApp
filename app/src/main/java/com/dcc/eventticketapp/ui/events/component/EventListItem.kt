package com.dcc.eventticketapp.ui.events.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.dcc.eventticketapp.ui.theme.OrangeLight
import com.dcc.eventticketapp.ui.theme.OrangeMain

@Composable
fun EventListItem(
    event: EventModel,
    surfaceColor: Color,
    textPrimary: Color,
    textSecond: Color,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = surfaceColor,
        shadowElevation = 3.dp
    ) {
        Column {
            // Image
            Box {
                AsyncImage(
                    model = event.imageUrl,
                    contentDescription = event.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(
                            RoundedCornerShape(
                                topStart = 20.dp,
                                topEnd = 20.dp
                            )
                        ),
                    contentScale = ContentScale.Crop
                )

                // Badge catégorie sur l'image
                Surface(
                    modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.TopStart),
                    shape = RoundedCornerShape(20.dp),
                    color = OrangeMain
                ) {
                    Text(
                        text = event.category,
                        fontSize = 10.sp,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(
                            horizontal = 10.dp, vertical = 4.dp
                        )
                    )
                }

                Surface(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(44.dp) // ← Plus grand pour faciliter le clic
                        .align(Alignment.TopEnd),
                    shape = RoundedCornerShape(50.dp),
                    color = Color.White.copy(alpha = 0.9f)
                ) {
                    IconButton(
                        onClick = onFavoriteClick,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = if (event.isFavorite)
                                Icons.Filled.Favorite
                            else
                                Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favori",
                            tint = if (event.isFavorite) OrangeMain else textSecond,
                            modifier = Modifier.size(20.dp) // ← Icône plus grande
                        )
                    }
                }
            }

            // Infos event
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = event.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.CalendarMonth, null,
                        modifier = Modifier.size(13.dp),
                        tint = OrangeMain
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(event.date, fontSize = 12.sp, color = textSecond)

                    Spacer(modifier = Modifier.width(12.dp))

                    Icon(
                        Icons.Outlined.LocationOn, null,
                        modifier = Modifier.size(13.dp),
                        tint = OrangeMain
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = event.city,
                        fontSize = 12.sp,
                        color = textSecond,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = OrangeLight
                    ) {
                        Text(
                            text = "Depuis ${event.priceStandard.toInt()} MAD",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = OrangeMain,
                            modifier = Modifier.padding(
                                horizontal = 10.dp, vertical = 4.dp
                            )
                        )
                    }
                }
            }
        }
    }
}

