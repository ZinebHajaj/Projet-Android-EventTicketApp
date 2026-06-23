package com.dcc.eventticketapp.ui.home.component
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dcc.eventticketapp.ui.theme.OrangeMain

@Composable
fun SectionHeader(
    title       : String,
    textPrimary : Color,
    textSecond  : Color,
    showSeeAll: Boolean = true,
    onSeeAll    : () -> Unit
) {
    Row(
        modifier              = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Text(title, fontSize = 18.sp,
            fontWeight = FontWeight.Bold, color = textPrimary)

        if (showSeeAll) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onSeeAll() }
            ) {
                Text(
                    text = "Voir tout",
                    fontSize = 13.sp,
                    color = OrangeMain,
                    fontWeight = FontWeight.Medium
                )
                Icon(
                    Icons.Outlined.ChevronRight,
                    contentDescription = null,
                    tint = OrangeMain,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}