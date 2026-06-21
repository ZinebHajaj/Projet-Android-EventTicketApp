package com.dcc.eventticketapp.ui.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dcc.eventticketapp.ui.theme.OrangeMain  // ← depuis Color.kt

data class ProfileMenuItem(
    val icon      : ImageVector,
    val label     : String,
    val subtitle  : String     = "",
    val hasToggle : Boolean    = false,
    val isToggled : Boolean    = false,
    val onToggle  : () -> Unit = {}
)

@Composable
fun MenuSection(
    title        : String,
    items        : List<ProfileMenuItem>,
    surfaceColor : Color,
    textPrimary  : Color,
    textSecond   : Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text       = title,
            fontSize   = 13.sp,
            fontWeight = FontWeight.Bold,
            color      = textSecond,
            modifier   = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )
        Surface(
            shape           = RoundedCornerShape(20.dp),
            color           = surfaceColor,
            shadowElevation = 2.dp
        ) {
            Column {
                items.forEachIndexed { index, item ->
                    MenuItemRow(item, textPrimary, textSecond)
                    if (index < items.lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color    = Color(0xFFF0F0F0)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MenuItemRow(
    item        : ProfileMenuItem,
    textPrimary : Color,
    textSecond  : Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !item.hasToggle) { }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(OrangeMain.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(item.icon, null,
                tint = OrangeMain, modifier = Modifier.size(20.dp))
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(item.label, fontSize = 14.sp,
                fontWeight = FontWeight.Medium, color = textPrimary)
            if (item.subtitle.isNotEmpty()) {
                Text(item.subtitle, fontSize = 12.sp, color = textSecond)
            }
        }

        if (item.hasToggle) {
            Switch(
                checked         = item.isToggled,
                onCheckedChange = { item.onToggle() },
                colors = SwitchDefaults.colors(
                    checkedThumbColor   = Color.White,
                    checkedTrackColor   = OrangeMain,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Color(0xFFCCCCCC)
                )
            )
        } else {
            Icon(Icons.Outlined.ChevronRight, null,
                tint = textSecond, modifier = Modifier.size(20.dp))
        }
    }
}