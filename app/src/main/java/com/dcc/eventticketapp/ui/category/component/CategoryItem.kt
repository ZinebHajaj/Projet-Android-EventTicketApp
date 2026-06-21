package com.dcc.eventticketapp.ui.category.component



import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dcc.eventticketapp.data.Entities.Category
import com.dcc.eventticketapp.ui.theme.OrangeMain

@Composable
fun CategoryItem(
    category     : Category,
    isSelected   : Boolean,
    surfaceColor : Color,
    textSecond   : Color,
    onClick      : () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier            = Modifier.clickable { onClick() }
    ) {
        Surface(
            shape           = RoundedCornerShape(16.dp),
            color           = if (isSelected) OrangeMain.copy(alpha = 0.15f)
            else surfaceColor,
            modifier        = Modifier.size(64.dp),
            shadowElevation = if (isSelected) 0.dp else 2.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector        = category.icon,
                    contentDescription = category.name,
                    tint               = if (isSelected) OrangeMain else textSecond,
                    modifier           = Modifier.size(26.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text       = category.name,
            fontSize   = 11.sp,
            color      = if (isSelected) OrangeMain else textSecond,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}