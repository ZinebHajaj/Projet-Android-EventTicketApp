package com.dcc.eventticketapp.ui.home.component



import androidx.compose.foundation.background
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
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.dcc.eventticketapp.ui.theme.BannerButton
import com.dcc.eventticketapp.ui.theme.OrangeMain
import com.dcc.eventticketapp.ui.theme.OverlayDark
import com.dcc.eventticketapp.ui.theme.OverlayDarkLight

@Composable
fun HeroBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(220.dp)
            .clip(RoundedCornerShape(20.dp))
    ) {
        AsyncImage(
            model              = "https://picsum.photos/seed/concert/800/400",
            contentDescription = null,
            modifier           = Modifier.fillMaxSize(),
            contentScale       = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(OverlayDark, OverlayDarkLight)
                    )
                )
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(20.dp)
        ) {
            Text("MUSIQUE LIVE", color = OrangeMain,
                fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Vivez la musique\nen direct !",
                color = Color.White, fontSize = 22.sp,
                fontWeight = FontWeight.Bold, lineHeight = 28.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Réservez vos billets pour les meilleurs\névénements de votre ville.",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 12.sp, lineHeight = 18.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Surface(
                shape    = RoundedCornerShape(30.dp),
                color    = BannerButton,
                modifier = Modifier.clickable { }
            ) {
                Row(
                    modifier          = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text       = "Explorer les événements",
                        color      = Color.White,
                        fontSize   = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        Icons.Outlined.ArrowForward, null,
                        tint     = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}