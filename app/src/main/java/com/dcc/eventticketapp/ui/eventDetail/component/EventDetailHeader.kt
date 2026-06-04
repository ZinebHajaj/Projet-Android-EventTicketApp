package com.dcc.eventticketapp.ui.eventDetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.dcc.eventticketapp.ui.theme.OrangeMain

@Composable
fun EventDetailHeader(
    imageUrl   : String,
    title      : String,
    category   : String,
    isFavorite : Boolean,
    onBack     : () -> Unit,
    onFavorite : () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(380.dp)  // ← grande image
    ) {
        // Image plein écran
        AsyncImage(
            model              = imageUrl,
            contentDescription = null,
            modifier           = Modifier.fillMaxSize(),
            contentScale       = ContentScale.Crop
        )

        // Overlay dégradé du bas
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.3f),
                            Color.Black.copy(alpha = 0.8f)
                        )
                    )
                )
        )

        // Bouton retour en haut à gauche
        Surface(
            modifier = Modifier
                .padding(top = 48.dp, start = 16.dp)  // ← padding top pour barre statut
                .size(42.dp)
                .align(Alignment.TopStart),
            shape = CircleShape,
            color = Color.White.copy(alpha = 0.85f)
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.Outlined.ArrowBack,
                    contentDescription = "Retour",
                    tint               = Color.Black
                )
            }
        }

        // Bouton favori en haut à droite
        Surface(
            modifier = Modifier
                .padding(top = 48.dp, end = 16.dp)
                .size(42.dp)
                .align(Alignment.TopEnd),
            shape = CircleShape,
            color = Color.White.copy(alpha = 0.85f)
        ) {
            IconButton(onClick = onFavorite) {
                Icon(
                    imageVector = if (isFavorite)
                        Icons.Filled.Favorite
                    else
                        Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favori",
                    tint               = if (isFavorite) OrangeMain else Color.Gray
                )
            }
        }

        // Titre + catégorie en bas de l'image sur l'overlay
        androidx.compose.foundation.layout.Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(20.dp)
        ) {
            // Badge catégorie
            Surface(
                shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
                color = OrangeMain
            ) {
                Text(
                    text     = category,
                    color    = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }

            androidx.compose.foundation.layout.Spacer(
                modifier = Modifier.height(8.dp)
            )

            // Titre sur l'image
            Text(
                text       = title,
                fontSize   = 26.sp,
                fontWeight = FontWeight.Bold,
                color      = Color.White,
                lineHeight = 32.sp
            )
        }
    }
}