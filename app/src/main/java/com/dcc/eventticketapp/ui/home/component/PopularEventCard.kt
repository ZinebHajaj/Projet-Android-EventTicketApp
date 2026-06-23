package com.dcc.eventticketapp.ui.home.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.dcc.eventticketapp.ui.theme.OrangeMain

@Composable
fun PopularEventCard(
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
            .padding(horizontal = 16.dp, vertical = 5.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = surfaceColor,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = event.imageUrl,
                contentDescription = event.title,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    event.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.LocationOn, null,
                        modifier = Modifier.size(12.dp), tint = textSecond
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(event.city, fontSize = 12.sp, color = textSecond)
                }
                Spacer(modifier = Modifier.height(3.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.CalendarMonth, null,
                        modifier = Modifier.size(12.dp), tint = textSecond
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(event.date, fontSize = 12.sp, color = textSecond)
                }
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    "Depuis ${event.priceStandard.toInt()} MAD",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = OrangeMain
                )
            }

            Surface(
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(50),
                color = Color.Transparent
            ) {
                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = if (event.isFavorite) Icons.Filled.Favorite
                        else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favori",
                        tint = if (event.isFavorite) OrangeMain else textSecond,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}