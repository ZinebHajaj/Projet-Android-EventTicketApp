package com.dcc.mobile.ui.splash

import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dcc.eventticketapp.ui.auth.components.AppLogo
import com.dcc.eventticketapp.ui.theme.EventTicketAppTheme
import com.dcc.eventticketapp.ui.theme.OrangeMain
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    // ── Animations ────────────────────────────────────────────────
    val logoScale    = remember { Animatable(0f) }
    val logoAlpha    = remember { Animatable(0f) }
    val textAlpha    = remember { Animatable(0f) }
    val taglineAlpha = remember { Animatable(0f) }
    val dotsAlpha    = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // 1. Logo apparaît avec rebond
        logoScale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 600,
                easing = FastOutSlowInEasing
            )
        )
        logoAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 400)
        )

        // 2. Texte principal apparaît
        delay(200)
        textAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 500)
        )

        // 3. Tagline apparaît
        delay(200)
        taglineAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 500)
        )

        // 4. Points de chargement apparaissent
        delay(300)
        dotsAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 400)
        )

        // 5. Attendre puis naviguer vers Home
        delay(1500)
        onSplashFinished()
    }

    // ── UI ────────────────────────────────────────────────────────
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {

            AppLogo(
                boxSize      = 110.dp,
                iconSize     = 56.dp,
                cornerRadius = 28.dp,
                scale        = logoScale.value
            )

            Spacer(modifier = Modifier.height(28.dp))

            // ── Nom de l'app ──────────────────────────────────────
            Text(
                text = "TicketGo",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.graphicsLayer(alpha = textAlpha.value)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ── Tagline ───────────────────────────────────────────
            Text(
                text = "Vos billets, en un clic.",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.graphicsLayer(alpha = taglineAlpha.value)
            )

            Spacer(modifier = Modifier.height(60.dp))

            // ── Points de chargement animés ───────────────────────
            LoadingDots(
                modifier = Modifier.graphicsLayer(alpha = dotsAlpha.value)
            )
        }

        // ── Version en bas ────────────────────────────────────────
        Text(
            text = "Version 1.0.0",
            fontSize = 12.sp,
            color = Color(0xFFAAAAAA),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        )
    }
}

// ── Points de chargement animés ───────────────────────────────────
@Composable
private fun LoadingDots(modifier: Modifier = Modifier) {
    val dot1 = remember { Animatable(0.4f) }
    val dot2 = remember { Animatable(0.4f) }
    val dot3 = remember { Animatable(0.4f) }

    LaunchedEffect(Unit) {
        while (true) {
            dot1.animateTo(1f, tween(300))
            dot2.animateTo(1f, tween(300))
            dot3.animateTo(1f, tween(300))
            delay(200)
            dot1.animateTo(0.4f, tween(300))
            dot2.animateTo(0.4f, tween(300))
            dot3.animateTo(0.4f, tween(300))
            delay(200)
        }
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        listOf(dot1, dot2, dot3).forEach { dot ->
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .scale(dot.value)
                    .clip(CircleShape)
                    .background(OrangeMain)
            )
        }
    }
}

@Preview (showBackground = true, showSystemUi = true)
@Composable
private fun SplashScreenPreview() {
    EventTicketAppTheme() {
        SplashScreen(
            onSplashFinished = {}
        )
    }
}